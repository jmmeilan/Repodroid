package es.uvigo.esei.tfg.repodroid.core.store.lucene;

import es.uvigo.esei.tfg.repodroid.core.model.Analysis;
import es.uvigo.esei.tfg.repodroid.core.model.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.model.Sample;
import es.uvigo.esei.tfg.repodroid.core.model.SampleQuery;
import es.uvigo.esei.tfg.repodroid.core.store.Indexer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneIndexer implements Indexer {

    private Directory indexDirectory;
    private Analyzer analyzer;
    private IndexWriterConfig writerConfig;
    private IndexWriter writer;
    private String basePath;
    private Logger logger;
    private SampleQueryTranslator queryTranslator;

    @Override
    public void initialize(String basePath, Logger l) {
        if (basePath == null) {
            throw new IllegalArgumentException("The base path is null");
        }
        this.logger = l;
        this.logger.log(Level.INFO, "Initializing lucene indexer...");
        this.queryTranslator = new SampleQueryTranslator(this.logger);
        try {
            this.basePath = basePath;
            this.indexDirectory = FSDirectory.open(Paths.get(this.basePath));
            this.analyzer = new WhitespaceAnalyzer();
            this.writerConfig = new IndexWriterConfig(this.analyzer);
            this.writerConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            this.writer = new IndexWriter(this.indexDirectory, this.writerConfig);
        } catch (IOException e) {
            this.logger.log(Level.INFO, "EXCEPTION: IOException while initializing LuceneIndexer");
        }
    }

    @Override
    public void close() {
        try {
            this.writer.commit();
            this.writer.close();
            this.logger.log(Level.INFO, "Terminating lucene indexer...");
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void indexSample(Sample sample) {
        if (sample == null) {
            throw new IllegalArgumentException("The sample is null");
        }
        this.logger.log(Level.INFO, "Indexing new sample...");
        try {
            indexAnalyses(this.writer, sample, 1);
        } catch (IOException e) {
            this.logger.log(Level.INFO, "EXCEPTION: IOException while indexing");
        }
    }

    @Override
    public void removeSample(String sampleID) {
        if (sampleID == null) {
            throw new IllegalArgumentException("The ID is null");
        }
        this.logger.log(Level.INFO, "Deleting a document...");
        try {
            this.writer.deleteDocuments(new Term("ID", sampleID));
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void updateSample(String sampleID, Sample sample) {
        if (sampleID == null || sample == null) {
            throw new IllegalArgumentException("The parameters can't be null");
        }
        this.logger.log(Level.INFO, "Updating index of a sample...");
        try {
            indexAnalyses(this.writer, sample, 2);
        } catch (IOException e) {
            this.logger.log(Level.INFO, "EXCEPTION: IOException while updating an index...");
        }
    }

    @Override
    //QUe es firtResult?
    public List<String> search(SampleQuery query, int firstResult, int numberOfSamples) {
        if (query == null) {
            throw new IllegalArgumentException("The query can't be null");
        }
        this.logger.log(Level.INFO, "Starting a new search...");
        List<String> result = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(this.writer, true); //TRUE OR FALSE? applyAllDeletes
            IndexSearcher searcher = new IndexSearcher(reader);
            Query luceneQuery = this.queryTranslator.getLuceneQuery(query);
            TopDocs results = searcher.search(luceneQuery, 5 * numberOfSamples);
            ScoreDoc[] hits = results.scoreDocs;
            int numTotalHits = results.totalHits;
            for (int i = 0; i < numTotalHits; i++) {
                Document doc = searcher.doc(hits[i].doc);
                result.add(doc.get("ID"));
            }
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int numberOfDocuments() {
        return this.writer.numDocs();
    }

    public int simpleDocCount(SampleQuery query) throws IOException {
        IndexReader reader = DirectoryReader.open(this.writer, true);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query luceneQuery = this.queryTranslator.getLuceneQuery(query);
        TotalHitCountCollector collector = new TotalHitCountCollector();
        searcher.search(luceneQuery, collector);
        return collector.getTotalHits();
    }

    private void indexAnalyses(final IndexWriter indexWriter,
            Sample sample, int op) throws IOException {
        if (sample == null) {
            throw new IllegalArgumentException("The sample can't be null");
        }
        Document doc = new Document();
        doc.add(new StringField("ID", sample.getId(), Field.Store.YES));
        for (Analysis an : sample.getAnalises().values()) {
            if (an instanceof IndexableAnalysis) {
                String fieldName = an.getAnalysisType();
                List<String> l = new ArrayList();
                String fieldValues = "";
                if (fieldName.equals("ApkPermissionsAnalysis")) {
                    for (String s : ((IndexableAnalysis) an).getIndexableItems()) {
                        l.add(s.split(":")[0]);
                    }
                    fieldValues = listToString(l);
                } else {
                    fieldValues = listToString(((IndexableAnalysis) an).getIndexableItems());
                }
                if (!fieldValues.isEmpty()) {
                    doc.add(new TextField(fieldName, fieldValues, Field.Store.NO));
                }
            }
        }
        if (op == 1) {
            indexWriter.addDocument(doc);
        } else if (op == 2) {
            indexWriter.updateDocument(new Term("ID", sample.getId()), doc);
        }
    }

    private String listToString(List<String> indexableItems) {
        StringBuilder sb = new StringBuilder();
        for (String item : indexableItems) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(item);
        }
        return sb.toString();
    }
}
