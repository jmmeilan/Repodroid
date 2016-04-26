
package Core;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


public class GenericDao<T> {
    
    @PersistenceContext(unitName = "repodroidPU")
    protected EntityManager em;
    
    protected Class<T> entityClass;
    
    private void identifyEntityClass(){
        this.entityClass = (Class<T>) 
                            ((ParameterizedType) getClass()
                                    .getGenericSuperclass())
                                    .getActualTypeArguments()[0];
    }
    
    public T create(T entity){
        em.persist(entity);
        return entity;
    }
    
    public T update(T entity){
        return em.merge(entity);
    }
    
    public void delete(T entity){
        em.remove(em.merge(entity));
    }
    
    public T searchById(Object Id){
        if (this.entityClass == null){
            identifyEntityClass();
        }
        return em.find(this.entityClass, Id);
    }
    
        public List<T> getAll() {
        if (this.entityClass == null) {
            identifyEntityClass();
        }
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(this.entityClass);
        query.select(query.from(this.entityClass));
        return em.createQuery(query).getResultList();
    }
    
    public Long countAll(){
        if (this.entityClass == null) {
            identifyEntityClass();
        }
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(this.entityClass)));
        return em.createQuery(query).getSingleResult();
    }
    
    public List<T> parametrizedQuery(String consulta, Map<String, Object> parametros) {
        Query query = em.createQuery(consulta);
        for (Map.Entry<String, Object> parametro : parametros.entrySet()) {
            query.setParameter(parametro.getKey(), parametro.getValue());
        }
        return query.getResultList();
    }
    
    protected T getUniqueResult(Query query) {
        List<T> results = query.getResultList();
        if (results == null) {
            return null;  // Not found
        } else if (results.size() != 1) {
            return null; // Not found
        } else {
            return results.get(0); 
        }
    }
}
