package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.edu.np.educaate1.Activity.PostSchedule;
import sg.edu.np.educaate1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorHomeFragment extends Fragment {


    public TutorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_home, container, false);

        Button button = (Button) view.findViewById(R.id.postBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PostSchedule.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
