package es.uvigo.esei.tfg.repodroid.core;

public interface ValueData {

    public boolean isString();

    public boolean isNumber();

    public boolean isMultivaluatedData();

    public boolean isAnalysisView();

    public String asString() throws WrongValueDataException;

    public float asNumber() throws WrongValueDataException;

    public MultivaluatedData asMultivaluatedData() throws WrongValueDataException;

    public AnalisysView asAnalisysView() throws WrongValueDataException;
}
