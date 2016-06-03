/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmmeilan
 */
public class SampleRepresentation {

    private String scanDate;
    private String numberAntiviruses;
    private String positives;
    private List<String> antiViruses;
    private List<String> classes;
    private List<String> permissions;
    private List<String> severities;
    private List<String> externalHosts;
    private List<String> dnsQueries;

    public SampleRepresentation() {
        this.antiViruses = new ArrayList();
        this.classes = new ArrayList();
        this.permissions = new ArrayList();
        this.severities = new ArrayList();
        this.externalHosts = new ArrayList();
        this.dnsQueries = new ArrayList();
    }

    public String getScanDate() {
        return scanDate;
    }

    public void setScanDate(String scanDate) {
        if (scanDate == null) {
            throw new IllegalArgumentException("The scanDate is null");
        }
        this.scanDate = scanDate;
    }

    public String getNumberAntiviruses() {
        return numberAntiviruses;
    }

    public void setNumberAntiviruses(String numberAntiviruses) {
        if (numberAntiviruses == null) {
            throw new IllegalArgumentException("The number of antiviruses is null");
        }
        this.numberAntiviruses = numberAntiviruses;
    }

    public String getPositives() {
        return positives;
    }

    public void setPositives(String positives) {
        if (positives == null) {
            throw new IllegalArgumentException("The number of positives is null");
        }
        this.positives = positives;
    }

    public List<String> getAntiViruses() {
        return antiViruses;
    }

    public void setAntiViruses(List<String> antiViruses) {
        if (antiViruses == null) {
            throw new IllegalArgumentException("The list of antiviruses is null");
        }
        this.antiViruses = antiViruses;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        if (classes == null) {
            throw new IllegalArgumentException("The list of classes is null");
        }
        this.classes = classes;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        if (permissions == null) {
            throw new IllegalArgumentException("The list of permissions is null");
        }
        this.permissions = permissions;
    }

    public List<String> getExternalHosts() {
        return externalHosts;
    }

    public void setExternalHosts(List<String> externalHosts) {
        if (externalHosts == null) {
            throw new IllegalArgumentException("The list of external hosts is null");
        }
        this.externalHosts = externalHosts;
    }

    public List<String> getDnsQueries() {
        return dnsQueries;
    }

    public void setDnsQueries(List<String> dnsQueries) {
        if (dnsQueries == null) {
            throw new IllegalArgumentException("The list of dns queries is null");
        }
        this.dnsQueries = dnsQueries;
    }

    public List<String> getSeverities() {
        return severities;
    }

    public void setSeverities(List<String> severities) {
        if (severities == null) {
            throw new IllegalArgumentException("The list of severities is null");
        }
        this.severities = severities;
    }

    public void separatePermissions() {
        List<String> newPermissions = new ArrayList();
        for (String p : this.permissions) {
            if (p.contains(":")) {
                String[] parts = p.split(":");
                newPermissions.add(parts[0]);
                this.severities.add(parts[1]);
            } else {
                newPermissions.add(p);
            }
        }
        this.permissions = newPermissions;
    }

}
