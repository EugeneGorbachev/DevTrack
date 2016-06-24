import dao.Factory;
import entity.Program;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class ProgramDAOTest {
    @Test
    public void testAddProgram() throws SQLException {
        Program program = new Program();
        program.setName("Google Chrome");
        program.setWhitelist("[\\w]+[.com]");
        program.setBlacklist("[\\w]+[.ru]");

        Factory.getInstance().getProgramDAO().add(program);

        System.out.println(program);
        Assert.assertNotEquals(program.getId(), 0);

        Factory.getInstance().getProgramDAO().delete(program);
    }

    @Test
    public void updateProgram() throws SQLException {
        Program program = new Program();
        program.setName("Google Chrome");
        program.setWhitelist("[\\w]+[.com]");
        program.setBlacklist("[\\w]+[.ru]");

        Factory.getInstance().getProgramDAO().add(program);

        String oldName = program.getName();
        program.setName("Chrome Google");

        Factory.getInstance().getProgramDAO().update(program);
        program = Factory.getInstance().getProgramDAO().getById(program.getId());

        System.out.println(program);
        Assert.assertNotEquals(program.getName(), oldName);

        Factory.getInstance().getProgramDAO().delete(program);
    }

    @Test
    public void testGetProgram() throws SQLException {
        Program program = new Program();
        program.setName("Google Chrome");
        program.setWhitelist("");
        program.setBlacklist("[\\w]+[.ru]");

        Factory.getInstance().getProgramDAO().add(program);

        Program program1 = Factory.getInstance().getProgramDAO().getById(program.getId());

        System.out.println(program1);
        Assert.assertEquals(program, program1);

        Factory.getInstance().getProgramDAO().delete(program);
    }
}
