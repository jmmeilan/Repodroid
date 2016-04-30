package Core;

import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.CuckooAnalyzer;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.store.SampleStore;
import java.util.List;

public class RepodroidAnalyzer implements Runnable{

    private final SampleStore store;
    private final CuckooAnalyzer analyzer;
    private final Sample sampleToAnalyze;
    
    
    //DEBERIA INICIALIZAR LAS COSAS PRA EL ANALISIS AQUI EN LUGAR DE RECIBIRLAS¿?¿ FRAN
    public RepodroidAnalyzer (SampleStore s, 
                              CuckooAnalyzer c,
                              Sample sample){
        this.analyzer = c;
        this.store = s;
        this.sampleToAnalyze = sample;
    }
    
    @Override
    public void run() {
        List<Analysis> analyses = analyzer.analyzeSample(this.sampleToAnalyze);
        for (Analysis analysis : analyses) {
            this.sampleToAnalyze.addAnalysis(analysis.getAnalysisName(), analysis);
        }
        store.storeSample(this.sampleToAnalyze);
        this.store.close();
        this.analyzer.terminate();
        //AGREGAR COMUNICACION POR CORREO ELECTRONICO
    }
    
}
