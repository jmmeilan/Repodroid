package es.uvigo.esei.tfg.repodroid.store.lucene;

import es.uvigo.esei.tfg.repodroid.core.Analysis;
import es.uvigo.esei.tfg.repodroid.core.IndexableAnalysis;
import es.uvigo.esei.tfg.repodroid.core.ParametrizedQuery;
import es.uvigo.esei.tfg.repodroid.core.SampleQuery;
import es.uvigo.esei.tfg.repodroid.core.SimilarityQuery;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

public class SampleQueryTranslator {
    
    private BooleanQuery.Builder queryBldr;
    
    public SampleQueryTranslator() {}
    
    public Query getLuceneQuery(SampleQuery sampleQuery){
        if(sampleQuery.getSampleQueryName().equals("Similarity query")){
            SimilarityQuery query = (SimilarityQuery) sampleQuery;
            for(Analysis an: query.getTestSample().getAnalises().values()){
                if (an instanceof IndexableAnalysis){
                    String field = an.getAnalysisType(); 
                    for(String value: 
                            ((IndexableAnalysis) an).getIndexableItems()){
                        this.queryBldr.add(new TermQuery(new Term(field,value)), 
                            BooleanClause.Occur.SHOULD);
                    }        
                }
            }
        } else {
            ParametrizedQuery query = (ParametrizedQuery) sampleQuery;
            for(Map.Entry<String,List<String>> parameter: 
                    query.getParameters().entrySet()){
                String field = parameter.getKey();
                for (String value: parameter.getValue()){
                    this.queryBldr.add(new TermQuery(new Term(field, value)),
                            BooleanClause.Occur.SHOULD);
                }
            }
        }
        return this.queryBldr.build();
    }
}
