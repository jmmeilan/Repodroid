
package es.uvigo.esei.tfg.repodroid.core.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnalisysView implements ValueData {
    private String name;
    private Map<String, ValueData> values;

    public AnalisysView() {
    }

    public AnalisysView(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ValueData> getValues() {
        return values;
    }

    public void setValues(Map<String, ValueData> values) {
        this.values = values;
    }

    public void addValue(String name , ValueData value) {
        if (this.values == null) {
            this.values = new HashMap<>();
        }
        this.values.put(name, value);
    }

    public void removeValue(String name) {
        if (this.values != null) {
            this.values.remove(name);
        }
    }
    
    public boolean hasValueName(String name) {
        if (this.values != null) {
            return this.values.containsKey(name);
        }
        return false;
    }
    
    public Set<String> getValueNames() {
        if (this.values != null) {
            return this.values.keySet();
        }
        else {
            return Collections.emptySet();
        }
    }    

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isMultivaluatedData() {
        return false;                
    }

    @Override
    public boolean isAnalysisView() {
        return true;
    }

    @Override
    public String asString() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing an AnalysisView value as a String");
    }

    @Override
    public float asNumber() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing an AnalysisView value as a Number");
    }

    @Override
    public MultivaluatedData asMultivaluatedData() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing an AnalysisView value as a MultivaluatedData");
    }

    @Override
    public AnalisysView asAnalisysView() throws WrongValueDataException {
        return this;
    }
}
