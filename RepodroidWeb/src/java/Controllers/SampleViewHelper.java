package Controllers;

import Core.SampleRepresentation;
import es.uvigo.esei.tfg.repodroid.core.AnalisysView;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.ValueData;
import es.uvigo.esei.tfg.repodroid.core.VisualizableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.WrongValueDataException;
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
        this.sample = sample;
    }

    public SampleRepresentation extractRepresentation() throws WrongValueDataException{
        SampleRepresentation representation = new SampleRepresentation();
        Map<String, Analysis> map = this.sample.getAnalises();
        for (Analysis value : map.values()) {
            if (value instanceof VisualizableAnalysis) {
                AnalisysView aV = ((VisualizableAnalysis) value).getAnalisisView();
                if (aV.getName().equals("antivirus analysis")) {
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
                if (aV.getName().equals("Apk classes analysis")) {
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
                if (aV.getName().equals("Apk permissions analysis")) {
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
                if (aV.getName().equals("output connections")) {
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
        }
        if (representation.getSeverities().isEmpty()) {
            representation.separatePermissions();
        }
        return representation;
    }
}
