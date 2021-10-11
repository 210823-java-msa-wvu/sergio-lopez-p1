package dev.lopez.models;
import java.sql.Date;

public class Notification {

    //Private data variables
    private int id, formID, checked;
    private String content;
    private Date time;


    public Notification() {
        super();
    }


    public Notification(int id, int formID, String content, Date time, int checked) {
        super();
        this.id = id;
        this.formID = formID;
        this.content = content;
        this.time = time;
        this.checked = checked;
    }

    //Getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFormID() {
        return formID;
    }

    public void setFormID(int formID) {
        this.formID = formID;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Notification [id=" + id + ", formID=" + formID + ", content=" + content + ", time=" + time
                + ", checked=" + checked + "]";
    }
}