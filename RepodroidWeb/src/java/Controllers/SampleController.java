package Controllers;

import Core.RepodroidAnalyzer;
import Core.SampleReference;
import Core.SampleReferenceDao;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleType;
import es.uvigo.esei.tfg.repodroid.store.SampleStore;
import es.uvigo.esei.tfg.repodroid.store.json.JSONStorer;
import es.uvigo.esei.tfg.repodroid.store.lucene.LuceneIndexer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

@Named(value = "SampleController")
//DEBE SER ESTO REQUESTSCOPED O CONVERSATIONSCOPED???? 
@SessionScoped
public class SampleController implements Serializable {

    private static Handler fh;
    private static Logger logger;
    private SampleStore store;
    private JSONStorer jsonStorer;
    private LuceneIndexer luceneIndexer;
    private CuckooAnalyzer analyzer;
    private SampleReference sample;
    private Part apkSample;
    //LISTS OF PARAMETERS FOR THE QUERY, needs to be converted to a list (SEPARAR POR COMAS PARA METER EN LISTA)
    private String permissions;
    private String classes;
    private String outputConnections;
    private String antiViruses;
    @Inject
    private UserController userBean;

    @EJB
    private SampleReferenceDao sampleDao;

    public Part getApkSample() {
        return apkSample;
    }

    public void setApkSample(Part apkSample) {
        this.apkSample = apkSample;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getOutputConnections() {
        return outputConnections;
    }

    public void setOutputConnections(String outputConnections) {
        this.outputConnections = outputConnections;
    }

    public String getAntiViruses() {
        return antiViruses;
    }

    public void setAntiViruses(String antiViruses) {
        this.antiViruses = antiViruses;
    }

    public SampleController() {
    }

    public SampleReference getSample() {
        return sample;
    }

    public void setSample(SampleReference sample) {
        this.sample = sample;
    }

    @PostConstruct
    public void inicializar() {
        this.sample = new SampleReference();
        try {
            fh = new FileHandler("/home/jmmeilan/Descargas/Repodroid/"
                    + "RepodroidWeb/web/resources/webResources/log/system.log");
            logger = Logger.getLogger("");
            logger.addHandler(fh);
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        this.store = new SampleStore("/home/jmmeilan/Descargas/Repodroid/"
                + "RepodroidWeb/web/resources/sampleStore");
        this.jsonStorer = new JSONStorer();
        this.luceneIndexer = new LuceneIndexer();
        this.store.setStorer(jsonStorer);
        this.store.setIndexer(luceneIndexer);
        this.store.initialize(logger);
        this.analyzer = new CuckooAnalyzer();
        this.analyzer.initialize(logger);
    }

    public void submitSample() {
        String extension = this.apkSample.getSubmittedFileName()
                .substring(apkSample.getSubmittedFileName().lastIndexOf(".") + 1, apkSample.getSubmittedFileName().length());
        if ((this.apkSample != null)
                && (extension.equals("apk"))) {
            String samplePath = "/home/jmmeilan/Descargas/Repodroid/"
                    + "RepodroidWeb/web/resources/sampleStore/SAMPLES/"
                    + this.userBean.getCurrentUser().getUsername() + "_"
                    + this.apkSample.getSubmittedFileName();
            try (InputStream input = this.apkSample.getInputStream()) {
                File samp = new File(samplePath);
                if (!samp.exists()) {
                    Files.copy(input, samp.toPath());
                }
                this.sample.setSamplePath(samplePath);
                this.sample.setUser(this.userBean.getCurrentUser());
                Sample sampleToAnalyze = new Sample(samplePath, SampleType.APK);
                sampleToAnalyze.setId(UUID.randomUUID().toString());
                this.sample.setStorerID(sampleToAnalyze.getId());
                Thread thread = (new Thread(
                        new RepodroidAnalyzer(this.store, this.analyzer, 
                                              sampleToAnalyze, 
                                              this.userBean.getCurrentUser().getEmail())));
                thread.start();
                this.sampleDao.create(this.sample);
                this.apkSample.getInputStream().close();
            } catch (IOException ex) {
                Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You must submit an .apk file", ""));
        }
    }

    public List<SampleReference> getSamplesFromUser() {
        List<SampleReference> currentUserSamples = this.sampleDao.getAllSamplesFromUser(this.userBean.getCurrentUser().getNumUser());
        return currentUserSamples;
    }

    public void doSimilarityQuery() {

    }

    public void doParametrizedQuery() {

    }
}
