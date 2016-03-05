package es.uvigo.esei.tfg.repodroid.analysis;

import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import java.util.List;

public interface Analyzer {
    public void initialize();
    public void terminate();
    public List<String> getAnalysesNames();
    public List<Analysis> analyzeSample(Sample sample);    
}
