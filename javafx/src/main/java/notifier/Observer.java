package notifier;

public interface Observer {
    void update(String programName, String windowTitle, int minutes);
}
