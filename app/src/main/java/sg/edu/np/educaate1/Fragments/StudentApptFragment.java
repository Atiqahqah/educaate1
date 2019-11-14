package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentApptFragment extends Fragment {

    ListView listView;
    ArrayList<Booking> bookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
    String TAG;
    SharedPreferences pref;
    String name;

    FirebaseAuth mAuth;


    public StudentApptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_student_appt, container, false);
        /*Button button = (Button) view.findViewById(R.id.bookingListBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),BookingList.class);
                startActivity(intent);
            }
        });*/
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.child("booking").getChildren()){
                            Log.d(TAG, snapshot.getKey());
                            Booking booking=snapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                            bookingList.add(booking);
                            adapter.notifyDataSetChanged();//important line!!
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(getActivity(),R.layout.bookinglayout,bookingList);
        listView=(ListView)view.findViewById(R.id.studentChooseList);
        listView.setAdapter(adapter);


        return view;
    }
}
