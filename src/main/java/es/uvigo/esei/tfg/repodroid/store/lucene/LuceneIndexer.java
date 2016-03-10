package es.uvigo.esei.tfg.repodroid.store.lucene;

import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.AntiVirusAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.ApkClassesAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.ApkPermissionsAnalysis;
import es.uvigo.esei.tfg.repodroid.analysis.cuckoo.OutputConnectionsAnalysis;
import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import es.uvigo.esei.tfg.repodroid.store.Indexer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer implements Indexer {

    private Directory indexDirectory;
    private Analyzer analyzer;
    private IndexWriterConfig writerConfig;
    
    @Override
    public void initialize(String basePath) {
       System.out.println("Initializing lucene indexer...");
       try {
           this.indexDirectory = FSDirectory.open(Paths.get(basePath));
           this.analyzer = new StandardAnalyzer();
           this.writerConfig = new IndexWriterConfig(this.analyzer);
       } catch (IOException e){
           System.out.println("EXCEPTION: IOException en LuceneIndexer");
       }
    }

    @Override
    public void close() {
        System.out.println("Terminating lucene indexer...");
    }

    @Override
    public void indexSample(Sample sample) {
        System.out.println("Indexing new sample...");
        try{
            this.writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(this.indexDirectory, this.writerConfig);
            indexAnalyses(writer, sample);
            writer.close();
        } catch (IOException e){
            System.out.println("EXCEPTION: IOException while indexing...");
        }

    }

    @Override
    public void removeSample(String sampleID) {
        System.out.println("Deleting a document...");
        try {
            IndexWriter writer = new IndexWriter(this.indexDirectory, this.writerConfig);
            writer.deleteDocuments(new Term("ID", sampleID));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    @Override
    public void updateSample(String sampleID, Sample sample) {
        System.out.println("Updating index of a sample...");
        try{
            this.writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(this.indexDirectory, this.writerConfig);
            indexAnalyses(writer, sample);
            writer.close();
        } catch (IOException e){
            System.out.println("EXCEPTION: IOException while updating an index...");
        }
    }

    @Override
    public List<String> search(SampleQuery query, int firstResult, int numberOfSamples) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void indexAnalyses(final IndexWriter indexWriter, 
                             Sample sample) throws IOException{
        Document doc = new Document();
        //TODO: a way to iterate over indexableAnalysis? Maybe that way we dont need every analysis type
        doc.add(new StringField("ID", sample.getId(), Field.Store.NO));
        for(Analysis an: sample.getAnalises().values()){
            switch(an.getAnalysisType()){
                case OutputConnectionsAnalysis.TYPE: 
                    OutputConnectionsAnalysis output = (OutputConnectionsAnalysis) an;
                    for (String s: output.getIndexableItems()){
                        //¿como diferenciar entre dns o host?¿Es importante? FRAN
                        doc.add(new StringField("Output Analysis", s, Field.Store.YES)); //FIELD STORE NO?¿?¿?¿ FRAN
                    }
                    break;
                case AntiVirusAnalysis.TYPE:
                    AntiVirusAnalysis antivirus = (AntiVirusAnalysis) an;
                    for (String s: antivirus.getIndexableItems()){
                        doc.add(new StringField("Antivirus Analysis", s, Field.Store.YES)); //FIELD STORE NO?¿?¿?¿
                    }
                    break;
                case ApkClassesAnalysis.TYPE:
                    ApkClassesAnalysis apkClassInfo = (ApkClassesAnalysis) an;
                    for (String s: apkClassInfo.getIndexableItems()){
                        doc.add(new StringField("Apk classes Analysis", s, Field.Store.YES)); //FIELD STORE NO?¿?¿?¿
                    }
                    break;
                case ApkPermissionsAnalysis.TYPE:
                    ApkPermissionsAnalysis apkPermissionInfo = (ApkPermissionsAnalysis) an;
                    for (String s: apkPermissionInfo.getIndexableItems()){
                        doc.add(new StringField("Apk permissions Analysis", s, Field.Store.YES)); //FIELD STORE NO?¿?¿?¿
                    }
                    break;
            }
        }
        if(indexWriter.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE){
                indexWriter.addDocument(doc);
        } else {
                indexWriter.updateDocument(new Term("ID", sample.getId()), doc);
        }
    }
    
}
