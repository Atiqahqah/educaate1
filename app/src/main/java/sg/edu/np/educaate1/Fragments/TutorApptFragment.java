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
    ArrayList<Booking> bookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
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
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.child("booking").getChildren()){
                    Log.d(TAG,Long.toString(snapshot.getChildrenCount()));
                    //Log.d(TAG,Long.toString(msgSnapshot.getChildrenCount()));
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
        listView=(ListView)view.findViewById(R.id.tutorApptList);
        listView.setAdapter(adapter);

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
                /*SharedPreferences.Editor editor=pref.edit();
                editor.putString("DATE",b.getDate());
                editor.apply();*/

                startActivity(intent);
            }
        });

        return view;
    }
}
