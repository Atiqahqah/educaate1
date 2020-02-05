package sg.edu.np.educaate1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import sg.edu.np.educaate1.Adapters.BookingAdapter;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.Classes.Chat;
import sg.edu.np.educaate1.Classes.Message;
import sg.edu.np.educaate1.Classes.Tutor;
import sg.edu.np.educaate1.R;

public class StudentMessage extends AppCompatActivity {
    //Shared Pref
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String TUID = "tutorid";

    ListView listView;
    ArrayList<String> msgList;
    ArrayList<String>idList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    ArrayAdapter<String> adapter;
    String TAG;
    String tutorId;
    String studentId;
    String id;
    String msg;
    String chatID;

    String strName;
    String strSubj;
    String strLocation;
    String strDate;
    String strTime;
    String strPrice;
    String strDesc;
    String strType;
    String name;

    Button confirmBtn;
    Button paymentBtn;
    Button rateBtn;

    //for confirmation
    private DatabaseReference databaseReferenceC;
    private DatabaseReference databaseReferenceCS;
    String tutorEmail;
    ArrayList<String> tutorIDList;
    private String currTutorID;
    Booking b;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_message);
        //Initialise SharedPref
        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        confirmBtn=(Button)findViewById(R.id.button9);
        confirmBtn.setVisibility(View.VISIBLE);
        paymentBtn=findViewById(R.id.button10);
        paymentBtn.setVisibility(View.GONE);
        rateBtn=findViewById(R.id.sRateBtn);
        rateBtn.setVisibility(View.GONE);

        Intent i=getIntent();//get intent from student appt
        tutorId=i.getStringExtra("tutorid");
        studentId=i.getStringExtra("studentid");
        id=i.getStringExtra("id");
        tutorEmail=i.getStringExtra("email");
        //name=i.getStringExtra("bookeename");

        strName=i.getStringExtra("name");
        strDate=i.getStringExtra("date");
        strDesc=i.getStringExtra("desc");
        strPrice=i.getStringExtra("price");
        strLocation=i.getStringExtra("name");
        strSubj=i.getStringExtra("subj");
        strTime=i.getStringExtra("time");
        strType=i.getStringExtra("type");

        TextView nameLabel=findViewById(R.id.textView9);
        nameLabel.setText(strName);

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
                    //Log.d("myid", user.getUid());
                    if(snapshot.child("bookingType").getValue().toString().equals("Tutor")&&snapshot.child("studentID").getValue().toString().equals(studentId)){
                        chatID=snapshot.getKey();
                        Log.d("myid", user.getUid());
                        for(DataSnapshot msgSnapshot:snapshot.child("messages").getChildren()){
                            Log.d("messages", msgSnapshot.getKey());
                            String msg=msgSnapshot.child("msg").getValue().toString();
                            msgList.add(msg);
                        }
                    }
                    else if(snapshot.child("tutorID").getValue().toString().equals(tutorId)&&snapshot.child("bookingType").getValue().toString().equals("Student")){
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

        adapter=new ArrayAdapter<>(StudentMessage.this,android.R.layout.simple_list_item_1,msgList);
        listView=(ListView)findViewById(R.id.messageList);
        listView.setAdapter(adapter);

        //codes for confirm function
        databaseReferenceC = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("booking");
        databaseReferenceC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("id").getValue().equals(id)){
                        b=snapshot.getValue(Booking.class);
                        Log.d("booking ID b",b.getId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get all UID of tutor that have the booking ID
        databaseReferenceCS= FirebaseDatabase.getInstance().getReference().child("users");
        tutorIDList=new ArrayList<>();
        databaseReferenceCS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tutorIDList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("tutor")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            if(id.equals(msgSnapshot.getKey())){
                                Tutor t=snapshot.getValue(Tutor.class);
                                tutorIDList.add(snapshot.getKey());
                                Log.d("Tutor ID",snapshot.getKey());
                                editor.putString(TUID,t.getId());
                                editor.apply();
                            }
                        }
                        if(snapshot.child("email").getValue().toString().equals(tutorEmail)){
                            currTutorID = snapshot.getKey();
                            Log.d("current Tutor ID",currTutorID);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("booking").child(id);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for(DataSnapshot snapshot:dataSnapshot.child(id).getChildren()){
                    Log.d("confirmid",id);
                    //Log.d("confirm",snapshot.child("status").getValue().toString());
                    if(dataSnapshot.child("status").getValue().toString().equals("Confirm")){
                        //Log.d("confirm",snapshot.child("status").getValue().toString());
                        confirmBtn.setVisibility(View.GONE);
                        paymentBtn.setVisibility(View.VISIBLE);
                        rateBtn.setVisibility(View.GONE);
                    }
                    else if(dataSnapshot.child("status").getValue().toString().equals("Close") && dataSnapshot.child("type").getValue().toString().equals("Student")){
                        confirmBtn.setVisibility(View.GONE);
                        paymentBtn.setVisibility(View.VISIBLE);
                        rateBtn.setVisibility(View.GONE);
                    }
                    else if(dataSnapshot.child("status").getValue().toString().equals("Paid")){
                        confirmBtn.setVisibility(View.GONE);
                        paymentBtn.setVisibility(View.GONE);
                        rateBtn.setVisibility(View.VISIBLE);
                    }
                    else if(dataSnapshot.child("type").getValue().toString().equals("Tutor")){
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

    public void send(View v){
        final EditText text=findViewById(R.id.editText2);
        String msg=text.getText().toString();

        Message message=new Message();
        msg="Student: "+msg;
        message.setMsg(msg);

        String count=Integer.toString(msgList.size()+1);

        databaseReference.child(chatID).child("messages").child(count).setValue(message);


        /*Message message=new Message();
        msg="Student: "+msg;
        message.setMsg(msg);

        String count=Integer.toString(msgList.size()+1);

        databaseReference.child("messages").child(count).setValue(message);*/

        /*if(msg!=null){
            sentmsg.setText("success");

            databaseReference= FirebaseDatabase.getInstance().getReference();


            Message message=new Message();
            message.setMsg("Student: "+text);

            databaseReference.child("chat").child(key).child("messages").setValue(message);
        }
        else{
            sentmsg.setText("write a message");
        }*/
    }

    public void studentConfirm(View v) {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //FirebaseUser user = mAuth.getCurrentUser();


        //edit booking status under student to close
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


        //edit booking status under selected tutor to confirm
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

        //edit booking status under other tutor to cancel
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

        if (tutorIDList.size() != 0) {
            for (int i = 0; i < tutorIDList.size(); i++) {
                if (tutorIDList.get(i) == currTutorID) {
                    databaseReference.child("users").child(currTutorID).child("booking").child(b.getId()).setValue(bookingConfirm);
                } else {
                    databaseReference.child("users").child(tutorIDList.get(i)).child("booking").child(b.getId()).setValue(bookingCancel);
                }
            }
        }
    }

    public void  payment(View v){
        Intent intent=new Intent(StudentMessage.this,PaymentConfirm.class);
        intent.putExtra("tutorid",tutorId);
        intent.putExtra("studentid",studentId);
        intent.putExtra("bookingid",id);
        intent.putExtra("name",strName);
        intent.putExtra("desc",strDesc);
        intent.putExtra("time",strTime);
        intent.putExtra("date",strDate);
        intent.putExtra("location",strLocation);
        intent.putExtra("subj",strSubj);
        intent.putExtra("price",strPrice);
        intent.putExtra("type", strType);

        startActivity(intent);
    }

    public void studentRate(View v){
        Intent intent=new Intent(StudentMessage.this,StudentReview.class);
        startActivity(intent);
    }
}
