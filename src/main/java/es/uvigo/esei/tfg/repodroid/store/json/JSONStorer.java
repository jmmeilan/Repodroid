
package es.uvigo.esei.tfg.repodroid.store.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.store.Storer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONStorer implements Storer {

    ObjectMapper mapper;
    String storerDirectory;
    
    @Override
    public void initialize(String basePath) {
       System.out.println("Initializing json storer...");
       this.mapper = new ObjectMapper();
       this.storerDirectory = basePath;
    }

    @Override
    public void close() {
        System.out.println("Terminating json storer...");
    }

    @Override
    public void storeSample(Sample sample) {
        System.out.println("Storing a sample as a json file...");
        try {
            File jsonSample = new File(this.storerDirectory+File.separator+sample.getId()+".json");
            mapper.writeValue(jsonSample, sample);
        } catch (IOException ex) {
            Logger.getLogger(JSONStorer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Sample retrieveSample(String sampleID) {
       System.out.println("Retrieving a sample from a json file...");
       Sample sample = new Sample();
        try {
            sample = this.mapper.readValue(new File(this.storerDirectory+File.separator+sampleID+".json"), Sample.class);
        } catch (IOException ex) {
            Logger.getLogger(JSONStorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sample;
    }

    @Override
    public void removeSample(String sampleID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSample(String sampleID, Sample sample) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
