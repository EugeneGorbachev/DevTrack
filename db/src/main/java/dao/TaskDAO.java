package dao;

import entity.Task;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class TaskDAO extends AbstractDAO<Task> {
    @Override
    public Task addOrUpdate(Task object) throws SQLException {
        if (object.getId() == 0) {
            Factory.getInstance().getTaskDAO().add(object);
            return object;
        } else {
            Factory.getInstance().getTaskDAO().update(object);
            return Factory.getInstance().getTaskDAO().getById(object.getId());
        }
    }

    public Task getById(long id) throws SQLException {
        EntityManager entityManager = null;
        Task task = null;
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            task = entityManager.find(Task.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task ;
    }

    public List<Task> getAll() throws SQLException {
        String query = "select t from Task t";
        return executeQuery(query, new HashMap<String, Object>());
    }
}
