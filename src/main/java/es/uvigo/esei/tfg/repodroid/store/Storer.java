package es.uvigo.esei.tfg.repodroid.store;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import java.nio.file.Path;
import java.util.logging.Logger;

public interface Storer {
    public void initialize(String basePath, Logger l);
    public void close();
    public void storeSample(Sample sample);
    public Sample retrieveSample(String sampleID);
    public void removeSample(String sampleID);
    public void updateSample(String sampleID, Sample sample);      
}
