package controller;

import entity.Program;
import entity.Task;
import entity.WorkedTime;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import tracker.DataController;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class DetailsController implements Initializable {
    @FXML
    public ListView listViewTaskList;
    @FXML
    public TextField textFieldTaskName;
    @FXML
    public Button buttonAddTask;
    @FXML
    public Button buttonDeleteTask;

    @FXML
    public Label labelTaskPrograms;
    @FXML
    public TableView tableViewProgramList;
    @FXML
    public TableColumn<Program, Long> columnProgramId;
    @FXML
    public TableColumn<Program, String> columnName;
    @FXML
    public TableColumn<Program, String> columnWhitelist;
    @FXML
    public TableColumn<Program, String> columnBlacklist;
    @FXML
    public TextField textFieldName;
    @FXML
    public TextField textFieldWhitelist;
    @FXML
    public TextField textFieldBlacklist;
    @FXML
    public Button buttonAddActiveProgram;
    @FXML
    public Button buttonDeleteActiveProgram;

    @FXML
    public Label labelTaskWorkedTime;
    @FXML
    public TableView tableViewWorkedTime;
    @FXML
    public TableColumn<WorkedTime, Long> columnWorkedTimeId;
    @FXML
    public TableColumn<WorkedTime, String> columnLogin;
    @FXML
    public TableColumn<WorkedTime, String> columnDate;
    @FXML
    public TableColumn<WorkedTime, String> columnTime;// косыль
    @FXML
    public TextField textFieldLogin;
    @FXML
    public DatePicker datePickerDay;
    @FXML
    public TextField textFieldTime;
    @FXML
    public Button buttonAddWorkedTime;
    @FXML
    public Button buttonDeleteWorkedTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // disable all add and delete record buttons
        buttonAddTask.setDisable(true);
        buttonDeleteTask.setDisable(true);
        buttonAddActiveProgram.setDisable(true);
        buttonDeleteActiveProgram.setDisable(true);
        buttonAddWorkedTime.setDisable(true);
        buttonDeleteWorkedTime.setDisable(true);

        // setting column value property
        columnProgramId.setCellValueFactory(cellDate -> new SimpleLongProperty(cellDate.getValue().getId()).asObject());
        columnName.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getName()));
        columnWhitelist.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getWhitelist()));
        columnBlacklist.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getBlacklist()));

        columnWorkedTimeId.setCellValueFactory(cellDate -> new SimpleLongProperty(cellDate.getValue().getId()).asObject());
        columnLogin.setCellValueFactory(cellDate -> new SimpleStringProperty(cellDate.getValue().getLogin()));
        columnDate.setCellValueFactory(cellDate -> new SimpleStringProperty(new SimpleDateFormat("dd.MM.yyy").format(cellDate.getValue().getDate())));
        columnTime.setCellValueFactory(cellDate -> new SimpleStringProperty(Integer.toString(cellDate.getValue().getTime())));

        // setting listview items and onChangeSelected event
        tableViewProgramList.setDisable(true);
        tableViewWorkedTime.setDisable(true);
        showTaskList();

        listViewTaskList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                buttonDeleteTask.setDisable(false);

                if (listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    tableViewProgramList.setDisable(false);
                    tableViewWorkedTime.setDisable(false);
                    buttonDeleteActiveProgram.setDisable(true);
                    buttonDeleteWorkedTime.setDisable(true);

                    labelTaskPrograms.setText(listViewTaskList.getSelectionModel().getSelectedItem().toString() + " programs");
                    labelTaskWorkedTime.setText(listViewTaskList.getSelectionModel().getSelectedItem().toString() + " worked time");

                    Task task = DataController.INSTANCE.getTaskByName(listViewTaskList.getSelectionModel().getSelectedItem().toString());

                    showProgramList(task);

                    showWorkedTimeList(task);
                } else {
                    tableViewProgramList.setDisable(true);
                    tableViewProgramList.setDisable(true);
                }
            }
        });

        // add task textfield check
        textFieldTaskName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (checkStringName(newValue)) {
                    buttonAddTask.setDisable(false);
                } else {
                    buttonAddTask.setDisable(true);
                }
            }
        });

        // add program textfield's checking
        textFieldName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (checkStringName(newValue) && checkStringExpression(textFieldWhitelist.getText())
                        && checkStringExpression(textFieldBlacklist.getText()) && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonAddActiveProgram.setDisable(false);
                } else {
                    buttonAddActiveProgram.setDisable(true);
                }
            }
        });

        textFieldWhitelist.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (checkStringName(textFieldName.getText()) && checkStringExpression(newValue)
                        && checkStringExpression(textFieldBlacklist.getText()) && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonAddActiveProgram.setDisable(false);
                } else {
                    buttonAddActiveProgram.setDisable(true);
                }
            }
        });

        textFieldBlacklist.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (checkStringName(textFieldName.getText()) && checkStringName(textFieldWhitelist.getText())
                        && checkStringExpression(newValue) && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonAddActiveProgram.setDisable(false);
                } else {
                    buttonAddActiveProgram.setDisable(true);
                }
            }
        });

        // add worked time textfield's and datepicker checking
        textFieldLogin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (checkStringName(newValue) && checkDate(datePickerDay.getValue()) && checkTimeValue(textFieldTime.getText())
                        && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonAddWorkedTime.setDisable(false);
                } else {
                    buttonAddWorkedTime.setDisable(true);
                }
            }
        });

        datePickerDay.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (checkStringName(textFieldLogin.getText()) && checkDate(newValue) && checkTimeValue(textFieldTime.getText())
                        && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonAddWorkedTime.setDisable(false);
                } else {
                    buttonAddWorkedTime.setDisable(true);
                }
            }
        });

        textFieldTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (checkStringName(textFieldLogin.getText()) && checkDate(datePickerDay.getValue()) && checkTimeValue(newValue)
                        && listViewTaskList.getSelectionModel().getSelectedItem() != null) {
                    buttonAddWorkedTime.setDisable(false);
                } else {
                    buttonAddWorkedTime.setDisable(true);
                }
            }
        });

        // setting program's tableview
        tableViewProgramList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    buttonDeleteActiveProgram.setDisable(false);
                }
            }
        });

        tableViewProgramList.setEditable(true);
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Program, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Program, String> event) {
                Task task = DataController.INSTANCE.getTaskByName(listViewTaskList.getSelectionModel().getSelectedItem().toString());
                Program activeProgram = (Program) tableViewProgramList.getSelectionModel().getSelectedItem();

                if (!task.getProgramNamesList().contains(event.getNewValue())) {
                    activeProgram.setName(event.getNewValue());
                    DataController.INSTANCE.updateActiveProgram(activeProgram);
                } else {
                    showProgramList(task);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Program with same name already exist");
                    alert.showAndWait();
                }

                showProgramList(task);
            }
        });

        columnWhitelist.setCellFactory(TextFieldTableCell.forTableColumn());
        columnWhitelist.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Program, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Program, String> event) {
                Program activeProgram = (Program) tableViewProgramList.getSelectionModel().getSelectedItem();

                activeProgram.setWhitelist(event.getNewValue());
                DataController.INSTANCE.updateActiveProgram(activeProgram);
            }
        });

        columnBlacklist.setCellFactory(TextFieldTableCell.forTableColumn());
        columnBlacklist.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Program, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Program, String> event) {
                Program activeProgram = (Program) tableViewProgramList.getSelectionModel().getSelectedItem();

                activeProgram.setBlacklist(event.getNewValue());
                DataController.INSTANCE.updateActiveProgram(activeProgram);
            }
        });

        // setting wrokedtime's tableview
        tableViewWorkedTime.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                buttonDeleteWorkedTime.setDisable(false);
            }
        });

        tableViewWorkedTime.setEditable(true);
        columnLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        columnLogin.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WorkedTime, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<WorkedTime, String> event) {
                Task task = DataController.INSTANCE.getTaskByName(listViewTaskList.getSelectionModel().getSelectedItem().toString());
                WorkedTime workedTime = (WorkedTime) tableViewWorkedTime.getSelectionModel().getSelectedItem();

                if (!task.validateWorkedTimeAttributes(event.getNewValue(), workedTime.getDate())) {
                    workedTime.setLogin(event.getNewValue());
                    DataController.INSTANCE.updateWorkedTime(workedTime);
                } else {
                    showWorkedTimeList(task);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("WorkedTime with same login and date already exist");
                    alert.showAndWait();
                }
            }
        });

        columnDate.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WorkedTime, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<WorkedTime, String> event) {
                Task task = DataController.INSTANCE.getTaskByName(listViewTaskList.getSelectionModel().getSelectedItem().toString());
                WorkedTime workedTime = (WorkedTime) tableViewWorkedTime.getSelectionModel().getSelectedItem();

                DateFormat format = new SimpleDateFormat("dd.MM.yyy", Locale.ENGLISH);
                Date newDate = null;
                try {
                    newDate = format.parse(event.getNewValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!task.validateWorkedTimeAttributes(workedTime.getLogin(), newDate)) {
                    workedTime.setDate(newDate);
                    DataController.INSTANCE.updateWorkedTime(workedTime);
                } else {
                    showWorkedTimeList(task);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("WorkedTime with same login and date already exist");
                    alert.showAndWait();
                }
            }
        });

        columnTime.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTime.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<WorkedTime, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<WorkedTime, String> event) {
                WorkedTime workedTime = (WorkedTime) tableViewWorkedTime.getSelectionModel().getSelectedItem();

                workedTime.setTime(Integer.parseInt(event.getNewValue()));
                DataController.INSTANCE.updateWorkedTime(workedTime);
            }
        });
    }

    private boolean checkStringName(String value) {
        return value.matches("[A-zА-яЁё][\\wА-яЁё ]+") && value.length() < 1024;
    }

    private boolean checkStringExpression(String value) {
        if (value.length() >= 0 && value.length() <= 1024) {
            return true;
        }
        return false;
    }

    private boolean checkDate(LocalDate date) {
        if (date != null) {
            if (!date.isAfter(LocalDate.now())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTimeValue(String value) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0 || intValue > 1440) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @FXML
    private void handleAddTask() {
        String taskName = textFieldTaskName.getText();
        if (!DataController.INSTANCE.getAllTaskNames().contains(taskName)) {
            Task task = new Task();
            task.setName(textFieldTaskName.getText());
            task.setProgramList(new ArrayList<>());
            task.setWorkedTimeList(new ArrayList<>());
            DataController.INSTANCE.addTask(task);
            showTaskList();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Task with same name already exist");
            alert.showAndWait();
        }
        resetTaskAdd();
    }

    @FXML
    private void handleDeleteTask() {
        String taskName = (String) listViewTaskList.getSelectionModel().getSelectedItem();
        Task task = DataController.INSTANCE.getTaskByName(taskName);

        if (task != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Permanently delete task '" + taskName + "' and all connected records from db");
            alert.setContentText("Continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DataController.INSTANCE.deleteTask(task);
            } else {
                // ... user chose CANCEL delete
            }
            showTaskList();
        }
    }

    @FXML
    private void handleAddProgram() {
        String taskName = (String) listViewTaskList.getSelectionModel().getSelectedItem();
        Task task = DataController.INSTANCE.getTaskByName(taskName);

        String programName = textFieldName.getText();
        if (!task.getProgramNamesList().contains(programName)) {
            Program program = new Program();
            program.setName(programName);
            program.setWhitelist(textFieldWhitelist.getText());
            program.setBlacklist(textFieldBlacklist.getText());
            program.setTask(task);

            DataController.INSTANCE.addActiveProgram(task, program);

            showProgramList(task);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Program with same name already exist");
            alert.showAndWait();
        }

        resetActiveProgramAdd();
    }

    @FXML
    private void handleDeleteProgram() {
        String taskName = (String) listViewTaskList.getSelectionModel().getSelectedItem();
        Task task = DataController.INSTANCE.getTaskByName(taskName);

        Program program = (Program) tableViewProgramList.getSelectionModel().getSelectedItem();
        if (task != null && program != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Permanently delete program '" + program.getName() + "' from db");
            alert.setContentText("Continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DataController.INSTANCE.deleteActiveProgram(task, program);
            } else {
                // ... user chose CANCEL delete
            }
            showProgramList(task);
        }
    }

    @FXML
    private void handleAddWorkedTime() {
        String taskName = (String) listViewTaskList.getSelectionModel().getSelectedItem();
        Task task = DataController.INSTANCE.getTaskByName(taskName);

        String login = textFieldLogin.getText();
        Date date = Date.from(datePickerDay.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (!task.validateWorkedTimeAttributes(login, date)) {
            WorkedTime workedTime = new WorkedTime();
            workedTime.setLogin(login);
            workedTime.setDate(date);
            workedTime.setTime(Integer.parseInt(textFieldTime.getText()));
            workedTime.setTask(task);

            DataController.INSTANCE.addWorkedTime(task, workedTime);

            showWorkedTimeList(task);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("WorkedTime with same login and date already exist");
            alert.showAndWait();
        }

        resetWorkedTimeAdd();
    }

    @FXML
    private void handleDeleteWorkedTime() {
        String taskName = (String) listViewTaskList.getSelectionModel().getSelectedItem();
        Task task = DataController.INSTANCE.getTaskByName(taskName);

        WorkedTime workedTime = (WorkedTime) tableViewWorkedTime.getSelectionModel().getSelectedItem();
        if (task != null && workedTime != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Permanently delete worked time for user '" + workedTime.getLogin() + "' for " +
                    new SimpleDateFormat("dd.MM.yyy").format(workedTime.getDate()) + " from db");
            alert.setContentText("Continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DataController.INSTANCE.deleteWorkedTime(task, workedTime);
            } else {
                // ... user chose CANCEL delete
            }
            showWorkedTimeList(task);
        }
    }

    private void showTaskList() {
        listViewTaskList.getItems().clear();
        listViewTaskList.setItems(FXCollections.observableList(DataController.INSTANCE.getAllTaskNames()));
        tableViewProgramList.setDisable(true);
        tableViewWorkedTime.setDisable(true);
    }

    private void showProgramList(Task task) {
        tableViewProgramList.getItems().clear();
        if (task.getProgramList() != null) {
            tableViewProgramList.getItems().addAll(task.getProgramList());
        }
    }

    private void showWorkedTimeList(Task task) {
        tableViewWorkedTime.getItems().clear();
        if (task.getProgramList() != null) {
            tableViewWorkedTime.getItems().addAll(task.getWorkedTimeList());
        }
    }

    private void resetTaskAdd() {
        buttonAddTask.setDisable(true);
        textFieldTaskName.clear();
    }

    private void resetActiveProgramAdd() {
        buttonAddActiveProgram.setDisable(true);
        textFieldName.clear();
        textFieldWhitelist.clear();
        textFieldBlacklist.clear();
    }

    private void resetWorkedTimeAdd() {
        buttonAddWorkedTime.setDisable(true);
        textFieldLogin.clear();
        datePickerDay.getEditor().clear();
        textFieldTime.clear();
    }
}
