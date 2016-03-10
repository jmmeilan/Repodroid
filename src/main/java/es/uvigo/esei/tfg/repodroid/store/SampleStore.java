package es.uvigo.esei.tfg.repodroid.store;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SampleStore {
    private String basePath;
    private Storer storer;
    private Indexer indexer;

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

    
    
    public void initialize() {
        System.out.println("Initializing sample store...");
        this.storer.initialize(this.basePath + File.separator + "STORE");
        this.indexer.initialize(this.basePath + File.separator + "INDEX");
    }

    public void close() {
        System.out.println("Terminating sample store...");
        this.storer.close();
        this.indexer.close();
    }

    //The sample comes already with an ID if cuckoo is used
    public void storeSample(Sample sample) {
        sample.setId(computeNextSampleID());
        System.out.println("EL ID ES: "+sample.getId()); //ELIMINAR
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
                result.add(this.storer.retrieveSample(sampleID));
            }
            return result;
       }
       return Collections.emptyList();
    }
    
    private String computeNextSampleID() {
        // TODO Podria utilizarsela siguiente linea devolviendo un String
        return UUID.randomUUID().toString();
    }
}
