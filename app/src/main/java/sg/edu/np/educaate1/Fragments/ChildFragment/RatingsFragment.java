package sg.edu.np.educaate1.Fragments.ChildFragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingsFragment extends Fragment {
    TextView ratings;
    ArrayList<Rating> ratingList;
    int TotalScore;
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
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Gson gson =new Gson();
        String json = pref.getString("review","");
        Type type = new TypeToken<ArrayList<Rating>>() {}.getType();
        if(json != ""){
            ratingList = gson.fromJson(json,type);
        }
        for (i = 0; i <= ratingList.size(); i ++){
            int score = ratingList.get(i).getScore();
            TotalScore += score;
        }
        ratings.setText(new Integer(TotalScore/i).toString());
        return v;

    }

}
