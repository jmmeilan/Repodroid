package es.uvigo.esei.tfg.repodroid.store;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import java.nio.file.Path;

public interface Storer {
    public void initialize(String basePath);
    public void close();
    public void storeSample(Sample sample);
    public Sample retrieveSample(long sampleID);
    public void removeSample(long sampleID);
    public void updateSample(long sampleID, Sample sample);      
}
