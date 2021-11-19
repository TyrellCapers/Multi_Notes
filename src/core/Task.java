package core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {
    private StringProperty name;
    private StringProperty status;
    private String notes;

    public Task(){
        this.name = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
    }

    public Task(String name, String status){
        this.name = new SimpleStringProperty();
        this.status = new SimpleStringProperty();

        setName(name);
        setStatus(status);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getName(){
        return this.name.get();
    }

    public void setStatus(String status){
        this.status.set(status);
    }

    public String getStatus(){
        return this.status.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void increaseStatus(){
        switch(this.getStatus()){
            case "Not Started":
                this.setStatus("In Progress");
                break;
            case "In Progress":
                this.setStatus("Completed");
                break;
            case "Completed":
                break;
            default:
                this.setStatus("Not Started");
                break;

        }
    }

    public void decreaseStatus(){
        switch(this.getStatus()){
            case "Not Started":
                break;
            case "In Progress":
                this.setStatus("Not Started");
                break;
            case "Completed":
                this.setStatus("In Progress");
                break;
            default:
                this.setStatus("Not Started");
                break;

        }
    }
}
