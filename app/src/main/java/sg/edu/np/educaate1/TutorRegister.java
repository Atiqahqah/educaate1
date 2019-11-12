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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TutorRegister extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";
    SharedPreferences pref;

    private EditText tEmailField;
    private EditText tPasswordField;
    private EditText tName;
    private EditText tAge;
    private EditText tGender;
    private EditText tPhoneNo;
    private EditText tEduLvl;
    private EditText tQuali;
    private EditText tDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);
        mAuth = FirebaseAuth.getInstance();

        tEmailField = findViewById(R.id.trEmailET);
        tPasswordField = findViewById(R.id.trEmailET);
        tName = findViewById(R.id.trNameET);
        tAge = findViewById(R.id.trAgeET);
        tGender = findViewById(R.id.trPasswordET);
        tPhoneNo = findViewById(R.id.trPhoneNoET);
        tEduLvl = findViewById(R.id.trEduLvlET);
        tQuali = findViewById(R.id.trQualificationET);
        tDesc = findViewById(R.id.trDescriptionET);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U signed in successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,PostSchedule.class));
        }else {
            Toast.makeText(this,"U didnt signed in",Toast.LENGTH_LONG).show();
        }
    }

    public void createTutorAccount(String email,String password){
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //add user type into database
                            databaseReference= FirebaseDatabase.getInstance().getReference();
                            Tutor t = new Tutor();
                            t.setEmail(tEmailField.getText().toString());
                            t.setName(tName.getText().toString());
                            t.setAge(tAge.getText().toString());
                            t.setGender(tGender.getText().toString());
                            t.setPhoneNo(tPhoneNo.getText().toString());
                            t.setEduLevel(tEduLvl.getText().toString());
                            t.setEduLevel(tQuali.getText().toString());
                            t.setEduLevel(tDesc.getText().toString());

                            databaseReference.child("users").child(user.getUid()).setValue(t);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(TutorRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    public void onTutorRegister(View v) {
        int i = v.getId();
        if (i == R.id.trRegisterBtn) {
            createTutorAccount(tEmailField.getText().toString(), tPasswordField.getText().toString());
        }
    }
}
