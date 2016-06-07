package TestCore;

import es.uvigo.esei.tfg.repodroid.core.ParametrizedQuery;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.store.json.JSONStorer;
import es.uvigo.esei.tfg.repodroid.store.lucene.LuceneIndexer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestIndexer {

    private static LuceneIndexer indexer;
    private static Sample sample;
    private static JSONStorer storer;

    @BeforeClass
    public static void setUp() {
        storer = new JSONStorer();
        storer.initialize("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "STORE", Logger.getLogger(""));
        indexer = new LuceneIndexer();
        indexer.initialize("/home/jmmeilan/Cuckoo/Sample_Store" + File.separator + "INDEX", Logger.getLogger(""));
        sample = storer.retrieveSample("67afaa39-54b0-4038-9e48-26b1d0f4059e");
        sample.setId("1");
    }
    
    @AfterClass
    public static void terminate(){
        indexer.close();
        storer.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInitialize() {
        indexer.initialize(null, Logger.getLogger(""));
    }

    @Test
    public void testIndexSample() {
        if(indexer.numberOfDocuments() > 0){
            indexer.removeSample("1");
        }
        indexer.indexSample(sample);
        assertThat(indexer.numberOfDocuments(), is(equalTo(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullIndexing() {
        indexer.indexSample(null);
    }
    
    @Test
    public void testSearch() {
        if (indexer.numberOfDocuments() == 0) {
            indexer.indexSample(sample);
        }
        Map<String, List<String>> parameters = new HashMap();
        List<String> values = new ArrayList();
        values.add("Avast");
        parameters.put("AntiVirusAnalysis", values);
        List<String> result = indexer.search(new ParametrizedQuery(parameters), 5, 5);
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullSearch() {
        indexer.search(null, 0, 0);
    }

    //DA PROBLEMAS
    @Test
    public void testRemoveSample() {
        if (indexer.numberOfDocuments() == 0) {
            indexer.indexSample(sample);
        }
        indexer.removeSample("1");
        assertThat(indexer.numberOfDocuments(), is(equalTo(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullRemoval() {
        indexer.removeSample(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullUpdate() {
        indexer.updateSample(null, sample);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestNullUpdate2() {
        indexer.updateSample("1", null);
    }

}
