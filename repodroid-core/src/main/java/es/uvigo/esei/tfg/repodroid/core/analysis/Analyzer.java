package es.uvigo.esei.tfg.repodroid.core.analysis;

import es.uvigo.esei.tfg.repodroid.core.model.Analysis;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import java.util.List;
import java.util.logging.Logger;

public interface Analyzer {
    public void initialize(Logger l);
    public void terminate();
    public List<String> getAnalysesNames();
    public List<Analysis> analyzeSample(Sample sample);    
}
