package testController;

import es.uvigo.esei.tfg.repodroid.web.controllers.SampleViewHelper;
import es.uvigo.esei.tfg.repodroid.web.entities.SampleRepresentation;
import es.uvigo.esei.tfg.repodroid.web.entities.WrongAnalysisViewException;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.AntiVirusAnalysis;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.ApkClassesAnalysis;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.ApkPermissionsAnalysis;
import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.OutputConnectionsAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.WrongValueDataException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TestSampleViewHelper {

    @Test
    public void testAntivirusExtract() throws WrongValueDataException,
            WrongAnalysisViewException {
        Sample s = new Sample();
        AntiVirusAnalysis a = new AntiVirusAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        a.addAntiVirus("Primer antivirus");
        s.addAnalysis(a.getAnalysisName(), a);
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractAntiVirusAnalysis(a.getAnalisisView(), representation);
        assertThat("Primer antivirus", is(equalTo(representation.getAntiViruses().get(0))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAntivirusExtract() throws WrongValueDataException,
            WrongAnalysisViewException {
        Sample s = new Sample();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractAntiVirusAnalysis(null, null);
    }

    @Test(expected = WrongAnalysisViewException.class)
    public void testWrongAntiVirusExtract() throws WrongValueDataException,
            WrongAnalysisViewException {
        Sample s = new Sample();
        ApkClassesAnalysis apk = new ApkClassesAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractAntiVirusAnalysis(apk.getAnalisisView(), representation);
    }

    @Test
    public void testClassesExtract() throws WrongValueDataException,
            WrongAnalysisViewException {

        Sample s = new Sample();
        ApkClassesAnalysis a = new ApkClassesAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        a.addClass("First class");
        s.addAnalysis(a.getAnalysisName(), a);
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractClassesAnalysis(a.getAnalisisView(), representation);
        assertThat("First class", is(equalTo(representation.getClasses().get(0))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullClassesExtract() throws WrongValueDataException,
            WrongAnalysisViewException {
        Sample s = new Sample();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractAntiVirusAnalysis(null, null);
    }

    @Test(expected = WrongAnalysisViewException.class)
    public void testWrongClassesExtract() throws WrongValueDataException,
            WrongAnalysisViewException {

        Sample s = new Sample();
        AntiVirusAnalysis a = new AntiVirusAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractClassesAnalysis(a.getAnalisisView(), representation);
    }

    @Test
    public void testPermissionExtract() throws WrongValueDataException,
            WrongAnalysisViewException {

        Sample s = new Sample();
        ApkPermissionsAnalysis a = new ApkPermissionsAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        a.addPermission("permission", "dangerous");
        s.addAnalysis(a.getAnalysisName(), a);
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractPermissionsAnalysis(a.getAnalisisView(), representation);
        representation.separatePermissions();
        assertThat("permission", is(equalTo(representation.getPermissions().get(0))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPermissionsExtract() throws WrongValueDataException,
            WrongAnalysisViewException {
        Sample s = new Sample();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractPermissionsAnalysis(null, null);
    }

    @Test(expected = WrongAnalysisViewException.class)
    public void testWrongPermissionsExtract() throws WrongValueDataException,
            WrongAnalysisViewException {

        Sample s = new Sample();
        AntiVirusAnalysis a = new AntiVirusAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractPermissionsAnalysis(a.getAnalisisView(), representation);
    }

    @Test
    public void testConnectionsExtract() throws WrongValueDataException,
            WrongAnalysisViewException {

        Sample s = new Sample();
        OutputConnectionsAnalysis a = new OutputConnectionsAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        a.addDNSQuery("query1");
        a.addExternalHost("host1");
        s.addAnalysis(a.getAnalysisName(), a);
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractConnectionsAnalysis(a.getAnalisisView(), representation);
        assertThat("query1", is(equalTo(representation.getDnsQueries().get(0))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConnectionsExtract() throws WrongValueDataException,
            WrongAnalysisViewException {
        Sample s = new Sample();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractConnectionsAnalysis(null, null);
    }

    @Test(expected = WrongAnalysisViewException.class)
    public void testWrongConnectionsExtract() throws WrongValueDataException,
            WrongAnalysisViewException {

        Sample s = new Sample();
        AntiVirusAnalysis a = new AntiVirusAnalysis();
        SampleRepresentation representation = new SampleRepresentation();
        SampleViewHelper helper = new SampleViewHelper(s);
        helper.extractPermissionsAnalysis(a.getAnalisisView(), representation);
    }
}
