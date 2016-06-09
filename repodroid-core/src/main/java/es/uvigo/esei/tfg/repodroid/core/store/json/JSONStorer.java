package es.uvigo.esei.tfg.repodroid.core.store.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.store.Storer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONStorer implements Storer {

    ObjectMapper mapper;
    String storerDirectory;
    private Logger logger;

    @Override
    public void initialize(String basePath, Logger l) {
        if (basePath == null) {
            throw new IllegalArgumentException("The base path is null");
        }
        this.logger = l;
        this.logger.log(Level.INFO, "Initializing json storer...");
        this.mapper = new ObjectMapper();
        this.storerDirectory = basePath;
        File storerDir = new File(basePath);
        storerDir.mkdirs();
    }

    @Override
    public void close() {
        this.logger.log(Level.INFO, "Terminating json storer...");
    }

    @Override
    public void storeSample(Sample sample) {
        if (sample == null) {
            throw new IllegalArgumentException("The sample is null");
        }
        this.logger.log(Level.INFO, "Storing a sample as a json file...");
        try {
            File jsonSample = new File(this.storerDirectory + File.separator + sample.getId() + ".json");
            mapper.writeValue(jsonSample, sample);
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Sample retrieveSample(String sampleID) {
        if (sampleID == null) {
            throw new IllegalArgumentException("The ID is null");
        }
        this.logger.log(Level.INFO, "Retrieving a sample from a json file...");
        Sample sample = new Sample();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            sample = this.mapper.readValue(new File(this.storerDirectory + File.separator + sampleID + ".json"), Sample.class);
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, null, ex);
        }
        return sample;
    }

    @Override
    public void removeSample(String sampleID) {
        if (sampleID == null) {
            throw new IllegalArgumentException("The ID is null");
        }
        this.logger.log(Level.INFO, "Deleting a json file...");
        try {
            Files.delete(Paths.get(this.storerDirectory + File.separator + sampleID + ".json"));
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", this.storerDirectory + File.separator + sampleID + ".json");
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }

    @Override
    public void updateSample(String sampleID, Sample sample) {
        if (sampleID == null || sample == null) {
            throw new IllegalArgumentException("The parameters can't be null");
        }
        this.logger.log(Level.INFO, "Updating a json file...");
        this.removeSample(sampleID);
        this.storeSample(sample);
    }

}
