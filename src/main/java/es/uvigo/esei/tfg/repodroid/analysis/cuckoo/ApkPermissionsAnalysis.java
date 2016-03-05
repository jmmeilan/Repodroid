package es.uvigo.esei.tfg.repodroid.analysis.cuckoo;

import es.uvigo.esei.tfg.repodroid.core.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.MultivaluatedData;
import es.uvigo.esei.tfg.repodroid.core.StringValueData;
import es.uvigo.esei.tfg.repodroid.core.VisualizableAnalysis;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApkPermissionsAnalysis implements Analysis, 
                                        IndexableAnalysis, 
                                        VisualizableAnalysis{
    
    public final static String NAME = "Apk permissions analysis";
    public final static String DESCRIPTION = "Permissions request by the apk in the manifest";
    public final static String TYPE = "ApkPermissionsAnalysis";
    
    private Set<String> permissions;
    
    public ApkPermissionsAnalysis () {
        this.permissions = new HashSet();
    }
    
    public ApkPermissionsAnalysis (Set<String> permissions) {
        this.permissions = permissions;
    }
    
    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
    
    public void addPermission(String permission) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(permission);
    }
    
    @Override
    public String getAnalysisName() {
        return ApkPermissionsAnalysis.NAME;
    }

    @Override
    public String getAnalysisDescription() {
        return ApkPermissionsAnalysis.DESCRIPTION;
    }
    
    @Override
    public String getAnalysisType() {
        return ApkPermissionsAnalysis.TYPE;
    }
    
        @Override
    public List<String> getIndexableItems() {
        List<String> result = new ArrayList<>();
        if (this.permissions != null) {
            result.addAll(this.permissions);
        }
        return result;
    }
    
    @Override
    public AnalisysView getAnalisisView() {
        AnalisysView result = new AnalisysView(this.getAnalysisName());
        MultivaluatedData permissionsValues = new MultivaluatedData();
        for (String permission : this.permissions) {
            permissionsValues.addValue(new StringValueData(permission));       }   
        result.addValue("Permissions", permissionsValues);
        
        return result;
    }
}
