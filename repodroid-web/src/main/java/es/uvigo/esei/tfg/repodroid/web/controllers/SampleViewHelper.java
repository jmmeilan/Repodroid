package es.uvigo.esei.tfg.repodroid.web.controllers;

import es.uvigo.esei.tfg.repodroid.web.entities.SampleRepresentation;
import es.uvigo.esei.tfg.repodroid.web.entities.WrongAnalysisViewException;
import es.uvigo.esei.tfg.repodroid.core.model.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.model.Analysis;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.ValueData;
import es.uvigo.esei.tfg.repodroid.core.model.VisualizableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.WrongValueDataException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SampleViewHelper {

    private Sample sample;

    public SampleViewHelper(Sample s) {
        this.sample = s;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        if (sample == null) {
            throw new IllegalArgumentException("The sample is null");
        }
        this.sample = sample;
    }

    public void extractAntiVirusAnalysis(AnalisysView aV,
            SampleRepresentation representation)
            throws WrongValueDataException,
            WrongAnalysisViewException {

        if (aV == null || representation == null) {
            throw new IllegalArgumentException("EXCEPTION: Parameters must not be null");
        } else {
            if (aV.getValues().get("Antiviruses") == null) {
                throw new WrongAnalysisViewException("EXCEPTION: Wrong ttype of Analysis view");
            }
            Map<String, ValueData> data = aV.getValues();
            Set<ValueData> s = data.get("Antiviruses").asMultivaluatedData().getValues();
            if (s != null) {
                for (ValueData v : s) {
                    if (v.isString()) {
                        List<String> lista = representation.getAntiViruses();
                        lista.add(v.asString());
                        representation.setAntiViruses(lista);
                    }
                }
            }
            representation.setScanDate(data.get("Scan date").asString());
            representation.setNumberAntiviruses(data.get("Number of antiviruses").asString());
            representation.setPositives(data.get("Positives").asString());
        }
    }

    public void extractClassesAnalysis(AnalisysView aV,
            SampleRepresentation representation)
            throws WrongValueDataException,
            WrongAnalysisViewException {

        if (aV == null || representation == null) {
            throw new IllegalArgumentException("EXCEPTION: Parameters must not be null");
        } else {
            if (aV.getValues().get("Name of the classes") == null) {
                throw new WrongAnalysisViewException("EXCEPTION: Wrong type of Analysis view");
            }
            Map<String, ValueData> data = aV.getValues();
            Set<ValueData> s = data.get("Name of the classes").asMultivaluatedData().getValues();
            for (ValueData v : s) {
                if (v.isString()) {
                    List<String> lista = representation.getClasses();
                    lista.add(v.asString());
                    representation.setClasses(lista);
                }
            }
        }
    }

    public void extractPermissionsAnalysis(AnalisysView aV,
            SampleRepresentation representation)
            throws WrongValueDataException,
            WrongAnalysisViewException {

        if (aV == null || representation == null) {
            throw new IllegalArgumentException("EXCEPTION: Parameters must not be null");
        } else {
            if (aV.getValues().get("Permissions") == null) {
                throw new WrongAnalysisViewException("EXCEPTION: Wrong type of Analysis view");
            }
            Map<String, ValueData> data = aV.getValues();
            Set<ValueData> s = data.get("Permissions").asMultivaluatedData().getValues();
            for (ValueData v : s) {
                if (v.isString()) {
                    List<String> lista = representation.getPermissions();
                    lista.add(v.asString());
                    representation.setPermissions(lista);
                }
            }
        }
    }

    public void extractConnectionsAnalysis(AnalisysView aV,
            SampleRepresentation representation)
            throws WrongValueDataException,
            WrongAnalysisViewException {

        if (aV == null || representation == null) {
            throw new IllegalArgumentException("EXCEPTION: Parameters must not be null");
        } else {
            if (aV.getValues().get("external hosts") == null) {
                throw new WrongAnalysisViewException("EXCEPTION: Wrong type of Analysis view");
            }
            Map<String, ValueData> data = aV.getValues();
            Set<ValueData> s = data.get("external hosts").asMultivaluatedData().getValues();
            for (ValueData v : s) {
                if (v.isString()) {
                    List<String> lista = representation.getExternalHosts();
                    lista.add(v.asString());
                    representation.setExternalHosts(lista);
                }
            }
            s = data.get("dns queries").asMultivaluatedData().getValues();
            for (ValueData v : s) {
                if (v.isString()) {
                    List<String> lista = representation.getDnsQueries();
                    lista.add(v.asString());
                    representation.setDnsQueries(lista);
                }
            }
        }
    }

    public SampleRepresentation extractRepresentation() {
        SampleRepresentation representation = new SampleRepresentation();
        Map<String, Analysis> map = this.sample.getAnalises();
        for (Analysis value : map.values()) {
            if (value instanceof VisualizableAnalysis) {
                AnalisysView aV = ((VisualizableAnalysis) value).getAnalisisView();
                try {
                    if (aV.getName().equals("antivirus analysis")) {
                        extractAntiVirusAnalysis(aV, representation);
                    }
                    if (aV.getName().equals("Apk classes analysis")) {
                        extractClassesAnalysis(aV, representation);
                    }
                    if (aV.getName().equals("Apk permissions analysis")) {
                        extractPermissionsAnalysis(aV, representation);
                    }
                    if (aV.getName().equals("output connections")) {
                        extractConnectionsAnalysis(aV, representation);
                    }
                } catch (WrongValueDataException | WrongAnalysisViewException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
        if (representation.getSeverities().isEmpty()) {
            representation.separatePermissions();
        }
        return representation;
    }
}
