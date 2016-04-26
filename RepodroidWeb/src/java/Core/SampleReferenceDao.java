
package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SampleReferenceDao extends GenericDao<SampleReference>{
    
    public SampleReference updatePath(int sampleId, String path){
        SampleReference sample = searchById(sampleId);
        sample.setSamplePath(path);
        return update(sample);
    }
    
    public List<SampleReference> getAllSamplesFromUser(int userId){
        List<SampleReference> toRet = new ArrayList();
        Map <String, Object> parameters = new HashMap();
        parameters.put("userID", userId);
        parametrizedQuery("SELECT s FROM Samples_References s"
                + " WHERE (s.user.iduser = :userId)",  parameters);
        return toRet;
    }
    
    public boolean existsSample(int sampleId){
        return (searchById(sampleId) != null);
    }
    
}
