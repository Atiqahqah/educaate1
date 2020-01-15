package sg.edu.np.educaate1.Classes;

public class Booking {
    private String time;
    private String date;
    private String subject;
    private String price;
    private String desc;
    private String location;
    private String name;
    private String id;
    private String status;
    private String type;
    private String tutorid;

    public Booking(){ }

    public String getTutorid() {
        return tutorid;
    }

    public void setTutorid(String tutorid) {
        this.tutorid = tutorid;
    }

    public Booking(String name, String subject, String date, String time, String price, String location, String desc, String id, String status, String type, String tutorid){
        this.time=time;
        this.date=date;
        this.subject=subject;
        this.price=price;
        this.location=location;
        this.desc=desc;
        this.name=name;
        this.id=id;
        this.status=status;
        this.type=type;
        this.tutorid=tutorid;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) { this.time = time; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
