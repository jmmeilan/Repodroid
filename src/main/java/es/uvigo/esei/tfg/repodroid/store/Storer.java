package es.uvigo.esei.tfg.repodroid.store;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import java.nio.file.Path;

public interface Storer {
    public void initialize(String basePath);
    public void close();
    public void storeSample(Sample sample);
    public Sample retrieveSample(String sampleID);
    public void removeSample(String sampleID);
    public void updateSample(String sampleID, Sample sample);      
}
