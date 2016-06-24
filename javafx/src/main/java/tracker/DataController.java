package tracker;

import controller.MainFromController;
import dao.Factory;
import entity.Program;
import entity.Task;
import entity.WorkedTime;
import notifier.Observer;

import java.sql.SQLException;
import java.util.*;

// Track changes and verify database
public class DataController implements Observer {
    public static DataController INSTANCE = new DataController();

    private MainFromController mainFromController;

    private String login;
    private List<Task> taskList;

    private DataController() {
        login = "default";
        taskList = null;
    }

    public void verifyAllTask() {
        for (Task task : taskList) {
            Task.verify(task);
        }
    }

    public void update(String programName, String windowTitle, int minutes) {
        mainFromController.showMessageIntoLog("Tracked program name: " + programName + ", window title: " + windowTitle, true);

        Iterator taskIterator = taskList.iterator();

        boolean contains = false;
        while (!contains && taskIterator.hasNext()) {
            Task task = (Task) taskIterator.next();
            List<WorkedTime> workedTimeList = task.getWorkedTimeList();

            contains = task.validateProgramAttributes(programName, windowTitle);
            if (contains) {
                boolean found = false;
                Iterator workedTimeIterator = workedTimeList.iterator();
                while (!found && workedTimeIterator.hasNext()) {
                    WorkedTime workedTime = (WorkedTime) workedTimeIterator.next();
                    if (workedTime.getLogin().equals(login)) {
                        workedTime.setTime(workedTime.getTime() + minutes);

                        updateWorkedTime(workedTime);

                        mainFromController.showMessageIntoLog("User " + workedTime.getLogin() + " continues to work on " + task.getName() + " task for " +
                                minutes + " minutes (currently " + workedTime.getTime() + " minutes)", false);

                        found = true;
                    }
                }

                if (!found) {
                    WorkedTime workedTime = new WorkedTime();
                    workedTime.setLogin(login);
                    workedTime.setDate(new Date());
                    workedTime.setTime(minutes);
                    workedTime.setTask(task);

                    addWorkedTime(task, workedTime);

                    mainFromController.showMessageIntoLog("User " + workedTime.getLogin() + " start to work on " + task.getName() + " task for " +
                            minutes + " minutes", false);

                }
            }
        }
    }

    public void addTask(Task task) {
        try {
            Factory.getInstance().getTaskDAO().add(task);
            taskList.add(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task) {
        try {
            for (Program program: task.getProgramList()) {
                Factory.getInstance().getProgramDAO().delete(program);
            }
            for (WorkedTime workedTime: task.getWorkedTimeList()) {
                Factory.getInstance().getWorkedTimeDAO().delete(workedTime);
            }
            Factory.getInstance().getTaskDAO().delete(task);
            taskList.remove(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task) {
        try {
            Factory.getInstance().getTaskDAO().update(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addActiveProgram(Task task, Program program) {
        try {
            Factory.getInstance().getProgramDAO().add(program);
            task.getProgramList().add(program);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteActiveProgram(Task task, Program program) {
        try {
            Factory.getInstance().getProgramDAO().delete(program);
            task.getProgramList().remove(program);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateActiveProgram(Program program) {
        try {
            Factory.getInstance().getProgramDAO().update(program);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorkedTime(Task task, WorkedTime workedTime) {
        try {
            Factory.getInstance().getWorkedTimeDAO().add(workedTime);
            task.getWorkedTimeList().add(workedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteWorkedTime(Task task, WorkedTime workedTime) {
        try {
            Factory.getInstance().getWorkedTimeDAO().delete(workedTime);
            task.getWorkedTimeList().remove(workedTime);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWorkedTime(WorkedTime workedTime) {
        try {
            Factory.getInstance().getWorkedTimeDAO().update(workedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Task getTaskByName(String name) {
        boolean found = false;
        Task result = null;
        Iterator iterator = taskList.iterator();
        while (!found && iterator.hasNext()) {
            Task task = (Task) iterator.next();
            if (task.getName().equals(name)) {
                result = task;
                found = true;
            }
        }
        return result;
    }

    public List<String> getAllTaskNames() {
        List<String> namesList = new ArrayList<>();
        for (Task task : taskList) {
            namesList.add(task.getName());
        }
        return namesList;
    }

    public List<String> getAllLogins() {
        return Factory.getInstance().getWorkedTimeDAO().executeQuery("select distinct w.login from WorkedTime w", new HashMap<>());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setMainFromController(MainFromController mainFromController) {
        this.mainFromController = mainFromController;
    }
}
