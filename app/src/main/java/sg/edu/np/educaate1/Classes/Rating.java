package sg.edu.np.educaate1.Classes;

public class Rating {
    private String ReviewId;
    private String Name;
    private String Desc;
    private int Score;

    public Rating(String id, String n, String d, int s){
        Name = n;
        Desc = d;
        Score = s;
        ReviewId = id;
    }

    public  String getReviewId(){return ReviewId;}

    public int getScore() {
        return Score;
    }

    public String getName() {
        return Name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setReviewId(String id){ReviewId = id; }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setScore(int score) {
        Score = score;
    }
}
