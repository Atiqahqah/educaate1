package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import sg.edu.np.educaate1.Activity.BookingList;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentApptFragment extends Fragment {


    public StudentApptFragment() {
        // Required empty public constructor
    }

    ArrayAdapter<Booking> adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_appt, container, false);
        Button button = (Button) view.findViewById(R.id.bookingListBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BookingList.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
