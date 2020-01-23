package sg.edu.np.educaate1.Classes;

public class Rating {
    private String ReviewId;
    private String userID;
    private String Desc;
    private int Score;

    public Rating(String id, String uid, String d, int s){
        userID = uid;
        Desc = d;
        Score = s;
        ReviewId = id;
    }

    public  String getReviewId(){return ReviewId;}

    public int getScore() {
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

    public void setScore(int score) {
        Score = score;
    }
}
