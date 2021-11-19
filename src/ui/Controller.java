package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import core.Task;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import persistence.XMLPersistence;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Controller {

    //Class Fields======================================================
    private Task currentTask = null;

    //FXML Fields=======================================================

    @FXML
    private TableView taskTable;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn statusColumn;

    @FXML
    private ListView actionList;

    @FXML
    private Button executeActionButton;

    @FXML
    private TextField actionTextField;

    @FXML
    private TextArea notesTextArea;

    //FXML Initialize===============================================
    @FXML
    public void initialize(){
        initializeActionList();
        initializeTableColumns();
    }

    //FXML Actions==================================================

    public void taskTableOnMouseClicked(MouseEvent mouseEvent) {
        //Change Tasks
        changeTask(retrieveCurrentTask(), retrieveSelectedTask());
    }

    public void executeAction(ActionEvent actionEvent) {
        String actionListItem = (String) actionList.getSelectionModel().getSelectedItem();
        Task task = null;
        switch(actionListItem){
            case "Add Task":
                String name = retrieveText();
                addTask(newTask(name));
                break;
            case "Remove Task":
                task = retrieveSelectedTask();
                if(task == currentTask){
                    currentTask = null;
                }
                taskTable.getItems().remove(task);
                break;
            case "Edit Name":
                task = retrieveSelectedTask();
                task.setName(retrieveText());
                break;
            case "Increase Status":
                task = retrieveSelectedTask();
                task.increaseStatus();
                break;
            case "Decrease Status":
                task = retrieveSelectedTask();
                task.decreaseStatus();
                break;
            case "Archive Tasks":
                archiveTasks();
                break;
            default:
                break;
        }
    }

    public void saveXML(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showSaveDialog(null);

        XMLPersistence.save((Task[]) taskTable.getItems().toArray(new Task[0]), selectedFile.getAbsolutePath());


    }

    public void loadXML(ActionEvent actionEvent){

        //Load file in file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load XML...");
        fileChooser.getExtensionFilters().add(
          new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );

        File selectedFile = fileChooser.showOpenDialog(null);


        //Load files
        Task[] tasks = XMLPersistence.load(selectedFile.getAbsolutePath());

        //Add tasks to task table
        taskTable.getItems().addAll(tasks);

    }

    //Initializers==================================================
    public void initializeActionList(){
        actionList.getItems().add("Add Task");
        actionList.getItems().add("Remove Task");
        actionList.getItems().add("Edit Name");
        actionList.getItems().add("Increase Status");
        actionList.getItems().add("Decrease Status");
        actionList.getItems().add("Archive Tasks");
    }

    public void initializeTableColumns(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
    }


    //Actions=======================================================
    public void addTask(Task task){
        taskTable.getItems().add(task);
    }

    public String retrieveText(){
        String content = actionTextField.getText();
        actionTextField.clear();
        return content;
    }

    public Task newTask(String name){
        Task task = new Task(name, "Not Started");
        return task;
    }

    public void changeTask(Task oldTask, Task newTask){
        //Notes Change
        if(oldTask != null) {
            oldTask.setNotes(notesTextArea.getText());
        }
        notesTextArea.setText(newTask.getNotes());

        //Change current task
        currentTask = newTask;

    }

    public Task retrieveCurrentTask(){
        return this.currentTask;
    }

    public Task retrieveSelectedTask(){
        return (Task) taskTable.getSelectionModel().getSelectedItem();
    }

    public void archiveTasks(){
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Archive Directory...");
        File archiveDir = dirChooser.showDialog(null);
        Date currentDate = Calendar.getInstance().getTime();

        File datedDir = new File(archiveDir.getAbsolutePath() + "\\" +
                new SimpleDateFormat("MM-dd-yyyy").format(currentDate));
        if(!datedDir.exists()){
            datedDir.mkdir();
        }

        for(Task task : (Task[]) taskTable.getItems().toArray(new Task[0])){
            if(task.getStatus().equals("Completed")){
                File newFile = new File(datedDir.getAbsolutePath() + "\\" + task.getName() + ".txt");
                try {
                    PrintWriter pw = new PrintWriter(newFile);
                    pw.write(task.getNotes());
                    pw.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }



    }

}
