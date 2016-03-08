package es.uvigo.esei.tfg.repodroid.pruebas;

import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleType;
import es.uvigo.esei.tfg.repodroid.store.SampleStore;
import es.uvigo.esei.tfg.repodroid.store.json.JSONStorer;
import es.uvigo.esei.tfg.repodroid.store.lucene.LuceneIndexer;
import java.util.List;


public class Prueba {
    public final static void main(String[] args) {
        // Inicializar almacen de muestras
        SampleStore store = new SampleStore("/home/jmmeilan/Cuckoo/Sample_Store");
        JSONStorer jsonStorer = new JSONStorer();
        LuceneIndexer luceneIndexer = new LuceneIndexer();
        store.setStorer(jsonStorer);
        store.setIndexer(luceneIndexer);
        store.initialize();
                
        
        // Inicializar el analizador
        CuckooAnalyzer analyzer = new CuckooAnalyzer();
        analyzer.initialize();
        
        // Analizar una  muestra
       Sample sample = new Sample("/home/jmmeilan/Cuckoo/Malware/good_2C3CCE76067676A2CCAC9CC7546BF8AB.apk", SampleType.APK);
       List<Analysis> analyses = analyzer.analyzeSample(sample);
        for (Analysis analysis : analyses) {
            sample.addAnalysis(analysis.getAnalysisName(), analysis);
        }
        //store.removeSample("4a46dc03-38d7-4677-bae9-bf607897e614");
        store.storeSample(sample);
        //store.updateSample("4a46dc03-38d7-4677-bae9-bf607897e614", sample);
        store.close();        
        analyzer.terminate();
    }
}
