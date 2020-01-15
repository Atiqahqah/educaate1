package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.DeleteBooking;
import sg.edu.np.educaate1.R;
import sg.edu.np.educaate1.TutorViewSchedule;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorApptFragment extends Fragment {

    public TutorApptFragment() {
        // Required empty public constructor
    }

    ListView listView;
    ListView listView2;

    ArrayList<Booking> postedBookingList;
    ArrayList<Booking> pendingBookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
    ArrayAdapter<Booking> adapter2;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tutor_appt, container, false);
        //return inflater.inflate(R.layout.fragment_tutor_appt, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        postedBookingList=new ArrayList<>();
        pendingBookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postedBookingList.clear();
                pendingBookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.child("booking").getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("Student")){
                        Booking booking=snapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                        pendingBookingList.add(booking);
                    }
                    else{
                        Booking booking=snapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                        postedBookingList.add(booking);
                    }
                    //Log.d(TAG,Long.toString(snapshot.getChildrenCount()));
                    //Log.d(TAG,Long.toString(msgSnapshot.getChildrenCount()));
                    //Log.d(TAG, snapshot.getKey());
                    adapter.notifyDataSetChanged();//important line!!
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(getActivity(),R.layout.bookinglayout,postedBookingList);
        listView=(ListView)view.findViewById(R.id.tutorApptList);
        listView.setAdapter(adapter);

        adapter2=new BookingAdapter(getActivity(),R.layout.bookinglayout,pendingBookingList);
        listView2=(ListView)view.findViewById(R.id.tutorPending);
        listView2.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Booking b = (Booking) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),
                        TutorViewSchedule.class);

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

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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

        return view;
    }
}
