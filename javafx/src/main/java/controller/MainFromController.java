package controller;

import entity.Task;
import entity.WorkedTime;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import notifier.Notifier;
import tracker.DataController;
import tracker.TrayIconController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class MainFromController implements Initializable {
    private final String ICON_STR = "/images/icon16x16.png";
    private Notifier notifier;
    private TrayIcon trayIcon = null;

    @FXML
    public MenuItem menuItemOpenFromFile;
    @FXML
    public MenuItem menuItemClose;
    @FXML
    public MenuItem menuItemStartTracking;
    @FXML
    public MenuItem menuItemStopTracking;
    @FXML
    public MenuItem menuItemAbout;

    @FXML
    public TextField textFieldLogin;

    @FXML
    public BarChart barChartWorkedTime;

    @FXML
    public ComboBox comboBoxUserLogin;
    @FXML
    public DatePicker datePickerFrom;
    @FXML
    public DatePicker datePickerTo;
    @FXML
    public Button buttonDraw;

    @FXML
    public ListView listViewTaskList;
    @FXML
    public Button buttonEditTaskDetails;

    @FXML
    public TextField textFieldNotifierDelta;

    @FXML
    public Button buttonStartTracking;
    @FXML
    public Button buttonStopTracking;

    @FXML
    public TextArea textAreaLog;

    public void initialize(URL location, ResourceBundle resources) {
        buttonDraw.setDisable(true);
        textFieldLogin.setText(DataController.INSTANCE.getLogin());
        textFieldNotifierDelta.setText(String.valueOf(Notifier.DEFAULTDELTA));

        updateComboBoxData();
        showTaskList();

        textFieldLogin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("[A-z][\\w]*")) {
                    DataController.INSTANCE.setLogin(newValue);
                } else {
                    textFieldLogin.setText(oldValue);
                }
            }
        });

        textFieldNotifierDelta.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("\\d+")) {
                    if (Integer.parseInt(newValue) > 1440) {
                        notifier.setDelta(1440);
                        textFieldNotifierDelta.setText("1440");
                    } else {
                        notifier.setDelta(Integer.parseInt(newValue));
                    }
                } else {
                    textFieldLogin.setText(oldValue);
                }
            }
        });

        datePickerFrom.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null
                        && comboBoxUserLogin.getValue() != null && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonDraw.setDisable(false);
                }
            }
        });

        datePickerTo.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null
                        && comboBoxUserLogin.getValue() != null && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonDraw.setDisable(false);
                }
            }
        });

        comboBoxUserLogin.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null
                        && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonDraw.setDisable(false);
                }
            }
        });

        listViewTaskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (datePickerFrom.getValue() != null && datePickerTo.getValue() != null
                        && comboBoxUserLogin.getValue() != null) {
                    buttonDraw.setDisable(false);
                }
            }
        });
        
        try {
            trayIcon = TrayIconController.createTrayIcon(MainFromController.class.getResource(ICON_STR), this);
        } catch (Exception e) {
            textAreaLog.appendText(e.getMessage());
        }
        try {
            TrayIconController.addIconToTray(trayIcon);
        } catch (Exception e) {
            textAreaLog.appendText(e.getMessage());
        }

    }

    public void handleCloseApp() throws Exception {
        handleStopTracking();
        TrayIconController.removeTrayIcon(trayIcon);
    }

    @FXML
    public void handleDrawChart() {
        Date dateFrom = Date.from(datePickerFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(datePickerTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (dateFrom.before(dateTo)) {
            Task task = DataController.INSTANCE.getTaskByName(listViewTaskList.getSelectionModel().getSelectedItem().toString());
            String login = (String) comboBoxUserLogin.getValue();

            barChartWorkedTime.getData().clear();
            XYChart.Series series = new XYChart.Series();
            series.setName(login);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFrom);
            while (calendar.getTime().before(dateTo)) {
                series.getData().add(new XYChart.Data(new SimpleDateFormat("dd.MM.yyy").format(calendar.getTime()), task.getTime(login, calendar.getTime()) / 60));
                calendar.add(Calendar.DATE, 1);
            }
            barChartWorkedTime.getData().add(series);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong 'DateFrom' and 'DateTo' order");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleStartTracking() {
        if (!notifier.isNeedToNotify()) {
            buttonStartTracking.setDisable(true);
            buttonStopTracking.setDisable(false);
            buttonEditTaskDetails.setDisable(true);
            textFieldNotifierDelta.setDisable(true);
            textAreaLog.clear();

            notifier.setNeedToNotify(true);

            DataController.INSTANCE.verifyAllTask();
            showMessageIntoLog("Database data verified", true);

            notifier.setNeedToNotify(true);
            showMessageIntoLog("Tracking has been started", false);
        }
    }

    @FXML
    public void handleStopTracking() {
        if (notifier.isNeedToNotify()) {
            buttonStartTracking.setDisable(false);
            buttonStopTracking.setDisable(true);
            buttonEditTaskDetails.setDisable(false);
            textFieldNotifierDelta.setDisable(false);

            notifier.setNeedToNotify(false);

            DataController.INSTANCE.verifyAllTask();
            showMessageIntoLog("Database data verified", true);

            notifier.setNeedToNotify(false);
            showMessageIntoLog("Tracking has been stopped", false);

            updateComboBoxData();
        }
    }

    @FXML
    public void handleShowAbout() {
        // about window
    }


    @FXML
    public void handleEditTaskDetails() throws IOException {
        handleStopTracking();

        Parent root;
        String fxmlFile = "/fxml/details.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Task details");
        stage.setScene(new Scene(root));
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                updateComboBoxData();
                showTaskList();
            }
        });
    }

    private void updateComboBoxData() {
        ObservableList<String> observableList = FXCollections.observableList(DataController.INSTANCE.getAllLogins());
        comboBoxUserLogin.setItems(observableList);
    }

    private void showTaskList() {
        ObservableList<String> observableList = FXCollections.observableList(DataController.INSTANCE.getAllTaskNames());
        listViewTaskList.getItems().clear();
        listViewTaskList.setItems(observableList);
    }

    public void showMessageIntoLog(String message, boolean showTime){
        if (showTime) {
            message = new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()) + " - " + message;
        }
        final String finalText = message + '\n';
        javafx.application.Platform.runLater(() -> textAreaLog.appendText(finalText));
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }
}
