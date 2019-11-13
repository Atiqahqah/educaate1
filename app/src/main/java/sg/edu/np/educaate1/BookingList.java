package sg.edu.np.educaate1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class BookingList extends AppCompatActivity {
    ListView listView;
    ArrayList<Booking> bookingList;
    DatabaseReference databaseReference;
    ArrayAdapter<Booking> adapter;
    String TAG;
    SharedPreferences pref;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                        //long no=msgSnapshot.getChildrenCount();
                        //Log.d(TAG, Objects.toString(no));
                        /*String subj=msgSnapshot.child("subject").getValue().toString();
                        String date=msgSnapshot.child("date").getValue().toString();
                        String time=msgSnapshot.child("time").getValue().toString();
                        String price=msgSnapshot.child("price").getValue().toString();
                        String location=msgSnapshot.child("location").getValue().toString();
                        String desc=msgSnapshot.child("desc").getValue().toString();
                        Booking booking=new Booking(subj,date,time,price,location,desc);*/
                        //Log.d(TAG,msgSnapshot.getKey()); //get name of child

                        Booking booking=msgSnapshot.getValue(Booking.class); //write this to make codes simple and make app load faster
                        bookingList.add(booking);
                        adapter.notifyDataSetChanged();//important line!!
                    }
                    //long no=snapshot.getChildrenCount();
                    //Log.d(TAG, Objects.toString(no));

                    /*String subj=snapshot.child("subject").getValue().toString();
                    String date=snapshot.child("date").getValue().toString();
                    String time=snapshot.child("time").getValue().toString();
                    String price=snapshot.child("price").getValue().toString();
                    String location=snapshot.child("location").getValue().toString();
                    String desc=snapshot.child("desc").getValue().toString();
                    Booking booking=new Booking(subj,date,time,price,location,desc);*/

                    /*bookingList.add(booking);
                    Log.d(TAG, "data added");
                    adapter.notifyDataSetChanged();*/  //important line!!!!

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new BookingAdapter(this,R.layout.bookinglayout,bookingList);//The data will displayed in the ListView following the layout in goallayout
        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Booking b = (Booking) parent.getItemAtPosition(position);
                Intent intent = new Intent(BookingList.this,
                        ViewSchedule.class);//open view goal page

                //get name of goal so viewgoal will display information about that goal
                intent.putExtra("name",b.getName());
                intent.putExtra("date",b.getDate());
                intent.putExtra("time",b.getTime());
                intent.putExtra("desc",b.getDesc());
                intent.putExtra("location",b.getLocation());
                intent.putExtra("price",b.getPrice());
                intent.putExtra("subj",b.getSubject());
                /*SharedPreferences.Editor editor=pref.edit();
                editor.putString("DATE",b.getDate());
                editor.apply();*/

                startActivity(intent);
            }
        });


        /*Button update = findViewById(R.id.btn2);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingList.this, StudentUpdate.class));
            }
        });*/

    }

    public void onClick(View v){
        startActivity(new Intent(BookingList.this, StudentUpdate.class));

    }
}
