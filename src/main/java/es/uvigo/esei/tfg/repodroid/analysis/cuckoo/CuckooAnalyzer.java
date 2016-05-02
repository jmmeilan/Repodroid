package es.uvigo.esei.tfg.repodroid.analysis.cuckoo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import java.util.List;
import es.uvigo.esei.tfg.repodroid.analysis.Analyzer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CuckooAnalyzer implements Analyzer, Serializable {

    private String urlCreate;
    private String urlView;
    private String urlReport;
    private ObjectMapper mapper;
    private List<String> analysesNames;
    private Logger logger;
    

    @Override
    public void initialize(Logger l) {
        this.logger = l;
        this.logger.log(Level.INFO, "Initializing analyzer..."); 
        this.urlCreate = "http://localhost:8090/tasks/create/file";
        this.urlView = "http://localhost:8090/tasks/view/";
        this.urlReport= "http://localhost:8090/tasks/report/";
        this.mapper = new ObjectMapper();
        this.analysesNames = new LinkedList();
        this.analysesNames.add(OutputConnectionsAnalysis.NAME);
        this.analysesNames.add(AntiVirusAnalysis.NAME);
        this.analysesNames.add(ApkClassesAnalysis.NAME);
        this.analysesNames.add(ApkPermissionsAnalysis.NAME);
    }

    @Override
    public void terminate() {
        this.logger.log(Level.INFO, "Terminating analyzer..."); 
    }

    @Override
    public List<String> getAnalysesNames() {
        return this.analysesNames;
    }

    @Override
    public List<Analysis> analyzeSample(Sample sample) {  
        this.logger.log(Level.INFO, "Analyzing a new sample..."); 
        //Enviamos un post multipart con el apk a analizar
        HttpPost httppost = new HttpPost(this.urlCreate);
        File apk = new File(sample.getPath());
        //Incluimos el apk a analizar
        HttpEntity multipart = MultipartEntityBuilder
                .create()
                .addBinaryBody("file", apk, 
                        ContentType.create("application/octet-stream"), 
                        apk.getName())
                .build();
        httppost.setEntity(multipart);
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response;
        int sampleID = 0;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                JsonNode rootNode = mapper.readValue(instream, JsonNode.class); 
                sampleID = rootNode.path("task_id").asInt(); 
            }
            response.close();
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, null, ex);
        }
        //Comprobamos que el analisis termine
        //TODO: como hacer que el servidor me notifique que ya esta listo por su cuenta?
        boolean analyzing = true;
        do {
            HttpGet httpget = new HttpGet(this.urlView+sampleID);
            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    JsonNode rootNode = mapper.readValue(instream, JsonNode.class);
                    if(rootNode.path("task").path("status").asText().equals("reported"))
                        analyzing = false;
                    this.logger.log(Level.INFO, "Checking if the analysis is done..."); 
                    Thread.sleep(30000); 
                }
                response.close();
            } catch (IOException | InterruptedException ex) {
                this.logger.log(Level.SEVERE, null, ex);
            }
        } while (analyzing);
        //Extraemos la informacion relevante del informe
        List<Analysis> toRet = new LinkedList<>();
        HttpGet httpget = new HttpGet(this.urlReport+sampleID);
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                JsonNode rootNode = mapper.readValue(instream, JsonNode.class); // src can be a File, URL, InputStream etc 
                toRet.add(extractNetworkAnalysis(rootNode.at("/network")));
                toRet.add(extractAntivirusAnalysis(rootNode.at("/virustotal")));
                toRet.add(extractApkClassesAnalysis(rootNode.at("/apkinfo/files")));
                toRet.add(extractApkPermissionsAnalysis(rootNode.at("/apkinfo/manifest/permissions")));
            }
                response.close();
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, null, ex);
            }
       return toRet;
    }
    
    private OutputConnectionsAnalysis extractNetworkAnalysis(JsonNode rootNode) {

        Set<String> externalHosts = new HashSet<>();
        for (JsonNode n : rootNode.at("/hosts")) {
            externalHosts.add(n.asText());
        }

        Set<String> dnsQueries = new HashSet<>();
        for (JsonNode n : rootNode.at("/dns")) {
            dnsQueries.add(n.path("request").asText());
        }
            
        OutputConnectionsAnalysis networkAnalysys = new OutputConnectionsAnalysis();
        networkAnalysys.setExternalHosts(externalHosts);
        networkAnalysys.setDnsQueries(dnsQueries);
        this.logger.log(Level.INFO, "Returning network analysis..."); 
        return networkAnalysys;
    }
    
    private AntiVirusAnalysis extractAntivirusAnalysis(JsonNode rootNode){
        
        Set<String> antiViruses = new HashSet<>();
        
        Iterator<String> it = rootNode.at("/scans").fieldNames();
        while(it.hasNext()){
            String field = it.next();
            if(rootNode.at("/scans/"+field).path("detected").asBoolean()){
                antiViruses.add(field);
            }
        } 
        AntiVirusAnalysis avAnalysis = new AntiVirusAnalysis();
        avAnalysis.setAntiVirusList(antiViruses);
        avAnalysis.setScanDate(rootNode.path("scan_date").asText());
        avAnalysis.setPositives(rootNode.path("positives").asInt());
        avAnalysis.setTotal(rootNode.path("total").asInt());
        this.logger.log(Level.INFO, "Returning antivirus analysis..."); 
        return avAnalysis;
    }
    
    private ApkClassesAnalysis extractApkClassesAnalysis (JsonNode rootNode){
        Set<String> classes = new HashSet<>();
        for(JsonNode n: rootNode){
            classes.add(n.path("name").asText());
        }
        ApkClassesAnalysis apkAnalysis = new ApkClassesAnalysis();
        apkAnalysis.setClasses(classes);
        this.logger.log(Level.INFO, "Returning apk classes analysis..."); 
        return apkAnalysis;
    }
    
    private ApkPermissionsAnalysis extractApkPermissionsAnalysis (JsonNode rootNode){
        ApkPermissionsAnalysis permissionsAnalysis = new ApkPermissionsAnalysis();
        for(JsonNode n: rootNode){
            permissionsAnalysis.addPermission(n.path("name").asText(), n.path("severity").asText());
        }
        this.logger.log(Level.INFO, "Returning apk permissions analysis..."); 
        return permissionsAnalysis;
    }

}
