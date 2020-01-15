package sg.edu.np.educaate1.Fragments.ChildFragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sg.edu.np.educaate1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorSummaryFragment extends Fragment {
    //Views
    TextView Email;
    TextView Phone;
    TextView Age;
    TextView Gender;
    TextView Edu;
    TextView Qual;
    TextView Desc;

    public TutorSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_summary, container, false);
        InitialiseView(view);
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Email.setText(pref.getString("tutor email","lol"));
        Age.setText(pref.getString("tutor age",""));
        Gender.setText(pref.getString("tutor gender",""));
        Phone.setText(pref.getString("tutor phone",""));
        Edu.setText(pref.getString("tutor edu",""));
        Qual.setText(pref.getString("tutor qual",""));
        Desc.setText(pref.getString("tutor desc",""));
        return view;
    }

    public void InitialiseView(View v){
        Email = v.findViewById(R.id.tutorProfileEmailtv);
        Age = v.findViewById(R.id.tutorProfileAgetv);
        Gender = v.findViewById(R.id.tutorProfileGendertv);
        Phone = v.findViewById(R.id.tutorProfilePhonetv);
        Edu = v.findViewById(R.id.tutorProfileEdutv);
        Qual = v.findViewById(R.id.tutorProfileQualitv);
        Desc = v.findViewById(R.id.tutorProfileDesctv);
    }

}
