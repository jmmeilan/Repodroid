package es.uvigo.esei.tfg.repodroid.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.AntiVirusAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.ApkClassesAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.ApkPermissionsAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.OutputConnectionsAnalysis;

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type")  
@JsonSubTypes({  
    @Type(value = AntiVirusAnalysis.class, name = AntiVirusAnalysis.NAME),
    @Type(value = ApkClassesAnalysis.class, name = ApkClassesAnalysis.NAME),
    @Type(value = ApkPermissionsAnalysis.class, name = ApkPermissionsAnalysis.NAME),
    @Type(value = OutputConnectionsAnalysis.class, name = OutputConnectionsAnalysis.NAME) })  
public interface Analysis {
    public String getAnalysisName();
    public String getAnalysisDescription();
    public String getAnalysisType();
}
