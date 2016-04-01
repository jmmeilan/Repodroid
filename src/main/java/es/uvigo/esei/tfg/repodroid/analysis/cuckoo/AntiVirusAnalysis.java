package es.uvigo.esei.tfg.repodroid.analysis.cuckoo;

import es.uvigo.esei.tfg.repodroid.core.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.MultivaluatedData;
import es.uvigo.esei.tfg.repodroid.core.StringValueData;
import es.uvigo.esei.tfg.repodroid.core.VisualizableAnalysis;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntiVirusAnalysis implements Analysis, 
                                          IndexableAnalysis, 
                                           VisualizableAnalysis{
    
    public final static String NAME = "antivirus analysis";
    public final static String DESCRIPTION = "Analysis by virustotal";
    public final static String TYPE = "AntiVirusAnalysis";
    
    private String scanDate;
    private int positives;
    private int total;
    
    //Set that contains the antivirus' that detected the sample as a virus
    private Set<String> antiVirusList;
    
    public AntiVirusAnalysis(){
        this.scanDate = "";
        this.positives = 0;
        this.total = 0;
        this.antiVirusList = new HashSet();
    }
    
    public AntiVirusAnalysis (String scanDate, 
                              int positives, 
                              int total, 
                              Set set){
        
        this.scanDate = scanDate;
        this.positives = positives;
        this.total = total;
        this.antiVirusList = set;
    }
    
    public String getScanDate(){
        return this.scanDate;
    }
    
    public int getPositives(){
        return this.positives;
    }
    
    public int getTotal(){
        return this.total;
    }
    
    public Set<String> getAntiVirusList(){
        return this.antiVirusList;
    }
    
    public void setScanDate(String sd){
        this.scanDate = sd;
    }
    
    public void setPositives(int p){
        this.positives = p;
    }
    
    public void setTotal(int t){
        this.total = t;
    }
    
    public void setAntiVirusList(Set m){
        this.antiVirusList = m;
    }
    
    public void addAntiVirus(String av) {
        if (this.antiVirusList == null) {
            this.antiVirusList = new HashSet();
        }
        this.antiVirusList.add(av);
    }
    
     @Override
    public List<String> getIndexableItems() {
        List<String> result = new ArrayList<>();
        if(this.antiVirusList != null){
            result.addAll(this.antiVirusList);
        }
        return result;
    }

    @Override
    public String getAnalysisName() {
        return AntiVirusAnalysis.NAME;
    }

    @Override
    public String getAnalysisDescription() {
        return AntiVirusAnalysis.DESCRIPTION;
    }
    
    @Override
    public String getAnalysisType() {
        return AntiVirusAnalysis.TYPE;
    }

    @Override
    public AnalisysView getAnalisisView() {
        AnalisysView result = new AnalisysView(this.getAnalysisName());
        MultivaluatedData antiVirusValues = new MultivaluatedData();
        for (String antivirus : this.antiVirusList) {
            antiVirusValues.addValue(new StringValueData(antivirus));
        }   
        result.addValue("Antiviruses", antiVirusValues);  
        result.addValue("Scan date", new StringValueData(this.scanDate));
        result.addValue("Number of antiviruses", new StringValueData(String.valueOf(this.total)));
        result.addValue("Positives", new StringValueData(String.valueOf(this.positives)));
        return result;
    }
}
