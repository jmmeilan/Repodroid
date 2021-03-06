package es.uvigo.esei.tfg.repodroid.core.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.AntiVirusAnalysis;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.ApkClassesAnalysis;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.ApkPermissionsAnalysis;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.OutputConnectionsAnalysis;

//Necesario para que json sepa como convertir un fichero json a sample
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
