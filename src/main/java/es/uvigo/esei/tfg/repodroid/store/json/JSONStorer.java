
package es.uvigo.esei.tfg.repodroid.store.json;

import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.store.Storer;

public class JSONStorer implements Storer {

    @Override
    public void initialize(String basePath) {
       System.out.println("Initializing json storer...");
    }

    @Override
    public void close() {
        System.out.println("Terminanting json storer...");
    }

    @Override
    public void storeSample(Sample sample) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sample retrieveSample(long sampleID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeSample(long sampleID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSample(long sampleID, Sample sample) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
