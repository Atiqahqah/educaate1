package sg.edu.np.educaate1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.Classes.Student;

public class TutorViewSchedule extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<Student> studentList;
    ArrayAdapter adapter;
    ListView listView;

    String TAG;
    private Button delBtn;
    String strName;
    String strSubj;
    String strLocation;
    String strDate;
    String strTime;
    String strPrice;
    String strDesc;
    //String strId;
    String strType;
    String strStatus;
    String bookingID;

    ArrayList<String> studentIDList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_schedule);

        Intent i=getIntent();

        strName = i.getStringExtra("name");

        strSubj = i.getStringExtra("subj");
        TextView subj=findViewById(R.id.tSubj);
        subj.setText(strSubj);

        strLocation = i.getStringExtra("location");
        TextView location=findViewById(R.id.tLoc);
        location.setText(strLocation);

        strDate = i.getStringExtra("date");
        strTime = i.getStringExtra("time");
        TextView datetime=findViewById(R.id.tDateTimez);
        datetime.setText(strDate+" "+strTime);

        strPrice = i.getStringExtra("price");
        TextView price=findViewById(R.id.tPrice);
        price.setText("$"+strPrice);

        strDesc = i.getStringExtra("desc");
        TextView desc=findViewById(R.id.tDesc);
        desc.setText(strDesc);

        //strId = i.getStringExtra("id");

        strType=i.getStringExtra("type");

        strStatus=i.getStringExtra("status");

        bookingID=i.getStringExtra("id");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        studentList=new ArrayList<>();
        studentIDList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                studentIDList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("student")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            if(bookingID.equals(msgSnapshot.getKey())){
                                Student s=snapshot.getValue(Student.class);
                                studentList.add(s);
                                studentIDList.add(snapshot.getKey());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new RequestAdapter(this,R.layout.requestlayout,studentList);
        listView=(ListView)findViewById(R.id.requestList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Student s = (Student) parent.getItemAtPosition(position);
                Intent intent = new Intent(TutorViewSchedule.this,
                        TutorMessage.class);

                intent.putExtra("email",s.getEmail());
                intent.putExtra("id",bookingID);

                /*SharedPreferences.Editor editor=pref.edit();
                editor.putString("DATE",b.getDate());
                editor.apply();*/

                startActivity(intent);
            }
        });

        delBtn = findViewById(R.id.tDelBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(strStatus.equals("Close")) {
                    displayConfirmStatus();
                }
                else {
                    displayDelConfirmation();
                }
            }
        });
    }

    public void displayDelConfirmation(){
        new AlertDialog.Builder(TutorViewSchedule.this)//alert dialog
                .setTitle("Confirm?")
                .setMessage("Delete this schedule?")
                .setPositiveButton("Yes",//option for yes
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference= FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                                Booking bookingCancel=new Booking();

                                bookingCancel.setDate(strDate);
                                bookingCancel.setTime(strTime);
                                bookingCancel.setSubject(strSubj);
                                bookingCancel.setDesc(strDesc);
                                bookingCancel.setPrice(strPrice);
                                bookingCancel.setLocation(strLocation);
                                bookingCancel.setName(strName);
                                bookingCancel.setId(bookingID);
                                bookingCancel.setType(strType);
                                bookingCancel.setStatus("Cancel");

                                if(studentIDList.size()!=0){
                                    for(int index=0;index<studentIDList.size();index++){
                                        databaseReference.child("users").child(studentIDList.get(index)).child("booking").child(bookingID).setValue(bookingCancel);
                                    }
                                }

                                databaseReference.child("users").child(user.getUid()).child("booking").child(bookingID).removeValue();
                            }
                        })
                .setNegativeButton("Cancel",//option for cancel
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();//closes the alert dialog
                            }
                        })
                .show();
    }

    public void displayConfirmStatus(){
        new AlertDialog.Builder(TutorViewSchedule.this)//alert dialog
                .setTitle("Unable to perform action")
                .setMessage("Please talk to the tutor to cancel the booking.")
                .setPositiveButton("Noted",//option for yes
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();//closes the alert dialog
                            }
                        })
                .show();
    }
}
