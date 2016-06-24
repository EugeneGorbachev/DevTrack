package dao;

import entity.WorkedTime;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class WorkedTimeDAO extends AbstractDAO<WorkedTime> {
    @Override
    public WorkedTime addOrUpdate(WorkedTime object) throws SQLException {
        if (object.getId() == 0) {
            Factory.getInstance().getWorkedTimeDAO().add(object);
            return object;
        } else {
            Factory.getInstance().getWorkedTimeDAO().update(object);
            return Factory.getInstance().getWorkedTimeDAO().getById(object.getId());
        }
    }

    public WorkedTime getById(long id) throws SQLException {
        EntityManager entityManager = null;
        WorkedTime workedTime = null;
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            workedTime = entityManager.find(WorkedTime.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workedTime;
    }

    public List<WorkedTime> getAll() throws SQLException {
        String query = "select w from WorkedTime w";
        return executeQuery(query, new HashMap<String, Object>());
    }
}
