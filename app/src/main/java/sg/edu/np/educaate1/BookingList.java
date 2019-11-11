package sg.edu.np.educaate1;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingList extends AppCompatActivity {
    ListView listView;
    ArrayList<Booking> bookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("booking");
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String subj=snapshot.child("subject").getValue().toString();
                    String date=snapshot.child("date").getValue().toString();
                    String time=snapshot.child("time").getValue().toString();
                    String price=snapshot.child("price").getValue().toString();
                    String location=snapshot.child("location").getValue().toString();
                    String desc=snapshot.child("desc").getValue().toString();
                    Booking booking=new Booking(subj,date,time,price,location,desc);

                    bookingList.add(booking);
                    Log.d(TAG, "data added");
                    adapter.notifyDataSetChanged();//important line!!!!
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(this,R.layout.bookinglayout,bookingList);//The data will displayed in the ListView following the layout in goallayout
        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
