package es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo;

import es.uvigo.esei.tfg.repodroid.core.model.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.model.Analysis;
import es.uvigo.esei.tfg.repodroid.core.model.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.MultivaluatedData;
import es.uvigo.esei.tfg.repodroid.core.model.StringValueData;
import es.uvigo.esei.tfg.repodroid.core.model.VisualizableAnalysis;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApkClassesAnalysis implements Analysis, 
                                        IndexableAnalysis, 
                                        VisualizableAnalysis{
    
    public final static String NAME = "Apk classes analysis";
    public final static String DESCRIPTION = "Classes of the apk";
    public final static String TYPE = "ApkClassesAnalysis";
    
    private Set<String> classes;
    
    public ApkClassesAnalysis(){
        this.classes = new HashSet();
    }
    
    public ApkClassesAnalysis(Set<String> classes) {
        this.classes = classes;
    }
    
    public Set<String> getClasses() {
        return classes;
    }

    public void setClasses(Set<String> classes) {
        this.classes = classes;
    }
    
    public void addClass(String c) {
        if (this.classes == null) {
            this.classes = new HashSet<>();
        }
        this.classes.add(c);
    }
    
    @Override
    public String getAnalysisName() {
        return ApkClassesAnalysis.NAME;
    }

    @Override
    public String getAnalysisDescription() {
        return ApkClassesAnalysis.DESCRIPTION;
    }
    
    @Override
    public String getAnalysisType() {
        return ApkClassesAnalysis.TYPE;
    }
    
    @Override
    public List<String> getIndexableItems() {
        List<String> result = new ArrayList<>();
        if (this.classes != null) {
            result.addAll(this.classes);
        }
        return result;
    }
    
    @Override
    public AnalisysView getAnalisisView() {
        AnalisysView result = new AnalisysView(this.getAnalysisName());
        
        MultivaluatedData classesValues = new MultivaluatedData();
        for (String c : this.classes) {
            classesValues.addValue(new StringValueData(c));
        }   
        result.addValue("Name of the classes", classesValues);        
        return result;
    }
    
}
