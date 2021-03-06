package testCore;

import es.uvigo.esei.tfg.repodroid.web.entities.SampleReference;
import es.uvigo.esei.tfg.repodroid.web.entities.User;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TestSampleReference {

    @Test
    public void TestGetSamplePath() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat("/path", is(equalTo(reference.getSamplePath())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullSamplePath() {
        SampleReference reference = new SampleReference();
        reference.setSamplePath(null);
    }

    @Test
    public void TestSetSamplePath() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        reference.setSamplePath("/nuevoPath");
        assertThat("/nuevoPath", is(equalTo(reference.getSamplePath())));
    }

    @Test
    public void TestGetNumSaple() {
        SampleReference reference = new SampleReference(1,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat(1, is(equalTo(reference.getNumSample())));
    }

    @Test
    public void TestSetNumSample() {
        SampleReference reference = new SampleReference(1,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        reference.setNumSample(2);
        assertThat(2, is(equalTo(reference.getNumSample())));
    }

    @Test
    public void TestGetUser() {
        SampleReference reference = new SampleReference(1,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat(new User(), is(equalTo(reference.getUser())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullUser() {
        SampleReference reference = new SampleReference();
        reference.setUser(null);
    }

    @Test
    public void TestSetUser() {
        User u = new User();
        SampleReference reference = new SampleReference(1,
                u,
                "/path",
                "storeId",
                "sampleName");
        u.setUsername("jmmeilan");
        reference.setUser(u);
        assertThat(u, is(equalTo(reference.getUser())));
    }

    @Test
    public void TestGetStorerId() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat("storeId", is(equalTo(reference.getStorerID())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullStorerId() {
        SampleReference reference = new SampleReference();
        reference.setStorerID(null);
    }

    @Test
    public void TestSetStorerId() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        reference.setStorerID("nuevoStoreId");
        assertThat("nuevoStoreId", is(equalTo(reference.getStorerID())));
    }

    @Test
    public void TestGetSampleName() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat("sampleName", is(equalTo(reference.getSampleName())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullSampleName() {
        SampleReference reference = new SampleReference();
        reference.setSampleName(null);
    }

    @Test
    public void TestSetSampleName() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        reference.setSampleName("nuevoSampleName");
        assertThat("nuevoSampleName", is(equalTo(reference.getSampleName())));
    }

    @Test
    public void TestGetSubmitDate() {
        SampleReference reference = new SampleReference("date", "description");
        assertThat("date", is(equalTo(reference.getSubmitDate())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullSubmitDate() {
        SampleReference reference = new SampleReference();
        reference.setSubmitDate(null);
    }

    @Test
    public void TestSetSubmitDate() {
        SampleReference reference = new SampleReference("date", "description");
        reference.setSubmitDate("newDate");
        assertThat("newDate", is(equalTo(reference.getSubmitDate())));
    }

    @Test
    public void TestGetDescription() {
        SampleReference reference = new SampleReference("date", "description");
        assertThat("description", is(equalTo(reference.getSampleDescription())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestSetNullDescription() {
        SampleReference reference = new SampleReference();
        reference.setSampleDescription(null);
    }

    @Test
    public void TestSetDescription() {
        SampleReference reference = new SampleReference("date", "description");
        reference.setSampleDescription("newDescription");
        assertThat("newDescription", is(equalTo(reference.getSampleDescription())));
    }

    /* @Test
    public void TestHashCode() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        int hash = 89 * 7 + Objects.hashCode(reference.getNumSample());
        assertThat(hash, is(equalTo(reference.hashCode())));
    }

    @Test
    public void TestEquals() {
        SampleReference reference = new SampleReference();
        assertThat(new SampleReference(), is(equalTo(reference)));
    }

    @Test
    public void TestNotEquals() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat(new SampleReference(), is(not(equalTo(reference))));
    }

    @Test
    public void ToString() {
        SampleReference reference = new SampleReference(0,
                new User(),
                "/path",
                "storeId",
                "sampleName");
        assertThat("Sample{id=0, name=sampleName}", is(equalTo(reference.toString())));
    }*/
}
