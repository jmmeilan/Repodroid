package es.uvigo.esei.tfg.repodroid.pruebas;

import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.OutputConnectionsAnalysis;
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
        //store.storeSample(sample);
        Sample prueba = store.retrieveSample("a6755fa4-4cb1-414d-8bdf-a1868c0127c2");
        System.out.println(prueba.getId() + " " + prueba.getPath() + " "+ prueba.getType());
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
        }
        store.close();        
        analyzer.terminate();
    }
}
