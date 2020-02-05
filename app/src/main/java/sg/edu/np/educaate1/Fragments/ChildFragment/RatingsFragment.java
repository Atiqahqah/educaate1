package sg.edu.np.educaate1.Fragments.ChildFragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingsFragment extends Fragment {
    ProgressBar pgb;
    TextView ratings;
    ArrayList<Rating> ratingList;
    double TotalScore;
    String AvgScore;
    double avgScoreDouble;
    double ratingListSize;
    double percentage;
    int i;
    public RatingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ratings, container, false);
        ratings = v.findViewById(R.id.ratingScoreTV);
        pgb = v.findViewById(R.id.ratingProgressBar);
        ratingList = new ArrayList<>();
        ratingList.clear();

        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Gson gson =new Gson();
        String json = pref.getString("review","");
        Type type = new TypeToken<ArrayList<Rating>>() {}.getType();
        if(json != ""){
            ratingList = gson.fromJson(json,type);
        }
        if(ratingList.size() != 0){
            for (i = 0; i < ratingList.size(); i ++) {
                double score = ratingList.get(i).getScore();
                Log.d("score", Double.toString(score));
                TotalScore += score;
            }

            ratingListSize = Double.valueOf(ratingList.size());
            avgScoreDouble = TotalScore/ratingListSize;
            DecimalFormat df = new DecimalFormat("#.#");
            AvgScore = df.format(avgScoreDouble);
            Log.d("avg score", AvgScore);

            percentage = (TotalScore/ratingListSize)/5*100;
            ratings.setText(AvgScore);
            pgb.setProgress((int)percentage);
        }
        else{
            ratings.setText("0");
            pgb.setProgress(0);
        }
        return v;

    }

}
