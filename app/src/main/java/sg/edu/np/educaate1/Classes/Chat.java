package sg.edu.np.educaate1.Classes;

import java.util.Date;

public class Chat {
    private String id;
    private String studentID;
    private String tutorID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chat(){}

    public Chat(String id,String studentID,String tutorID){
        this.id=id;
        this.studentID=studentID;
        this.tutorID=tutorID;
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
