package es.uvigo.esei.tfg.repodroid.core.model;

public class AnalysisInfo {
    
    public String name;
    public String description;
    public String type;
    public String analyzerClassName;

    public AnalysisInfo() {
    }

    public AnalysisInfo(String name, String description, String type, String analyzerClassName) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.analyzerClassName = analyzerClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnalyzerClassName() {
        return analyzerClassName;
    }

    public void setAnalyzerClassName(String analyzerClassName) {
        this.analyzerClassName = analyzerClassName;
    }
    
    
}
