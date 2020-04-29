package model;

/**
 * Created by Turnyur Siy on 11/04/2017.
 */


public class DataObject {
    public String taskTitle;
    public String taskSubject;
    public String taskBody;
    public String taskDate;
    public String taskTime;
    public  int id;


    public DataObject(String title, String subject, String body, String date,String time,int id) {
        this.id=id;
        taskTitle = title;
        taskSubject = subject;
        taskBody = body;
        taskDate = date;
        taskTime = time;

    }

    public String getmTitle() {
        return taskTitle;
    }

    public void setmTitle(String mText) {
        this.taskTitle = mText;
    }

    public String getmSubject() {
        return taskSubject;
    }

    public void setmSubject(String mText) {
        this.taskSubject = mText;
    }

    public String getmBody() {
        return taskBody;
    }

    public void setmBody(String mText) {
        this.taskBody = mText;
    }

    public String getmDate() {
        return taskDate;
    }

    public void setmDate(String mText) {
        this.taskDate = mText;
    }

    public String getmTime() {
        return taskTime;
    }

    public void setmTime(String mText) {
        this.taskTime = mText;
    }

}
