package es.uvigo.esei.tfg.repodroid.web.services;

import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.core.model.ParametrizedQuery;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.SimilarityQuery;
import es.uvigo.esei.tfg.repodroid.core.store.SampleStore;
import es.uvigo.esei.tfg.repodroid.core.store.json.JSONStorer;
import es.uvigo.esei.tfg.repodroid.core.store.lucene.LuceneIndexer;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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

    public static String logPath = "/home/jmmeilan/Descargas/Repodroid/"
            + "RepodroidWeb/web/resources/webResources/log/system.log";
    public static String sampleStorePath = "/home/jmmeilan/Descargas/Repodroid/"
            + "RepodroidWeb/web/resources/sampleStore";

    private SampleStore store;
    private CuckooAnalyzer analyzer;
    private static Handler fh;
    private static Logger logger;

    @PostConstruct
    public void inicializar() {
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
    
    public void saveSample(Sample s){
        this.store.storeSample(s);
    }

    public List<Sample> similaritySearch(Sample s) {
        return this.store.search(new SimilarityQuery(s, 50), 0, 5);
    }

    public List<Sample> parametrizedSearch(String antiViruses,
            String classes,
            String permissions,
            String outputConnections) {

        Map<String, List<String>> parameters = new HashMap();
        parameters.put("AntiVirusAnalysis", Arrays.asList(antiViruses.split(",")));
        parameters.put("ApkClassesAnalysis", Arrays.asList(classes.split(",")));
        parameters.put("ApkPermissionsAnalysis", Arrays.asList(permissions.split(",")));
        parameters.put("OutputConnectionsAnalysis", Arrays.asList(outputConnections.split(",")));
        return this.store.search(new ParametrizedQuery(parameters), 5, 5);
    }
}
