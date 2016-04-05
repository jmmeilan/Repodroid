package es.uvigo.esei.tfg.repodroid.pruebas;

import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.AntiVirusAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.ApkClassesAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.ApkPermissionsAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.OutputConnectionsAnalysis;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleType;
import es.uvigo.esei.tfg.repodroid.core.SimilarityQuery;
import es.uvigo.esei.tfg.repodroid.core.permissionInfo;
import es.uvigo.esei.tfg.repodroid.store.SampleStore;
import es.uvigo.esei.tfg.repodroid.store.json.JSONStorer;
import es.uvigo.esei.tfg.repodroid.store.lucene.LuceneIndexer;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;


public class Prueba {
    private static Handler fh;
    private static Logger logger; //Pasar en el initialize de todo lo q lo use y cambiar system outs
    
    public final static void main(String[] args) throws IOException {
        // Inicializar almacen de muestras
        fh = new FileHandler("/home/jmmeilan/Cuckoo/prueba.log");  
        logger = Logger.getLogger("");
        logger.addHandler(fh);
        SampleStore store = new SampleStore("/home/jmmeilan/Cuckoo/Sample_Store");
        JSONStorer jsonStorer = new JSONStorer();
        LuceneIndexer luceneIndexer = new LuceneIndexer();
        store.setStorer(jsonStorer);
        store.setIndexer(luceneIndexer);
        store.initialize(logger);
        // Inicializar el analizador
        CuckooAnalyzer analyzer = new CuckooAnalyzer();
        analyzer.initialize(logger);
        
        // Analizar una  muestra
        Sample sample = new Sample("/home/jmmeilan/Cuckoo/Malware/good_2C3CCE76067676A2CCAC9CC7546BF8AB.apk", SampleType.APK);
        List<Analysis> analyses = analyzer.analyzeSample(sample);
        for (Analysis analysis : analyses) {
            sample.addAnalysis(analysis.getAnalysisName(), analysis);
        }
        store.storeSample(sample);
        /*List<Sample> result = store.search(new SimilarityQuery(sample, 50), 0, 5);
        for(Sample s: result){
            testJsonRetrieval(s);
        }*/
        store.close();        
        analyzer.terminate();
    }
    
    private static void testJsonRetrieval(Sample prueba){
        for (Analysis an: prueba.getAnalises().values()){
            if (an.getAnalysisType().equals("OutputConnectionsAnalysis"))
            {
                OutputConnectionsAnalysis analysis = (OutputConnectionsAnalysis) an;
                System.out.println(analysis.getAnalysisDescription()+" "+analysis.getAnalysisName()+" "+analysis.getAnalysisType());
                for (String s: analysis.getDnsQueries()){
                    System.out.println(s);
                }
                for (String s: analysis.getExternalHosts()){
                    System.out.println(s);
                }
            }
            if (an.getAnalysisType().equals("AntiVirusAnalysis"))
            {
                AntiVirusAnalysis analysis = (AntiVirusAnalysis) an;
                System.out.println(analysis.getAnalysisDescription()+" "+analysis.getAnalysisName()+" "+analysis.getAnalysisType());
                for (String s: analysis.getAntiVirusList()){
                    System.out.println(s);
                }
                System.out.println(analysis.getPositives()+" "+analysis.getTotal()+" "+analysis.getScanDate());
            }
            if (an.getAnalysisType().equals("ApkClassesAnalysis"))
            {
                ApkClassesAnalysis analysis = (ApkClassesAnalysis) an;
                System.out.println(analysis.getAnalysisDescription()+" "+analysis.getAnalysisName()+" "+analysis.getAnalysisType());
                for (String s: analysis.getClasses()){
                    System.out.println(s);
                }
            }
            if (an.getAnalysisType().equals("ApkPermissionsAnalysis"))
            {
                ApkPermissionsAnalysis analysis = (ApkPermissionsAnalysis) an;
                System.out.println(analysis.getAnalysisDescription()+" "+analysis.getAnalysisName()+" "+analysis.getAnalysisType());
                for (permissionInfo s: analysis.getPermissions()){
                    System.out.println(s.toString());
                }
            }
        }
    }
}
