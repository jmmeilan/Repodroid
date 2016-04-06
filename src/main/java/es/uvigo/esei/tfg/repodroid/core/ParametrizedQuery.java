package es.uvigo.esei.tfg.repodroid.core;

import java.util.List;
import java.util.Map;

public class ParametrizedQuery implements SampleQuery{
    
    public final static String NAME = "Parametrized query";
    public final static String DESCRIPTION = "Query to search samples following parameters introduced by an user";
    
    private Map<String, List<String>> parameters;
    
    public ParametrizedQuery(){}
    
    public ParametrizedQuery(Map<String, List<String>> p){
        this.parameters = p;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public String getSampleQueryName() {
       return ParametrizedQuery.NAME;
    }

    @Override
    public String getSampleQueryDescription() {
       return ParametrizedQuery.DESCRIPTION;
    }
    
}
