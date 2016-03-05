package es.uvigo.esei.tfg.repodroid.core;

import java.util.HashMap;
import java.util.Map;

public class Sample {
    private long id;
    private String path;
    private SampleType type;
    private Map<String, Analysis> analises;

    public Sample() {
    }

    public Sample(String path, SampleType type) {
        this.path = path;
        this.type = type;
        this.analises = new HashMap<>();        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SampleType getType() {
        return type;
    }

    public void setType(SampleType type) {
        this.type = type;
    }

    public Map<String, Analysis> getAnalises() {
        return analises;
    }

    public void setAnalises(Map<String, Analysis> analises) {
        this.analises = analises;
    }
    
    public void addAnalysis(String analysisName, Analysis analysis) {
        if (this.analises == null) {
            this.analises = new HashMap<>();
        }
        this.analises.put(analysisName, analysis);
    }
    
    public boolean hasAnalysis(String analysisName) {
        if (this.analises != null) {
            return this.analises.containsKey(analysisName);
        }
        return false;    
    }
    
    public Analysis retrieveAnalysis(String analysisName) {
        if (this.analises != null) {
            return this.analises.get(analysisName);
        }
        return null;
    }
    
    public Analysis removeAnalysis(String analysisName) {
        if (this.analises != null) {
            return this.analises.remove(analysisName);
        }
        return null;
    }
    
    
    
}
