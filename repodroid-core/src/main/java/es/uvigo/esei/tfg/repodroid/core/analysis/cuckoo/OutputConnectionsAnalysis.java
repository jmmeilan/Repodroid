package es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo;

import es.uvigo.esei.tfg.repodroid.core.model.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.model.Analysis;
import es.uvigo.esei.tfg.repodroid.core.model.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.MultivaluatedData;
import es.uvigo.esei.tfg.repodroid.core.model.StringValueData;
import es.uvigo.esei.tfg.repodroid.core.model.VisualizableAnalysis;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OutputConnectionsAnalysis implements Analysis, 
                                                  IndexableAnalysis, 
                                                  VisualizableAnalysis {
    
    public final static String NAME = "output connections";
    public final static String DESCRIPTION = "DNS queries and external hosts reached by the sample during the analysis";
    public final static String TYPE = "OutputConnectionsAnalysis";
          
    
    private Set<String> externalHosts;
    private Set<String> dnsQueries;

    public OutputConnectionsAnalysis() {
    }

    public OutputConnectionsAnalysis(Set<String> externalHosts, Set<String> dnsQueries) {
        this.externalHosts = externalHosts;
        this.dnsQueries = dnsQueries;
    }

    public Set<String> getExternalHosts() {
        return externalHosts;
    }

    public void setExternalHosts(Set<String> externalHosts) {
        this.externalHosts = externalHosts;
    }

    public Set<String> getDnsQueries() {
        return dnsQueries;
    }

    public void setDnsQueries(Set<String> dnsQueries) {
        this.dnsQueries = dnsQueries;
    }

    public void addExternalHost(String externalHost) {
        if (this.externalHosts == null) {
            this.externalHosts = new HashSet<>();
        }
        this.externalHosts.add(externalHost);
    }

    public void addDNSQuery(String dnsQuery) {
        if (this.dnsQueries == null) {
            this.dnsQueries = new HashSet<>();
        }
        this.dnsQueries.add(dnsQuery);
    }

    @Override
    public List<String> getIndexableItems() {
        List<String> result = new ArrayList<>();
        if (this.externalHosts != null) {
            result.addAll(this.externalHosts);
        }
        if (this.dnsQueries != null) {
            result.addAll(this.dnsQueries);
        }
        return result;
    }

    @Override
    public String getAnalysisName() {
        return OutputConnectionsAnalysis.NAME;
    }

    @Override
    public String getAnalysisDescription() {
        return OutputConnectionsAnalysis.DESCRIPTION;
    }
    
    @Override
    public String getAnalysisType() {
        return OutputConnectionsAnalysis.TYPE;
    }

    @Override
    public AnalisysView getAnalisisView() {
        AnalisysView result = new AnalisysView(this.getAnalysisName());
        
        MultivaluatedData hostsValues = new MultivaluatedData();
        for (String host : this.externalHosts) {
            hostsValues.addValue(new StringValueData(host));
        }   
        result.addValue("external hosts", hostsValues);

        MultivaluatedData dnsQueriesValues = new MultivaluatedData();
        for (String dnsQuery : this.dnsQueries) {
            dnsQueriesValues.addValue(new StringValueData(dnsQuery));       }   
        result.addValue("dns queries", dnsQueriesValues);
        
        return result;
    }
}
