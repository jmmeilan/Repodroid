package es.uvigo.esei.tfg.repodroid.web.services;

import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.core.model.ParametrizedQuery;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.SimilarityQuery;
import es.uvigo.esei.tfg.repodroid.core.store.SampleStore;
import es.uvigo.esei.tfg.repodroid.core.store.TermInfo;
import es.uvigo.esei.tfg.repodroid.core.store.json.JSONStorer;
import es.uvigo.esei.tfg.repodroid.core.store.lucene.LuceneIndexer;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class RepodroidService {

    public static final String REPODROID_HOME = System.getProperty("user.home")
            + File.separator + "REPODROID";

    private SampleStore store;
    private CuckooAnalyzer analyzer;
    private static Handler fh;
    private static Logger logger;

    @PostConstruct
    public void inicializar() {
        File baseDir = new File(REPODROID_HOME);
        baseDir.mkdirs();

        File logDir = new File(baseDir, "log");
        logDir.mkdirs();

        File sampleStoreDir = new File(baseDir, "SAMPLE_STORE");
        sampleStoreDir.mkdirs();

        File samplesDir = new File(baseDir, "SAMPLES");
        samplesDir.mkdirs();

        String logPath = logDir.getAbsolutePath() + File.separator + "system.log";
        String sampleStorePath = sampleStoreDir.getAbsolutePath();

        try {
            fh = new FileHandler(logPath);
            logger = Logger.getLogger("");
            logger.addHandler(fh);
        } catch (IOException | SecurityException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        this.analyzer = new CuckooAnalyzer();
        this.analyzer.initialize(logger);
        this.store = new SampleStore(sampleStorePath);
        this.store.setStorer(new JSONStorer());
        this.store.setIndexer(new LuceneIndexer());
        this.store.initialize(logger);
    }

    @PreDestroy
    public void terminate() {
        this.store.close();
        this.analyzer.terminate();
    }

    public void analyzeSample(Sample s, String email) {
        Thread thread = (new Thread(
                new AnalysisWorker(this.store, this.analyzer,
                        s,
                        email)));
        thread.start();
    }

    public Sample retrieveSample(String id) {
        return this.store.retrieveSample(id);
    }

    public void saveSample(Sample s) {
        this.store.storeSample(s);
    }

    public List<Sample> similaritySearch(Sample s) {
        return this.store.search(new SimilarityQuery(s, 50), 0, 5);
    }

    public List<Sample> parametrizedSearch(Map<String, List<String>> parameters) {
        return this.store.search(new ParametrizedQuery(parameters), 5, 5);
    }

    public List<TermInfo> retrieveTermInfoForIndexableAnalysis(String indexableAnalysisName) {
        return this.store.retrieveTermInfoForIndexableAnalysis(indexableAnalysisName);
    }

    public List<TermInfo> retrieveTermInfoForIndexableAnalysis(String indexableAnalysisName, int maxTerms) {
        return this.store.retrieveTermInfoForIndexableAnalysis(indexableAnalysisName, maxTerms);
    }

    public String composeSamplePath(String username, String sampleName) {
        return REPODROID_HOME + File.separator + "SAMPLES" + File.separator
                + username + "_" + sampleName;
    }
}
