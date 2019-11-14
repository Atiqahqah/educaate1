package sg.edu.np.educaate1.Classes;

public class Booking {
    private String time;
    private String date;
    private String subject;
    private String price;
    private String desc;
    private String location;
    private String name;

    public Booking(){ }

    public Booking(String name,String subject,String date,String time,String price,String location,String desc){
        this.time=time;
        this.date=date;
        this.subject=subject;
        this.price=price;
        this.location=location;
        this.desc=desc;
        this.name=name;
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
}
