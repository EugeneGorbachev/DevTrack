package dao;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class HibernateUtil {
    private static final HibernateUtil instance = new HibernateUtil();

    private EntityManager entityManager;

    private HibernateUtil() {
    }

    public static HibernateUtil getInstance() {
        return instance;
    }

    public EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = Persistence.createEntityManagerFactory("PERSISTENCE").createEntityManager();
        }
        return entityManager;
    }

    public void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
