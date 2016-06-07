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
    @Column(name = "SUBMIT_DATE")
    private String submitDate;
    @Column(name = "SAMPLE_DESCRIPTION")
    private String sampleDescription;

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

    public SampleReference(String submitDate, String sampleDescription) {
        this.submitDate = submitDate;
        this.sampleDescription = sampleDescription;
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
        if (sampleName == null) {
            throw new IllegalArgumentException("The name is null");
        }
        this.sampleName = sampleName;
    }

    public void setStorerID(String storerID) {
        if (storerID == null) {
            throw new IllegalArgumentException("The id is null");
        }
        this.storerID = storerID;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        if (submitDate == null) {
            throw new IllegalArgumentException("The date is null");
        }
        this.submitDate = submitDate;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        if (sampleDescription == null) {
            throw new IllegalArgumentException("The description is null");
        }
        this.sampleDescription = sampleDescription;
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
