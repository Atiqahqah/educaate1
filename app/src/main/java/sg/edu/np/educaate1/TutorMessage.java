package sg.edu.np.educaate1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.Classes.Student;

public class TutorMessage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    private String uid;
    Booking b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_message);

        Intent i=getIntent();
        final String bookingID=i.getStringExtra("id");
        Log.d("test BID",bookingID);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        uid = sharedPreferences.getString(UID, null);
        Log.d("UID test",uid);

        if(uid != null){
            Log.d("long","not null uid ");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("booking");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null)
                    {
                        Log.d("long","not null datasnapshot ");

                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Log.d("long",Long.toString(snapshot.getChildrenCount()));
                            Log.d("booking ID i got",snapshot.child("id").getValue().toString());

                            if(snapshot.child("id").getValue().toString().equals(bookingID))
                            {
                                b=snapshot.getValue(Booking.class);
                                Log.d("booking ID b",b.getId());
                            }
                            else {
                                Log.d("booking ID b","no match");
                            }
                        }
                    }
                    else {
                        Log.d("long","error, null object ");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Log.d("long","error, null uid ");
        }
    }

    public void send(View v){
        EditText text=findViewById(R.id.editText);
        TextView sentmsg=findViewById(R.id.textView8);
        String msg=text.getText().toString();
        if(msg!=null){
            sentmsg.setText("success");
        }
        else{
            sentmsg.setText("write a message");
        }
    }

    public void cancel(View v){
        Intent intent=new Intent(TutorMessage.this,TutorViewSchedule.class);
        startActivity(intent);
    }

    public void confirm(View v){


        databaseReference = FirebaseDatabase.getInstance().getReference();
        //FirebaseUser user = mAuth.getCurrentUser();
        //add user type into database
        databaseReference= FirebaseDatabase.getInstance().getReference();

        Booking booking=new Booking();

        booking.setDate(b.getDate());
        booking.setTime(b.getTime());
        booking.setSubject(b.getSubject());
        booking.setDesc(b.getDesc());
        booking.setPrice(b.getPrice());
        booking.setLocation(b.getLocation());
        booking.setName(b.getName());
        booking.setId(b.getId());
        booking.setStatus("Close");

        databaseReference.child("users").child(uid).child("booking").child(b.getId()).setValue(booking);
    }
}
