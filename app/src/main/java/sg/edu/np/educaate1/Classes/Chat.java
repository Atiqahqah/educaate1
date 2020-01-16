package sg.edu.np.educaate1.Classes;

import java.util.Date;

public class Chat {
    private String id;
    private String studentID;
    private String tutorID;
    private String bookingType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chat(){}

    public Chat(String id,String studentID,String tutorID,String bookingType){
        this.id=id;
        this.studentID=studentID;
        this.tutorID=tutorID;
        this.bookingType=bookingType;
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

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }
}
