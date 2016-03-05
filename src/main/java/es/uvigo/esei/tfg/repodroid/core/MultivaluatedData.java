
package es.uvigo.esei.tfg.repodroid.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MultivaluatedData implements ValueData {
    private Set<ValueData> values;

    public MultivaluatedData() {
    }

    public MultivaluatedData(Set<ValueData> values) {
        this.values = values;
    }

    public Set<ValueData> getValues() {
        return values;
    }

    public void setValues(Set<ValueData> values) {
        this.values = values;
    }

    public void addValue(ValueData value) {
        if (this.values == null) {
            this.values = new HashSet<>();
        }
        this.values.add(value);
    }

    public void removeValue(ValueData value) {
        if (this.values != null) {
            this.values.remove(value);
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
        return true;
    }

    @Override
    public String asString() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a MultivaluatedData value as a String");
    }

    @Override
    public float asNumber() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a MultivaluatedData value as a Number");
    }

    @Override
    public MultivaluatedData asMultivaluatedData() throws WrongValueDataException {
        return this;
    }

    @Override
    public boolean isAnalysisView() {
        return false;
    }

    @Override
    public AnalisysView asAnalisysView() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a MultivaluatedData value as an AnalysisView");
    }
    
}
