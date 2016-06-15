package es.uvigo.esei.tfg.repodroid.core.store;

import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.SampleQuery;
import java.util.List;
import java.util.logging.Logger;

public interface Indexer {
    public void initialize(String basePath, Logger logger);
    public void close();
    public void indexSample(Sample sample);
    public void removeSample(String sampleID);
    public void updateSample(String sampleID, Sample sample);      
    public List<String> search(SampleQuery query, int firstResult, int numberOfSamples);
    
    public List<TermInfo> retrieveTermInfoForIndexableAnalysis(String indexableAnalysisName, int maxTerms);
}
