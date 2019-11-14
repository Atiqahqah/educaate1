package sg.edu.np.educaate1.Activity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Tutor;
import sg.edu.np.educaate1.R;

public class TutorUpdate extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";
    private String uid;

    private EditText tName;
    private EditText tPhoneNo;
    private EditText tEduLvl;
    private EditText tQuali;
    private EditText tDesc;

    FirebaseUser user;

    private Tutor tutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_update);
        mAuth = FirebaseAuth.getInstance();

        tName = findViewById(R.id.tUpdateName);
        tPhoneNo = findViewById(R.id.tUpdatePhone);
        tEduLvl = findViewById(R.id.tUpdateEdu);
        tQuali = findViewById(R.id.tUpdateQualifications);
        tDesc = findViewById(R.id.tUpdateDescription);

        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        uid = sharedPreferences.getString(UID, null);
        Log.d(TAG, uid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tutor = dataSnapshot.getValue(Tutor.class);
                Log.d(TAG,"name is" + tutor.getName());

                tPhoneNo.setText(tutor.getPhoneNo());
                tName.setText(tutor.getName());
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
        t.setName(tName.getText().toString());
        t.setPhoneNo(tPhoneNo.getText().toString());
        t.setEduLevel(tEduLvl.getText().toString());
        t.setQualification(tQuali.getText().toString());
        t.setDescription(tDesc.getText().toString());
        t.setAge(tutor.getAge());
        t.setGender(tutor.getGender());
        t.setEmail(tutor.getEmail());

        databaseReference.child("tutor").child(user.getUid()).setValue(t);
    }

    public void onTutorRegister(View v) {
        int i = v.getId();
        if (i == R.id.trRegisterBtn) {
            UpdateTutorProfile();
        }
    }
}


