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
import sg.edu.np.educaate1.Activity.ViewSchedule;
import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentHomeFragment extends Fragment {

    ListView listView;
    ArrayList<Booking> bookingList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ArrayAdapter<Booking> adapter;
    String TAG;
    SharedPreferences pref;
    String name;

    FirebaseAuth mAuth;


    public StudentHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("tutor")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            Log.d(TAG, msgSnapshot.getKey());
                            Log.d("Status", msgSnapshot.child("status").getValue().toString());

                            if(msgSnapshot.child("status").getValue().toString().equals("Open") && msgSnapshot.child("type").getValue().toString().equals("Tutor"))
                            {
                                Booking booking=msgSnapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                                bookingList.add(booking);
                                //adapter.notifyDataSetChanged();//important line!!
                            }
                        }
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
                    if(userSnapshot.child("type").getValue().toString().equals("Tutor")){
                        for (int i=0;i<bookingList.size();i++){
                            if(userSnapshot.child("id").getValue().toString().equals(bookingList.get(i).getId())){
                                bookingList.remove(bookingList.get(i));
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
