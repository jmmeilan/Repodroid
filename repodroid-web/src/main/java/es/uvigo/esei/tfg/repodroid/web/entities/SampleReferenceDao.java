package es.uvigo.esei.tfg.repodroid.web.entities;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

@Stateless
@LocalBean
public class SampleReferenceDao extends GenericDao<SampleReference> {

    public SampleReference updatePath(int sampleId, String path) {
        if (path == null) {
            throw new IllegalArgumentException("The path is null");
        }
        SampleReference sample = searchById(sampleId);
        sample.setSamplePath(path);
        return update(sample);
    }

    public List<SampleReference> getAllSamplesFromUser(int userId) {
        Query q = em.createQuery("SELECT s FROM SampleReference s"
                + " WHERE s.user.idUser = :userId");
        q.setParameter("userId", userId);
        List<SampleReference> toRet = q.getResultList();
        return toRet;
    }

    public boolean existsSample(int sampleId) {
        return (searchById(sampleId) != null);
    }

    public SampleReference getReferenceFromStoreId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("The id is null");
        }
        Query q = em.createQuery("SELECT s FROM SampleReference s "
                + "WHERE s.storerID = :id");
        q.setParameter("id", id);
        return getUniqueResult(q);
    }

    public SampleReference search(int id) {
        Query q = em.createQuery("SELECT s FROM SampleReference s "
                + "WHERE s.numSample = :id");
        q.setParameter("id", id);
        return getUniqueResult(q);
    }

}
