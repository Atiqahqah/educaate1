package sg.edu.np.educaate1.Classes;

public class Rating {
    private String Name;
    private String Desc;
    private int Score;

    public Rating(String n, String d, int s){
        Name = n;
        Desc = d;
        Score = s;
    }

    public int getScore() {
        return Score;
    }

    public String getName() {
        return Name;
    }

    public String getDesc() {
        return Desc;
    }

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
