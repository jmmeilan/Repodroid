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

public class CuckooAnalyzer implements Analyzer {

    String urlCreate;
    String urlView;
    String urlReport;
    ObjectMapper mapper;

    @Override
    public void initialize() {
        System.out.println("Initializing analyzer...");
        this.urlCreate = "http://localhost:8090/tasks/create/file";
        this.urlView = "http://localhost:8090/tasks/view/";
        this.urlReport= "http://localhost:8090/tasks/report/";
        this.mapper = new ObjectMapper();
    }

    @Override
    public void terminate() {
        System.out.println("Terminating analyzer...");
    }

    @Override
    public List<String> getAnalysesNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Analysis> analyzeSample(Sample sample) {  
        //Enviamos un post multipart con el apk a analizar
        /*HttpPost httppost = new HttpPost(this.urlCreate);
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
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                JsonNode rootNode = mapper.readValue(instream, JsonNode.class); 
                sample.setId(rootNode.path("task_id").asLong());
            }
            response.close();
        } catch (IOException ex) {
            Logger.getLogger(CuckooAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Comprobamos que el analisis termine
        //TODO: como hacer que el servidor me mande que ya esta listo por su cuenta?
        boolean analyzing = true;
        do {
            HttpGet httpget = new HttpGet(this.urlView+sample.getId());
            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    JsonNode rootNode = mapper.readValue(instream, JsonNode.class);
                    if(rootNode.path("task").path("status").asText().equals("reported"))
                        analyzing = false;
                    System.out.println("Comprobando estado del analisis...");
                    Thread.sleep(60000); 
                }
                response.close();
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(CuckooAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (analyzing);
        */
        //BORRAR SIG 2 LINEAS
        //Extraemos la informacion relevante del informe
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response;
        List<Analysis> toRet = new LinkedList<>();
        HttpGet httpget = new HttpGet(this.urlReport+"4"/*sample.getId()*/);
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
            //TENGO MAS ANTIVIRUS Y CLASES DE LAS Q DEBERIA?
            }
                response.close();
            } catch (IOException ex) {
                Logger.getLogger(CuckooAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.println("Returning network analysis...");
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
        System.out.println("Returning antivirus analysis...");
        return avAnalysis;
    }
    
    private ApkClassesAnalysis extractApkClassesAnalysis (JsonNode rootNode){
        Set<String> classes = new HashSet<>();
        for(JsonNode n: rootNode){
            classes.add(n.path("name").asText());
        }
        ApkClassesAnalysis apkAnalysis = new ApkClassesAnalysis();
        apkAnalysis.setClasses(classes);
        System.out.println("Returning apk classes analysis...");
        return apkAnalysis;
    }
    
    private ApkPermissionsAnalysis extractApkPermissionsAnalysis (JsonNode rootNode){
        Set<String> permissions = new HashSet<>();
        for(JsonNode n: rootNode){
            permissions.add(n.path("name").asText());
        }
        ApkPermissionsAnalysis permissionsAnalysis = new ApkPermissionsAnalysis();
        permissionsAnalysis.setPermissions(permissions);
        System.out.println("Returning apk permissions analysis...");
        return permissionsAnalysis;
    }

}
