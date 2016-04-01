package es.uvigo.esei.tfg.repodroid.core;

public class SimilarityQuery implements SampleQuery{
    
    public final static String NAME = "Similarity query";
    public final static String DESCRIPTION = "Query to search samples that are similar by a percentage to a given sample";

    
    private Sample testSample;
    private float similarityPercentage;
    
    public SimilarityQuery(){}
    
    public SimilarityQuery(Sample s, float sP){
        this.testSample = s;
        this.similarityPercentage = sP;
    }

    public Sample getTestSample() {
        return testSample;
    }

    public void setTestSample(Sample testSample) {
        this.testSample = testSample;
    }

    public float getSimilarityPercentage() {
        return similarityPercentage;
    }

    public void setSimilarityPercentage(float similarityPercentage) {
        this.similarityPercentage = similarityPercentage;
    }

    @Override
    public String getSampleQueryName() {
       return SimilarityQuery.NAME;
    }

    @Override
    public String getSampleQueryDescription() {
       return SimilarityQuery.DESCRIPTION;
    }
    

    
}
