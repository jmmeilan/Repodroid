package es.uvigo.esei.tfg.repodroid.store;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import java.util.List;

public interface Indexer {
    public void initialize(String basePath);
    public void close();
    public void indexSample(Sample sample);
    public void removeSample(long sampleID);
    public void updateSample(long sampleID, Sample sample);      
    public List<Long> search(SampleQuery query, int firstResult, int numberOfSamples);
}
