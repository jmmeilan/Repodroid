package es.uvigo.esei.tfg.repodroid.core;

public class NumberValueData implements ValueData {
    private float value;

    public NumberValueData() {
    }

    public NumberValueData(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    
    
    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean isMultivaluatedData() {
        return false;
    }

    @Override
    public String asString() {
         return Float.toString(this.value);
    }

    @Override
    public float asNumber() throws WrongValueDataException {
        return this.value;

    }

    @Override
    public MultivaluatedData asMultivaluatedData() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a Number value as a ValueData");
    }
 
    @Override
    public boolean isAnalysisView() {
        return false;
    }

    @Override
    public AnalisysView asAnalisysView() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a Number value as an AnalysisView");
    }    
    
}
