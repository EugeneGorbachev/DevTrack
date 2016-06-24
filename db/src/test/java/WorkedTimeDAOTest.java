import dao.Factory;
import entity.WorkedTime;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;

public class WorkedTimeDAOTest {
    @Test
    public void testAddRecord() throws SQLException {
        WorkedTime workedTime = new WorkedTime();
        workedTime.setLogin("testlogin");
        workedTime.setDate(new Date());
        workedTime.setTime(312);

        Factory.getInstance().getWorkedTimeDAO().add(workedTime);

        System.out.println(workedTime);
        Assert.assertNotEquals(workedTime.getId(), 0);

        Factory.getInstance().getWorkedTimeDAO().delete(workedTime);
    }

    @Test
    public void testGetRecord() throws SQLException {
        WorkedTime workedTime = new WorkedTime();
        workedTime.setLogin("newlog");
        workedTime.setDate(new Date());
        workedTime.setTime(124);

        Factory.getInstance().getWorkedTimeDAO().add(workedTime);

        WorkedTime workedTime1 = Factory.getInstance().getWorkedTimeDAO().getById(workedTime.getId());

        System.out.println(workedTime1);
        Assert.assertEquals(workedTime, workedTime1);

        Factory.getInstance().getWorkedTimeDAO().delete(workedTime);
    }
}
