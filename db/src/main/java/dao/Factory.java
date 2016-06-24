package dao;


public class Factory {
    private static Factory instance = null;

    private static ProgramDAO programDAO = null;
    private static WorkedTimeDAO workedTimeDAO = null;
    private static TaskDAO taskDAO = null;

    private Factory() {
    }

    public static synchronized Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public ProgramDAO getProgramDAO() {
        if (programDAO == null) {
            programDAO = new ProgramDAO();
        }
        return programDAO;
    }

    public WorkedTimeDAO getWorkedTimeDAO() {
        if (workedTimeDAO == null) {
            workedTimeDAO = new WorkedTimeDAO();
        }
        return workedTimeDAO;
    }

    public TaskDAO getTaskDAO() {
        if (taskDAO == null) {
            taskDAO = new TaskDAO();
        }
        return taskDAO;
    }
}
