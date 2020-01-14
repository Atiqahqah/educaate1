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
    private DatabaseReference databaseReferenceS;


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    private String uid;
    Booking b;
    ArrayList<String> studentIDList;
    private String currStudentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_message);

        Intent i=getIntent();
        final String bookingID=i.getStringExtra("id");
        Log.d("test BID",bookingID);
        final String studentEmail=i.getStringExtra("email");

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        uid = sharedPreferences.getString(UID, null);
        Log.d("UID test",uid);

        //find booking details of this booking
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

                            b=snapshot.getValue(Booking.class);
                            Log.d("booking ID b",b.getId());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //get all UID of student that have the booking ID
        databaseReferenceS= FirebaseDatabase.getInstance().getReference().child("users");
        studentIDList=new ArrayList<>();
        databaseReferenceS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentIDList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("student")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            if(bookingID.equals(msgSnapshot.getKey())){
                                Student s=snapshot.getValue(Student.class);
                                studentIDList.add(snapshot.getKey());
                                Log.d("Student ID",snapshot.getKey());
                            }
                        }
                        if(snapshot.child("email").getValue().toString().equals(studentEmail)){
                            currStudentID = snapshot.getKey();
                            Log.d("current Student ID",currStudentID);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        //edit booking status under tutor to close
        Booking booking=new Booking();

        booking.setDate(b.getDate());
        booking.setTime(b.getTime());
        booking.setSubject(b.getSubject());
        booking.setDesc(b.getDesc());
        booking.setPrice(b.getPrice());
        booking.setLocation(b.getLocation());
        booking.setName(b.getName());
        booking.setId(b.getId());
        booking.setType(b.getType());
        booking.setStatus("Close");

        databaseReference.child("users").child(uid).child("booking").child(b.getId()).setValue(booking);


        //edit booking status under selected student to confirm
        Booking bookingConfirm=new Booking();

        bookingConfirm.setDate(b.getDate());
        bookingConfirm.setTime(b.getTime());
        bookingConfirm.setSubject(b.getSubject());
        bookingConfirm.setDesc(b.getDesc());
        bookingConfirm.setPrice(b.getPrice());
        bookingConfirm.setLocation(b.getLocation());
        bookingConfirm.setName(b.getName());
        bookingConfirm.setId(b.getId());
        bookingConfirm.setType(b.getType());
        bookingConfirm.setStatus("Confirm");

        //edit booking status under other student to cancel
        Booking bookingCancel=new Booking();

        bookingCancel.setDate(b.getDate());
        bookingCancel.setTime(b.getTime());
        bookingCancel.setSubject(b.getSubject());
        bookingCancel.setDesc(b.getDesc());
        bookingCancel.setPrice(b.getPrice());
        bookingCancel.setLocation(b.getLocation());
        bookingCancel.setName(b.getName());
        bookingCancel.setId(b.getId());
        bookingCancel.setType(b.getType());
        bookingCancel.setStatus("Cancel");

        if(studentIDList.size()!=0){
            for(int i=0;i<studentIDList.size();i++){
                if(studentIDList.get(i) == currStudentID){
                    databaseReference.child("users").child(currStudentID).child("booking").child(b.getId()).setValue(bookingConfirm);
                }
                else {
                    databaseReference.child("users").child(studentIDList.get(i)).child("booking").child(b.getId()).setValue(bookingCancel);
                }
            }
        }
    }
}
