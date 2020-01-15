package sg.edu.np.educaate1.Activity;

import android.content.SharedPreferences;
import android.os.health.UidHealthStats;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import sg.edu.np.educaate1.R;

public class StudentUpdate extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    ArrayList<Booking>bookingList;

    private TextView sEmailField;
    private EditText sPasswordField;
    private TextView sName;
    private EditText sDescription;
    private EditText sPhoneNo;
    private EditText sEduLvl;

    private String uid;

    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);
        mAuth = FirebaseAuth.getInstance();

        sEmailField = findViewById(R.id.sUpdateEmail);
        sName = findViewById(R.id.sUpdateName);
        //sDescription = findViewById(R.id.sUpdateDescription);
        sPhoneNo = findViewById(R.id.sUpdateNumber);
        sEduLvl = findViewById(R.id.sUpdateEdu);

        //Spinner spinnerChoice = (Spinner) findViewById(R.id.genderSpinner);
        //ArrayAdapter<String> = new ArrayAdapter<String>(StudentRegister.this, list)


        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        uid = sharedPreferences.getString(UID, null);

        if(uid != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            bookingList=new ArrayList<>();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.child("booking").getChildren()){
                        Log.d("long",Long.toString(snapshot.getChildrenCount()));
                        //Log.d("subject",snapshot.child("subject").getValue().toString());
                        /*String subj=snapshot.child("subject").getValue().toString();
                        String date=snapshot.child("date").getValue().toString();
                        String time=snapshot.child("time").getValue().toString();
                        String price=snapshot.child("price").getValue().toString();
                        String location=snapshot.child("location").getValue().toString();
                        String status=snapshot.child("status").getValue().toString();
                        String id=snapshot.child("id").getValue().toString();
                        String desc=snapshot.child("desc").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();*/

                        //Booking booking=new Booking(name,subj,date,time,price,location,desc,id,status);
                        Booking booking=snapshot.getValue(Booking.class);
                        bookingList.add(booking);
                    }
                    student = dataSnapshot.getValue(Student.class);
                    sEmailField.setText(student.getEmail());
                    sName.setText(student.getName());
                    sPhoneNo.setText(student.getPhoneNo());
                    sEduLvl.setText(student.getEduLevel());
                    //Log.d(TAG,Long.toString(dataSnapshot.getChildrenCount()));
                    //if(dataSnapshot.getChildrenCount()>7){

                    //}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void UpdateStudentProfile() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        //add user type into database
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Student s = new Student();
        s.setEmail(sEmailField.getText().toString());
        s.setName(sName.getText().toString());
        s.setPhoneNo(sPhoneNo.getText().toString());
        s.setEduLevel(sEduLvl.getText().toString());
        Log.d("Age", student.getAge());
        s.setAge(student.getAge());
        s.setGender(student.getGender());
        s.setType("student");
        Log.d(TAG,student.getName());

        if(bookingList.size()!=0){
            databaseReference.child("users").child(user.getUid()).setValue(s);
            for(int i=0;i<bookingList.size();i++){
                String id=bookingList.get(i).getId();
                databaseReference.child("users").child(user.getUid()).child("booking").child(id).setValue(bookingList.get(i));
            }
        }
        else{
            databaseReference.child("users").child(user.getUid()).setValue(s);
        }
        //databaseReference.child("users").child(user.getUid()).setValue(s);
    }

    public void onStudentUpdate(View v) {
        int i = v.getId();
        if (i == R.id.sUpdateBtn) {
            UpdateStudentProfile();
        }
    }

    public void checkBooking(){
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        bookingList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                if(dataSnapshot.getChildrenCount()>7){
                    for(DataSnapshot snapshot:dataSnapshot.child("booking").getChildren()){
                            Booking s=snapshot.getValue(Booking.class);
                            bookingList.add(s);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
