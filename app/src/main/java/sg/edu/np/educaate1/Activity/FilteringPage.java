package sg.edu.np.educaate1.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

public class FilteringPage extends AppCompatActivity {
    ArrayList<String> subjects;
    ListView listView;
    //ArrayList<Booking> displayBookingList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ArrayAdapter<Booking> adapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering_page);

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        final List<Booking> displayBookingList=new ArrayList<>();
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

        /*databaseReference2= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        for(int i=0;i<displayBookingList.size();i++){
            //subjects.add(displayBookingList.get(i).getSubject());
        }

        Spinner mySpinner=(Spinner)findViewById(R.id.subjFilter);
        ArrayAdapter<Booking>adapter=new ArrayAdapter<Booking>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,displayBookingList);
        //ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.subjects,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

    }
}
