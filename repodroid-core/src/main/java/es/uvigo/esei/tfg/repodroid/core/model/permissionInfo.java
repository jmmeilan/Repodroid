package es.uvigo.esei.tfg.repodroid.core.model;

public class permissionInfo {
   private String permission;
   private String severity;
   
   public permissionInfo(){
   }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSeverity() {
        return severity;
    }

    public permissionInfo(String permission, String severity) {
        this.permission = permission;
        this.severity = severity;
    }

    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
   
   public String toString(){
       return this.getPermission()+":"+this.getSeverity();
   }
}
