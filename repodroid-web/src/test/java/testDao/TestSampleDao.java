package testDao;

import es.uvigo.esei.tfg.repodroid.web.entities.SampleReference;
import es.uvigo.esei.tfg.repodroid.web.entities.SampleReferenceDao;
import java.util.List;
import javax.ejb.EJB;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class TestSampleDao {

    @EJB
    private SampleReferenceDao sampleDao;

    @Test
    public void testUpdatePath() {
        SampleReference ref = sampleDao.updatePath(1201, "newPath");
        assertThat(ref, is(not(equalTo(null))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullPathUpdate() {
        sampleDao.updatePath(1201, null);
    }

    @Test
    public void TestGetSamplesFromUser() {
        List<SampleReference> s = sampleDao.getAllSamplesFromUser(1051);
        assertThat(s.size(), is(not(equalTo(0))));
    }

    @Test
    public void TestExists() {
        boolean result = sampleDao.existsSample(1);
        assertThat(result, is(equalTo(true)));
    }

    @Test
    public void testGetReferenceFromStoreId() {
        SampleReference s = sampleDao.getReferenceFromStoreId("d9e82a46-4fef-42d9-a42a-502bf0d63bb9");
        assertThat(s.getNumSample(), is(equalTo(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullGetReference() {
        sampleDao.getReferenceFromStoreId(null);
    }
    
    @Test
    public void testSearch(){
        SampleReference s = sampleDao.search(1);
        assertThat(s.getSubmitDate(), is(equalTo("06-06-2016")));
    }
}
