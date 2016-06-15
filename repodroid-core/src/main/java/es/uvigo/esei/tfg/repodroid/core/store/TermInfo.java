package es.uvigo.esei.tfg.repodroid.core.store;

public class TermInfo {
    public String term;
    public int ocurrences;

    public TermInfo() {
    }

    public TermInfo(String term, int ocurrences) {
        this.term = term;
        this.ocurrences = ocurrences;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getOcurrences() {
        return ocurrences;
    }

    public void setOcurrences(int ocurrences) {
        this.ocurrences = ocurrences;
    }
    
    
    
}
