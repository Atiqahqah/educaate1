package sg.edu.np.educaate1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Activity.StudentMessage;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.Classes.Message;
import sg.edu.np.educaate1.Classes.Student;

public class TutorMessage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    ArrayList<String> msgList;
    ArrayAdapter<String> adapter;
    ListView listView;

    String tutorID;
    String studentID;
    String id;
    String chatID;

    //for confirmation
    private DatabaseReference databaseReferenceC;
    private DatabaseReference databaseReferenceCS;
    String studentEmail;
    ArrayList<String> studentIDList;
    private String currStudentID;
    Booking b;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    //for button
    DatabaseReference confirmDatabase;
    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_message);

        confirmBtn=findViewById(R.id.button5);
        confirmBtn.setVisibility(View.VISIBLE);

        Intent i=getIntent();
        tutorID=i.getStringExtra("tutorid");
        studentID=i.getStringExtra("studentid");
        id=i.getStringExtra("id");
        studentEmail=i.getStringExtra("email");

        Log.d("tutormsgid",tutorID);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("chat").child(id);
        msgList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //idList.add(snapshot.getKey());
                    Log.d("myid", user.getUid());
                    if(snapshot.child("studentID").getValue().toString().equals(studentID)&&snapshot.child("bookingType").getValue().toString().equals("Tutor")){
                        chatID=snapshot.getKey();
                        Log.d("myid", user.getUid());
                        for(DataSnapshot msgSnapshot:snapshot.child("messages").getChildren()){
                            Log.d("messages", msgSnapshot.getKey());
                            String msg=msgSnapshot.child("msg").getValue().toString();
                            msgList.add(msg);
                        }
                    }
                    else if(snapshot.child("tutorID").getValue().toString().equals(tutorID)&&snapshot.child("bookingType").getValue().toString().equals("Student")){
                        chatID=snapshot.getKey();
                        Log.d("myid", user.getUid());
                        for(DataSnapshot msgSnapshot:snapshot.child("messages").getChildren()){
                            Log.d("messages", msgSnapshot.getKey());
                            String msg=msgSnapshot.child("msg").getValue().toString();
                            msgList.add(msg);
                        }
                    }
                    //Log.d("list", snapshot.getKey());
                    //if(snapshot.child("studentID").getValue().toString().equals(user.getUid())){
                    //Log.d("snapshot", snapshot.child("studentID").getValue().toString());
                    /*if(dataSnapshot.child("studentID").getValue().toString().equals(user.getUid())){
                        for(DataSnapshot msgSnapshot:snapshot.child("message").getChildren()){
                            String msg=msgSnapshot.getValue().toString();
                            msgList.add(msg);
                        }
                    }*/

                    //Log.d("msg", msg);

                    Log.d("msg list", Integer.toString(msgList.size()));

                    adapter.notifyDataSetChanged();//important line!!
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("list", Integer.toString(msgList.size()));

        adapter=new ArrayAdapter<>(TutorMessage.this,android.R.layout.simple_list_item_1,msgList);
        listView=(ListView)findViewById(R.id.tutormsgList);
        listView.setAdapter(adapter);



        //codes for confirm function
        databaseReferenceC = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("booking");
        databaseReferenceC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    b=snapshot.getValue(Booking.class);
                    Log.d("booking ID b",b.getId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get all UID of student that have the booking ID
        databaseReferenceCS= FirebaseDatabase.getInstance().getReference().child("users");
        studentIDList=new ArrayList<>();
        databaseReferenceCS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentIDList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("student")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            if(id.equals(msgSnapshot.getKey())){
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


        //codes for confirmation button
        confirmDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("booking").child(id);
        confirmDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for(DataSnapshot snapshot:dataSnapshot.child(id).getChildren()){
                    Log.d("confirmid",id);
                    //Log.d("confirm",snapshot.child("status").getValue().toString());
                    if(dataSnapshot.child("status").getValue().toString().equals("Confirm")){
                        //Log.d("confirm",snapshot.child("status").getValue().toString());
                        confirmBtn.setVisibility(View.GONE);
                    }
                    else if(dataSnapshot.child("status").getValue().toString().equals("Close") && dataSnapshot.child("type").getValue().toString().equals("Student")){
                        confirmBtn.setVisibility(View.GONE);
                    }
                    else if(dataSnapshot.child("type").getValue().toString().equals("Student")){
                        confirmBtn.setVisibility(View.GONE);
                    }
                    /*else if(snapshot.child("status").getValue().toString().equals("Close")){
                        confirmBtn.setVisibility(View.VISIBLE);
                    }*/
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMsg(View v){
        final EditText text=findViewById(R.id.tutormsg);
        String msg=text.getText().toString();

        Message message=new Message();
        msg="Tutor: "+msg;
        message.setMsg(msg);

        String count=Integer.toString(msgList.size()+1);

        databaseReference.child(chatID).child("messages").child(count).setValue(message);
    }

    public void cancel(View v){
        Intent intent=new Intent(TutorMessage.this,TutorViewSchedule.class);
        startActivity(intent);
    }

    public void confirm(View v) {


        databaseReference = FirebaseDatabase.getInstance().getReference();
        //FirebaseUser user = mAuth.getCurrentUser();


        //edit booking status under tutor to close
        Booking booking = new Booking();

        booking.setDate(b.getDate());
        booking.setTime(b.getTime());
        booking.setSubject(b.getSubject());
        booking.setDesc(b.getDesc());
        booking.setPrice(b.getPrice());
        booking.setLocation(b.getLocation());
        booking.setName(b.getName());
        booking.setId(b.getId());
        booking.setType(b.getType());
        booking.setTutorid(b.getTutorid());
        booking.setStatus("Close");

        databaseReference.child("users").child(user.getUid()).child("booking").child(b.getId()).setValue(booking);


        //edit booking status under selected student to confirm
        Booking bookingConfirm = new Booking();

        bookingConfirm.setDate(b.getDate());
        bookingConfirm.setTime(b.getTime());
        bookingConfirm.setSubject(b.getSubject());
        bookingConfirm.setDesc(b.getDesc());
        bookingConfirm.setPrice(b.getPrice());
        bookingConfirm.setLocation(b.getLocation());
        bookingConfirm.setName(b.getName());
        bookingConfirm.setId(b.getId());
        bookingConfirm.setType(b.getType());
        bookingConfirm.setTutorid(b.getTutorid());
        bookingConfirm.setStatus("Confirm");

        //edit booking status under other student to cancel
        Booking bookingCancel = new Booking();

        bookingCancel.setDate(b.getDate());
        bookingCancel.setTime(b.getTime());
        bookingCancel.setSubject(b.getSubject());
        bookingCancel.setDesc(b.getDesc());
        bookingCancel.setPrice(b.getPrice());
        bookingCancel.setLocation(b.getLocation());
        bookingCancel.setName(b.getName());
        bookingCancel.setId(b.getId());
        bookingCancel.setType(b.getType());
        bookingCancel.setTutorid(b.getTutorid());
        bookingCancel.setStatus("Cancel");

        if (studentIDList.size() != 0) {
            for (int i = 0; i < studentIDList.size(); i++) {
                if (studentIDList.get(i) == currStudentID) {
                    databaseReference.child("users").child(currStudentID).child("booking").child(b.getId()).setValue(bookingConfirm);
                } else {
                    databaseReference.child("users").child(studentIDList.get(i)).child("booking").child(b.getId()).setValue(bookingCancel);
                }
            }
        }
    }
}
