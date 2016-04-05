package es.uvigo.esei.tfg.repodroid.store.lucene;

import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.ParametrizedQuery;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import es.uvigo.esei.tfg.repodroid.core.SimilarityQuery;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class SampleQueryTranslator {

    private BooleanQuery.Builder queryBldr;
    private Logger logger;

    public SampleQueryTranslator(Logger l) {
        this.logger = l;
    }

    public Query getLuceneQuery(SampleQuery sampleQuery) {
        this.logger.log(Level.INFO, "Creating a lucene query...");
        this.queryBldr = new BooleanQuery.Builder();
        if (sampleQuery.getSampleQueryName().equals("Similarity query")) {
            SimilarityQuery query = (SimilarityQuery) sampleQuery;
            for (Analysis an : query.getTestSample().getAnalises().values()) {
                String field = an.getAnalysisType();
                if (an instanceof IndexableAnalysis) {
                    for (String value
                            : ((IndexableAnalysis) an).getIndexableItems()) {
                        boolean retry = true;
                        while (retry) {
                            try {
                                retry = false;
                                this.queryBldr.add(new TermQuery(new Term(field, value)),
                                        BooleanClause.Occur.SHOULD);
                            } catch (BooleanQuery.TooManyClauses e) {
                                // Double the number of boolean queries allowed.
                                // The default is in org.apache.lucene.search.BooleanQuery and is 1024.
                                String defaultQueries = Integer.toString(BooleanQuery.getMaxClauseCount());
                                int oldQueries = Integer.parseInt(System.getProperty("org.apache.lucene.maxClauseCount", defaultQueries));
                                int newQueries = oldQueries * 2;
                                this.logger.log(Level.WARNING,
                                        "Too many clauses for query: " + oldQueries + ".  Increasing to " + newQueries, e);
                                System.setProperty("org.apache.lucene.maxClauseCount", Integer.toString(newQueries));
                                BooleanQuery.setMaxClauseCount(newQueries);
                                retry = true;
                            }
                        }
                    }
                }
            }
        } else {
            ParametrizedQuery query = (ParametrizedQuery) sampleQuery;
            for (Map.Entry<String, List<String>> parameter
                    : query.getParameters().entrySet()) {
                String field = parameter.getKey();
                for (String value : parameter.getValue()) {
                    boolean retry = true;
                    while (retry) {
                        try {
                            retry = false;
                            this.queryBldr.add(new TermQuery(new Term(field, value)),
                                    BooleanClause.Occur.SHOULD);
                        } catch (BooleanQuery.TooManyClauses e) {
                            // Double the number of boolean queries allowed.
                            // The default is in org.apache.lucene.search.BooleanQuery and is 1024.
                            String defaultQueries = Integer.toString(BooleanQuery.getMaxClauseCount());
                            int oldQueries = Integer.parseInt(System.getProperty("org.apache.lucene.maxClauseCount", defaultQueries));
                            int newQueries = oldQueries * 2;
                            this.logger.log(Level.WARNING,
                                    "Too many clauses for query: " + oldQueries + ".  Increasing to " + newQueries, e);
                            System.setProperty("org.apache.lucene.maxClauseCount", Integer.toString(newQueries));
                            BooleanQuery.setMaxClauseCount(newQueries);
                            retry = true;
                        }
                    }
                }
            }
        }
        return this.queryBldr.build();
    }
}
