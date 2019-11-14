package sg.edu.np.educaate1.Activity;

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

import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.R;

public class StudentUpdate extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

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

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        uid = sharedPreferences.getString(UID, null);

        if(uid != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    student = dataSnapshot.getValue(Student.class);
                    sEmailField.setText(student.getEmail());
                    sName.setText(student.getName());
                    sPhoneNo.setText(student.getPhoneNo());
                    sEduLvl.setText(student.getEduLevel());
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
        databaseReference.child("users").child(user.getUid()).setValue(s);
    }

    public void onStudentUpdate(View v) {
        int i = v.getId();
        if (i == R.id.sUpdateBtn) {
            UpdateStudentProfile();
        }
    }

}
