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

import sg.edu.np.educaate1.Activity.PostSchedule;
import sg.edu.np.educaate1.Activity.StudentPostSchedule;
import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.DeleteBooking;
import sg.edu.np.educaate1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentApptFragment extends Fragment {

    ListView listView;
    ListView listView2;
    ArrayList<Booking> pendingBookingList;
    ArrayList<Booking> postedBookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
    ArrayAdapter<Booking> adapter2;
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
        pendingBookingList=new ArrayList<>();
        postedBookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingBookingList.clear();
                postedBookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.child("booking").getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("Tutor")){
                        Log.d(TAG, snapshot.getKey());
                        Booking booking=snapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                        pendingBookingList.add(booking);
                    }
                    else{
                        Booking booking=snapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                        postedBookingList.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();//important line!!
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(getActivity(),R.layout.bookinglayout,pendingBookingList);
        listView=(ListView)view.findViewById(R.id.studentChooseList);
        listView.setAdapter(adapter);

        adapter2=new BookingAdapter(getActivity(),R.layout.bookinglayout,postedBookingList);
        listView2=(ListView)view.findViewById(R.id.studentPosted);
        listView2.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Booking b = (Booking) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),
                        DeleteBooking.class);

                intent.putExtra("name",b.getName());
                intent.putExtra("date",b.getDate());
                intent.putExtra("time",b.getTime());
                intent.putExtra("desc",b.getDesc());
                intent.putExtra("location",b.getLocation());
                intent.putExtra("price",b.getPrice());
                intent.putExtra("subj",b.getSubject());
                intent.putExtra("id",b.getId());
                intent.putExtra("type",b.getType());
                intent.putExtra("status",b.getStatus());
                /*SharedPreferences.Editor editor=pref.edit();
                editor.putString("DATE",b.getDate());
                editor.apply();*/

                startActivity(intent);
            }
        });

        Button button = (Button) view.findViewById(R.id.postSBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), StudentPostSchedule.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
