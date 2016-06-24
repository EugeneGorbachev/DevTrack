import controller.MainFromController;
import dao.Factory;
import dao.HibernateUtil;
import dao.TaskDAO;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import notifier.Notifier;
import tracker.DataController;

public class Main extends Application {
    private static Notifier notifier = new Notifier(Notifier.DEFAULTDELTA);

    public static void main(String[] args) throws Exception {
        DataController.INSTANCE.setTaskList(Factory.getInstance().getTaskDAO().getAll());
        notifier.addObserver(DataController.INSTANCE);

        Thread thread = new Thread(notifier);
        thread.start();
        launch(args);

        thread.interrupt();
    }

    public void start(Stage primaryStage) throws Exception {
        String fxmlFile = "/fxml/mainform.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = fxmlLoader.load();

        MainFromController mainFromController = fxmlLoader.getController();
        mainFromController.setNotifier(notifier);
        DataController.INSTANCE.setMainFromController(mainFromController);

        primaryStage.setTitle("Time Tracker");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    mainFromController.handleCloseApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.getInstance().closeEntityManager();
        System.exit(0);
    }
}
