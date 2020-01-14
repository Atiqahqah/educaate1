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
import sg.edu.np.educaate1.Activity.ViewSchedule;
import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorHomeFragment extends Fragment {
    ListView listView;
    ArrayList<Booking> displayBookingList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
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

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        displayBookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayBookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("student")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            //Log.d(TAG, msgSnapshot.getKey());
                            if(msgSnapshot.child("status").getValue().toString().equals("Open") && msgSnapshot.child("type").getValue().toString().equals("Student"))
                            {
                                Booking booking=msgSnapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                                displayBookingList.add(booking);
                            }
                        }
                        Log.d("display", Integer.toString(displayBookingList.size()));
                        //Log.d("booking", Integer.toString(bookingList.size()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference2= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot:dataSnapshot.child("booking").getChildren()){
                    //Log.d(TAG, userSnapshot.getKey());
                    if(userSnapshot.child("type").getValue().toString().equals("Student")){
                        for (int i=0;i<displayBookingList.size();i++){
                            if(userSnapshot.child("id").getValue().toString().equals(displayBookingList.get(i).getId())){
                                displayBookingList.remove(displayBookingList.get(i));
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(getActivity(),R.layout.bookinglayout,displayBookingList);
        listView=(ListView)view.findViewById(R.id.tutorhome);
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
                intent.putExtra("status",b.getStatus());
                intent.putExtra("type",b.getType());

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
