package Core;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Sample_References")
public class SampleReference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NUM_SAMPLE")
    private int numSample;
    @ManyToOne
    @JoinColumn(name = "USERID")
    private User user;
    @Column(name = "SAMPLE_PATH")
    private String samplePath;
    @Column(name = "STORER_ID")
    private String storerID;
    @Column(name = "SAMPLE_NAME")
    private String sampleName;

    public SampleReference() {
        this.numSample = -1;
    }

    public SampleReference(int numSample, User user, String samplePath, String storerID, String sampleName) {
        this.numSample = numSample;
        this.user = user;
        this.samplePath = samplePath;
        this.storerID = storerID;
        this.sampleName = sampleName;
    }

    public String getSamplePath() {
        return samplePath;
    }

    public void setSamplePath(String samplePath) {
        if (samplePath == null) {
            throw new IllegalArgumentException("The path is null");
        }
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
        if (user == null) {
            throw new IllegalArgumentException("The user is null");
        }
        this.user = user;
    }

    public String getStorerID() {
        return storerID;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public void setStorerID(String storerID) {
        this.storerID = storerID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.numSample);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SampleReference other = (SampleReference) obj;
        if (!Objects.equals(this.numSample, other.numSample)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Sample{" + "id=" + this.getNumSample() + ", name=" + this.sampleName + "}";
    }

}
