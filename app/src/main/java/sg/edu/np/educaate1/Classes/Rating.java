package sg.edu.np.educaate1.Classes;

public class Rating {
    private String ReviewId;
    private String userID;
    private String Desc;
    private double Score;

    public Rating(String id, String uid, String d, double s){
        userID = uid;
        Desc = d;
        Score = s;
        ReviewId = id;
    }

    public  String getReviewId(){return ReviewId;}

    public double getScore() {
        return Score;
    }

    public String getUserID() {
        return userID;
    }

    public String getDesc() {
        return Desc;
    }

    public void setReviewId(String id){ReviewId = id; }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setUserID(String userid) { userID = userid; }

    public void setScore(double score) {
        Score = score;
    }
}
