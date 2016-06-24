package entity;

import dao.Factory;
import dao.TaskDAO;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.*;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 1024)
    private String name;

    @OneToMany(mappedBy = "task")
    private List<Program> programList;

    @OneToMany(mappedBy = "task")
    private List<WorkedTime> workedTimeList;

    public static void verify(Task task) {
        try {
            for (Program program : task.getProgramList()) {
                Factory.getInstance().getProgramDAO().addOrUpdate(program);
            }
            for (WorkedTime workedTime : task.getWorkedTimeList()) {
                Factory.getInstance().getWorkedTimeDAO().addOrUpdate(workedTime);
            }
            Factory.getInstance().getTaskDAO().addOrUpdate(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<WorkedTime> getWorkedTimeList(String login, Date dateFrom, Date dateTo){
        List<WorkedTime> result = new ArrayList<WorkedTime>();
        for (WorkedTime workedTime: workedTimeList) {
            if (workedTime.getLogin().equals(login) && workedTime.getDate().compareTo(dateFrom) >= 0 && workedTime.getDate().compareTo(dateTo) <= 0) {
                result.add(workedTime);
            }
        }
        Collections.sort(result, new Comparator<WorkedTime>(){
            public int compare (WorkedTime workedTime1, WorkedTime workedTime2){
                return workedTime1.getDate().compareTo(workedTime2.getDate());
            }
        });
        return result;
    }

    public List<String> getProgramNamesList() {
        List<String> names = new ArrayList<>();
        for (Program program: programList) {
            names.add(program.getName());
        }
        return names;
    }

    public boolean validateProgramAttributes(String programName, String windowTitle) {
        for (Program program : programList) {
            if (program.getName().equals(programName)) {
                return program.validate(windowTitle);
            }
        }
        return false;
    }

    public boolean validateWorkedTimeAttributes(String login, Date date) {
        for (WorkedTime workedTime: workedTimeList) {
            if (workedTime.getDate().equals(date) && workedTime.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public int getTime(String login, Date date) {
        int time = 0;
        for (WorkedTime workedTime: workedTimeList) {
            if (workedTime.getDate().equals(date) && workedTime.getLogin().equals(login)) {
                time = workedTime.getTime();
            }
        }
        return time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    public List<WorkedTime> getWorkedTimeList() {
        return workedTimeList;
    }

    public void setWorkedTimeList(List<WorkedTime> workedTimeList) {
        this.workedTimeList = workedTimeList;
    }

    @Override
    public String toString() {
        String string = "Task {" +
                " id: " + id +
                ", name: " + name;
        if (programList != null) {
            for (Program program : programList) {
                string += "\n" + program.toString();
            }
        }
        if (workedTimeList != null) {
            for (WorkedTime workedTime : workedTimeList) {
                string += "\n" + workedTime.toString();
            }
        }
        string += " }";
        return string;
    }
}
