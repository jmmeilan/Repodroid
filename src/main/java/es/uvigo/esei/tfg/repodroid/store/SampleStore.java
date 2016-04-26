package es.uvigo.esei.tfg.repodroid.store;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SampleStore {
    private String basePath;
    private Storer storer;
    private Indexer indexer;
    private Logger logger;

    public SampleStore(String basePath) {
        this.basePath = basePath;
    }

    public SampleStore(String basePath, Storer storer, Indexer indexer) {
        this.basePath = basePath;
        this.storer = storer;
        this.indexer = indexer;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }

    
    public void setStorer(Storer storer) {
        this.storer = storer;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }

    
    
    public void initialize(Logger l) {
        this.logger = l;
        this.logger.log(Level.INFO, "Initializing sample store..."); 
        this.storer.initialize(this.basePath + File.separator + "STORE" , l);
        this.indexer.initialize(this.basePath + File.separator + "INDEX", l);
    }

    public void close() {
        this.logger.log(Level.INFO, "Terminating sample store..."); 
        this.storer.close();
        this.indexer.close();
    }

    //SAMPLE SHOULD COME WITH AN ID
    public void storeSample(Sample sample) {
        this.storer.storeSample(sample);
        this.indexer.indexSample(sample);

    }

    public Sample retrieveSample(String sampleID) {
        return this.storer.retrieveSample(sampleID);
    }

    public void removeSample(String sampleID) {
        this.storer.removeSample(sampleID);
        this.indexer.removeSample(sampleID);
    }

    public void updateSample(String sampleID, Sample sample) {
        sample.setId(sampleID);
        this.storer.updateSample(sampleID, sample);
        this.indexer.updateSample(sampleID, sample);
    }

    public List<Sample> search(SampleQuery query, int firstResult, int numberOfSamples) {
        List<String> sampleIDs = this.indexer.search(query, firstResult, numberOfSamples);
        if ((sampleIDs != null) && (!sampleIDs.isEmpty())) {
            List<Sample> result = new ArrayList<>();
            for (String sampleID : sampleIDs) {
                System.out.println("ID ENCONTRAOS: "+sampleID);
                result.add(this.storer.retrieveSample(sampleID));
            }
            return result;
       }
       return Collections.emptyList();
    }
    
    public String computeNextSampleID() {
        return UUID.randomUUID().toString();
    }
}
