package sg.edu.np.educaate1.Activity;

import android.content.Intent;
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
import sg.edu.np.educaate1.R;

public class StudentMessage extends AppCompatActivity {
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

    Button confirmBtn;
    Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_message);

        confirmBtn=(Button)findViewById(R.id.button9);
        confirmBtn.setVisibility(View.VISIBLE);
        paymentBtn=findViewById(R.id.button10);
        paymentBtn.setVisibility(View.GONE);

        Intent i=getIntent();//get intent from student appt
        tutorId=i.getStringExtra("tutorid");
        studentId=i.getStringExtra("studentid");
        id=i.getStringExtra("id");

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
                    }
                    else if(dataSnapshot.child("status").getValue().toString().equals("Close") && dataSnapshot.child("type").getValue().toString().equals("Student")){
                        confirmBtn.setVisibility(View.GONE);
                        paymentBtn.setVisibility(View.VISIBLE);
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
}
