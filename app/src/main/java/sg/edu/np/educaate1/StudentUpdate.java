package sg.edu.np.educaate1;

import android.content.SharedPreferences;
import android.os.health.UidHealthStats;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class StudentUpdate extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    private EditText sEmailField;
    private EditText sPasswordField;
    private EditText sName;
    private EditText sDescription;
    private EditText sPhoneNo;
    private EditText sEduLvl;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mAuth = FirebaseAuth.getInstance();

        sEmailField = findViewById(R.id.sUpdateEmail);
        sName = findViewById(R.id.sUpdateName);
        //sDescription = findViewById(R.id.sUpdateDescription);
        sPhoneNo = findViewById(R.id.sUpdateNumber);
        sEduLvl = findViewById(R.id.sUpdateEdu);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        uid = sharedPreferences.getString(UID, null);

        if(uid != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Student student = dataSnapshot.getValue(Student.class);
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
        databaseReference = FirebaseDatabase.getInstance().getReference().child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child("users").child("name").setValue(sName);
                databaseReference.child("users").child("eduLevel").setValue(sEduLvl);
                databaseReference.child("users").child("phoneNo").setValue(sPhoneNo);
                databaseReference.child("users").child("email").setValue(sEmailField);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onStudentUpdate(View v) {
        int i = v.getId();
        if (i == R.id.sUpdateBtn) {
            UpdateStudentProfile();
        }
    }

}
