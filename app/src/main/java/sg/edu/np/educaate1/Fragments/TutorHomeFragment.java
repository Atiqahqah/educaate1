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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Activity.PostSchedule;
import sg.edu.np.educaate1.Activity.ViewSchedule;
import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorHomeFragment extends Fragment {
    ListView listView;
    ArrayList<Booking> bookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
    String TAG;
    SharedPreferences pref;
    String name;

    FirebaseAuth mAuth;

    public TutorHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_home, container, false);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("student")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            Log.d(TAG, msgSnapshot.getKey());
                            Log.d("Status", msgSnapshot.child("status").getValue().toString());
                            if(msgSnapshot.child("status").getValue().toString().equals("Open") && msgSnapshot.child("type").getValue().toString().equals("Student"))
                            {
                                Booking booking=msgSnapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                                bookingList.add(booking);
                                adapter.notifyDataSetChanged();//important line!!
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(getActivity(),R.layout.bookinglayout,bookingList);
        listView=(ListView)view.findViewById(R.id.studentListV);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Booking b = (Booking) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),
                        ViewSchedule.class);

                intent.putExtra("name",b.getName());
                intent.putExtra("date",b.getDate());
                intent.putExtra("time",b.getTime());
                intent.putExtra("desc",b.getDesc());
                intent.putExtra("location",b.getLocation());
                intent.putExtra("price",b.getPrice());
                intent.putExtra("subj",b.getSubject());
                intent.putExtra("id",b.getId());

                startActivity(intent);
            }
        });

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
