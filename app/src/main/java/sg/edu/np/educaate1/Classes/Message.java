package sg.edu.np.educaate1.Classes;

import java.util.Date;

public class Message {
    private String msg;
    private Date dateTime;
    private String id;
    private String studentID;
    private String tutorID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Message(){}

    public Message(String msg,Date dateTime,String id,String studentID,String tutorID){
        this.msg=msg;
        this.dateTime=dateTime;
        this.id=id;
        this.studentID=studentID;
        this.tutorID=tutorID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTutorID() {
        return tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }
}
