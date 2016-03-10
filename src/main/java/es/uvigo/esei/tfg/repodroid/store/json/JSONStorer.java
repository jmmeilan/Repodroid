
package es.uvigo.esei.tfg.repodroid.store.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.store.Storer;
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
            //CORRIGE QUE FALLE PQ NO ENCUENTRA ANALYSISNAME. VER MEJOR FORMA
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            sample = this.mapper.readValue(new File(this.storerDirectory+File.separator+sampleID+".json"), Sample.class);
        } catch (IOException ex) {
            Logger.getLogger(JSONStorer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sample;
    }

    @Override
    public void removeSample(String sampleID){
       System.out.println("Deleting a json file...");
       try {
            Files.delete(Paths.get(this.storerDirectory+File.separator+sampleID+".json"));
       }catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", this.storerDirectory+File.separator+sampleID+".json");
       }catch (IOException x) {
            // File permission problems are caught here.
        System.err.println(x);
}
    }

    //ELIMINAMOS Y VOLVEMOS A CREAR EL JSON¿?
    @Override
    public void updateSample(String sampleID, Sample sample) {
        System.out.println("Updating a json file...");
        this.removeSample(sampleID);
        this.storeSample(sample);
    }
    
}
