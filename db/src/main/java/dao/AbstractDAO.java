package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractDAO<T> {
    public List executeQuery(String queryString, Map<String, Object> parameters) {
        EntityManager entityManager = null;
        List resultList = new ArrayList();
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery(queryString);
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
            resultList = query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public void add(T object) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(T object) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract T addOrUpdate(T object) throws SQLException;

    public abstract T getById(long id) throws SQLException;

    public abstract Collection getAll() throws SQLException;

    public void delete(T object) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(object);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
