package dao;

import entity.Program;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class ProgramDAO extends AbstractDAO<Program> {
    public Program addOrUpdate(Program object) throws SQLException {
        if (object.getId() == 0) {
            Factory.getInstance().getProgramDAO().add(object);
            return object;
        } else {
            Factory.getInstance().getProgramDAO().update(object);
            return Factory.getInstance().getProgramDAO().getById(object.getId());
        }
    }

    public Program getById(long id) throws SQLException {
        EntityManager entityManager = null;
        Program program = null;
        try {
            entityManager = HibernateUtil.getInstance().getEntityManager();
            program = entityManager.find(Program.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return program;
    }

    public List<Program> getAll() throws SQLException {
        String query = "select a from Program a";
        return executeQuery(query, new HashMap<String, Object>());
    }
}
