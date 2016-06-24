import dao.Factory;
import entity.Program;
import entity.Task;
import entity.WorkedTime;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDAOTest {
    @Test
    public void testAddRecord() throws SQLException {
        Task task = new Task();
        task.setName("tests");

        Factory.getInstance().getTaskDAO().add(task);

        System.out.println(task);
        Assert.assertNotEquals(task.getId(), 0);

        Factory.getInstance().getTaskDAO().delete(task);
    }

    @Test
    public void testGetRecord() throws SQLException {
        Task task = new Task();
        task.setName("gettask");

        Factory.getInstance().getTaskDAO().add(task);

        List<Program> programList = new ArrayList<Program>();

        Program program = new Program();
        program.setName("Google Chrome");
        program.setWhitelist("[\\w]+[.com]");
        program.setBlacklist("[\\w]+[.ru]");
        program.setTask(task);
        Factory.getInstance().getProgramDAO().add(program);
        programList.add(program);

        program = new Program();
        program.setName("Chrome Google");
        program.setWhitelist("[\\w]+[.com]");
        program.setBlacklist("[\\w]+[.ru]");
        program.setTask(task);
        Factory.getInstance().getProgramDAO().add(program);
        programList.add(program);

        List<WorkedTime> workedTimeList = new ArrayList<WorkedTime>();

        WorkedTime workedTime = new WorkedTime();
        workedTime.setLogin("testlogin");
        workedTime.setDate(new Date());
        workedTime.setTime(312);
        workedTime.setTask(task);
        Factory.getInstance().getWorkedTimeDAO().add(workedTime);
        workedTimeList.add(workedTime);

        task.setProgramList(programList);
        task.setWorkedTimeList(workedTimeList);

        Task task1 = Factory.getInstance().getTaskDAO().getById(task.getId());
        System.out.println(task1);

        Assert.assertEquals(task1, task);

        for (Program program1: programList) {
            Factory.getInstance().getProgramDAO().delete(program1);
        }
        for (WorkedTime workedTime1: workedTimeList) {
            Factory.getInstance().getWorkedTimeDAO().delete(workedTime1);
        }
        Factory.getInstance().getTaskDAO().delete(task);
    }
}
