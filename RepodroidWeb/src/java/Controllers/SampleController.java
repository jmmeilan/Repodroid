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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean(name = "SampleController")
@SessionScoped
public class SampleController {

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
    
/* TODO: METER LOS PATHS ESTATICOS EN UN FICHERO APARTE PARA SU ACCESO Y MODIFICACION MAS FACIL*/
    @ManagedProperty("#{UserController}")
    private UserController userBean;

    @EJB
    private SampleReferenceDao sampleDao;

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
            fh = new FileHandler("/home/jmmeilan/Cuckoo/prueba.log");
            logger = Logger.getLogger("");
            logger.addHandler(fh);
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        this.store = new SampleStore("/home/jmmeilan/Cuckoo/Sample_Store");
        this.jsonStorer = new JSONStorer();
        this.luceneIndexer = new LuceneIndexer();
        this.store.setStorer(jsonStorer);
        this.store.setIndexer(luceneIndexer);
        this.store.initialize(logger);
        this.analyzer = new CuckooAnalyzer();
        analyzer.initialize(logger);
        
    }

    public void submitSample() {
        if ((this.apkSample != null) && (this.apkSample.getSubmittedFileName().contains(".apk"))) {
            try (InputStream input = this.apkSample.getInputStream()) {
                String samplePath = "/home/jmmeilan/Cuckoo/ApkSamples/" + this.apkSample.getSubmittedFileName();
                Files.copy(input, new File(samplePath).toPath());
                this.sample.setSamplePath(samplePath);
                this.sample.setUser(this.userBean.getCurrentUser());
                Sample sampleToAnalyze = new Sample(samplePath, SampleType.APK);
                sampleToAnalyze.setId(this.store.computeNextSampleID());
                this.sample.setStorerID(sampleToAnalyze.getId());
                Thread thread = (new Thread(new RepodroidAnalyzer(this.store, this.analyzer, sampleToAnalyze)));
                thread.start();
                this.sampleDao.create(this.sample);
                this.apkSample.getInputStream().close();
                this.store.close();
                this.analyzer.terminate();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "You must submit an .apk file", ""));
        }
    }
    
    public void doSimilarityQuery(){
        
    }
    
    public void doParametrizedQuery(){
        
    }
}
