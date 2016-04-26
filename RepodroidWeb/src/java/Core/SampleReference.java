package Core;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity @Table(name="Samples_References")
public class SampleReference implements Serializable{
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "NUM_SAMPLE")
    private int numSample;
    @ManyToOne @JoinColumn(name="USERID")
    private User user;
    @Column(name="SAMPLE_PATH")
    private String samplePath;
    @Column(name="STORER_ID")
    private String storerID;
    
    public SampleReference() {
    }


    public String getSamplePath() {
        return samplePath;
    }

    public void setSamplePath(String samplePath) {
        this.samplePath = samplePath;
    }

    public int getNumSample() {
        return numSample;
    }

    public void setNumSample(int numSample) {
        this.numSample = numSample;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStorerID() {
        return storerID;
    }

    public void setStorerID(String storerID) {
        this.storerID = storerID;
    }
    
    
}
