package es.uvigo.esei.tfg.repodroid.core;

public class StringValueData implements ValueData {
    private String value;

    public StringValueData() {
    }

    public StringValueData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
    
    @Override
    public boolean isString() {
        return true;
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
    public String asString() {
        return this.value;
    }

    @Override
    public float asNumber() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a String value as a Number");
    }

    @Override
    public MultivaluatedData asMultivaluatedData() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a String value as a MultivaluatedData");
    }

    @Override
    public boolean isAnalysisView() {
        return false;
    }

    @Override
    public AnalisysView asAnalisysView() throws WrongValueDataException {
        throw new WrongValueDataException("Acessing a String value as an AnalysisView");
    }
    
    
}
