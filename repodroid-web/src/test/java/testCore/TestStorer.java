package testCore;

import es.uvigo.esei.tfg.repodroid.core.analysis.cuckoo.AntiVirusAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.store.json.JSONStorer;
import java.io.File;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStorer {

    private static JSONStorer storer;
    private static Sample sample;

    @BeforeClass
    public static void setUp() {
        storer = new JSONStorer();
        storer.initialize("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE", Logger.getLogger(""));
        sample = new Sample();
        AntiVirusAnalysis a = new AntiVirusAnalysis();
        a.addAntiVirus("Primer antivirus");
        sample.addAnalysis(a.getAnalysisName(), a);
        sample.setId("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialize() {
        JSONStorer store = new JSONStorer();
        storer.initialize(null, Logger.getLogger(""));
    }

    @Test
    public void testJsonStoring() {
        storer.storeSample(sample);
        File file = new File("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE/1");
        assertThat(file.getName(), is(equalTo("1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullStoring() {
        storer.storeSample(null);
    }

    @Test
    public void testRetrieveSample() {
        File file = new File("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE/1");
        if (!file.exists()) {
            storer.storeSample(sample);
        }
        Sample s = storer.retrieveSample("1");
        assertThat(s.getId(), is(equalTo("1")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRetrieve() {
        storer.retrieveSample(null);
    }

    @Test
    public void testRemoval() {
        storer.removeSample("1");
        File file = new File("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE/1");
        assertThat(file.exists(), is(equalTo(false)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRemoval() {
        storer.removeSample(null);
    }

    @Test
    public void testUpdate() {
        File file = new File("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE/1");
        if (!file.exists()) {
            storer.storeSample(sample);
        }
        Sample s = new Sample();
        s.setId("2");
        storer.updateSample("1", s);
        file = new File("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE/2");
        assertThat(file.getName(), is(equalTo("2")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUpdate() {
        storer.updateSample(null, sample);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUpdate2() {
        storer.updateSample("1", null);
    }
}
