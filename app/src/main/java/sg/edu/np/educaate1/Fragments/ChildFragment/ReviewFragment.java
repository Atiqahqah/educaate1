package sg.edu.np.educaate1.Fragments.ChildFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sg.edu.np.educaate1.Adapters.RatingsAdapter;
import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {
    ArrayList<Rating> ratingList = new ArrayList<>();

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_review, container, false);
        RecyclerView ratingView = v.findViewById(R.id.ratingRV);
        ratingView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RatingsAdapter ratingsAdapter = new RatingsAdapter(getActivity(),ratingList);
        ratingView.setAdapter(ratingsAdapter);
        return v ;
    }

}
