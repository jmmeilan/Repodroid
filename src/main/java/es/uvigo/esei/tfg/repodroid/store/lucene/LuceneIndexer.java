package es.uvigo.esei.tfg.repodroid.store.lucene;

import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.Sample;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import es.uvigo.esei.tfg.repodroid.store.Indexer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer implements Indexer {

    private Directory indexDirectory;
    private Analyzer analyzer;
    private IndexWriterConfig writerConfig;
    private IndexWriter writer;
    private String basePath;
    
    @Override
    public void initialize(String basePath) {
       System.out.println("Initializing lucene indexer..."); //Cambiar system.out por un logger
       try {
           this.indexDirectory = FSDirectory.open(Paths.get(basePath));
           this.analyzer = new KeywordAnalyzer();
           this.writerConfig = new IndexWriterConfig(this.analyzer);
           this.writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
           this.basePath = basePath;
           this.writer = new IndexWriter(this.indexDirectory, this.writerConfig);
       } catch (IOException e){
           System.out.println("EXCEPTION: IOException en LuceneIndexer");
       }
    }

    @Override
    public void close() {
        try {
            this.writer.commit();
            this.writer.close();
            System.out.println("Terminating lucene indexer...");
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void indexSample(Sample sample) {
        System.out.println("Indexing new sample...");
        try{
            indexAnalyses(this.writer, sample);
        } catch (IOException e){
            System.out.println("EXCEPTION: IOException while indexing...");
        }

    }

    @Override
    public void removeSample(String sampleID) {
        System.out.println("Deleting a document...");
        try {
           this.writer.deleteDocuments(new Term("ID", sampleID));
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

    @Override
    public void updateSample(String sampleID, Sample sample) {
        System.out.println("Updating index of a sample...");
        try{
            indexAnalyses(this.writer, sample);
        } catch (IOException e){
            System.out.println("EXCEPTION: IOException while updating an index...");
        }
    }

    @Override
    public List<String> search(SampleQuery query, int firstResult, int numberOfSamples) {
        System.out.println("Starting a new search...");
        try {
            IndexReader reader = DirectoryReader.open(this.writer, true); //TRUE OR FALSE? applyAllDeletes
            IndexSearcher searcher = new IndexSearcher(reader);
        } catch (IOException ex) {
            Logger.getLogger(LuceneIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<String>();
    }
    
    private void indexAnalyses(final IndexWriter indexWriter, 
                             Sample sample) throws IOException{
        Document doc = new Document();
        doc.add(new StringField("ID", sample.getId(), Field.Store.NO));
        for(Analysis an: sample.getAnalises().values()){
            if (an instanceof IndexableAnalysis){
                String fieldName = an.getAnalysisType();
                String fieldValues = listToString(((IndexableAnalysis) an).getIndexableItems());
                doc.add(new StringField(fieldName, fieldValues, Field.Store.NO)); 
            }
        }
        indexWriter.updateDocument(new Term("ID", sample.getId()), doc);
    }

    private String listToString(List<String> indexableItems) {
        StringBuilder sb = new StringBuilder();
        for (String item :indexableItems) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(item);
        }
        return sb.toString();    
    }    
}