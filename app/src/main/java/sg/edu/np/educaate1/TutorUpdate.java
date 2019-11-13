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

public class TutorUpdate extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";
    SharedPreferences pref;

    private EditText tEmailField;
    private EditText tName;
    private EditText tPhoneNo;
    private EditText tEduLvl;
    private EditText tQuali;
    private EditText tDesc;

    FirebaseUser user = mAuth.getCurrentUser();

    private Tutor tutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);
        mAuth = FirebaseAuth.getInstance();

        tEmailField = findViewById(R.id.tUpdateEmail);
        tName = findViewById(R.id.tUpdateName);
        tPhoneNo = findViewById(R.id.tUpdatePhone);
        tEduLvl = findViewById(R.id.tUpdateEdu);
        tQuali = findViewById(R.id.tUpdateQualifications);
        tDesc = findViewById(R.id.tUpdateDescription);

        String uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tutor = dataSnapshot.getValue(Tutor.class);
                tEmailField.setText(tutor.getEmail());
                tName.setText(tutor.getName());
                tPhoneNo.setText(tutor.getPhoneNo());
                tEduLvl.setText(tutor.getEduLevel());
                tQuali.setText(tutor.getQualification());
                tDesc.setText(tutor.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void UpdateTutorProfile(){
        FirebaseUser user = mAuth.getCurrentUser();
        //updateUI(user);
        //add user type into database
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Tutor t = new Tutor();
        t.setEmail(tEmailField.getText().toString());
        t.setName(tName.getText().toString());
        t.setPhoneNo(tPhoneNo.getText().toString());
        t.setEduLevel(tEduLvl.getText().toString());
        t.setQualification(tQuali.getText().toString());
        t.setDescription(tDesc.getText().toString());
        t.setAge(tutor.getAge());
        t.setGender(tutor.getGender());

        databaseReference.child("tutor").child(user.getUid()).setValue(t);
    }

    public void onTutorRegister(View v) {
        int i = v.getId();
        if (i == R.id.trRegisterBtn) {
            UpdateTutorProfile();
        }
    }
}


