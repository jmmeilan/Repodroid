package es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo;

import es.uvigo.esei.tfg.repodroid.core.model.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.model.Analysis;
import es.uvigo.esei.tfg.repodroid.core.model.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.MultivaluatedData;
import es.uvigo.esei.tfg.repodroid.core.model.StringValueData;
import es.uvigo.esei.tfg.repodroid.core.model.VisualizableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.permissionInfo;
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
    
    //AGREGAR SEVERIDAD DE LOS PERMISOS? REQUIERE CREAR ALGUNA CLASE O USAR MAPS!!
    private Set<permissionInfo> permissions;
    
    public ApkPermissionsAnalysis () {
        this.permissions = new HashSet();
    }
    
    public ApkPermissionsAnalysis (Set<permissionInfo> permissions) {
        this.permissions = permissions;
    }
    
    public Set<permissionInfo> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<permissionInfo> permissions) {
        this.permissions = permissions;
    }
    
    public void addPermission(String permission, String severity) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        permissionInfo perm = new permissionInfo(permission, severity);
        this.permissions.add(perm);
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
            for(permissionInfo p: this.permissions){
                result.add(p.toString());
            }
        }
        return result;
    }
    
    @Override
    public AnalisysView getAnalisisView() {
        AnalisysView result = new AnalisysView(this.getAnalysisName());
        MultivaluatedData permissionsValues = new MultivaluatedData();
        for (permissionInfo permission : this.permissions) {
            permissionsValues.addValue(new StringValueData(permission.toString()));       
        }   
        result.addValue("Permissions", permissionsValues);      
        return result;
    }
}
