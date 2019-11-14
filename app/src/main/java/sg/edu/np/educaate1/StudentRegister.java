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

public class StudentRegister extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";
    SharedPreferences pref;

    private EditText sEmailField;
    private EditText sPasswordField;
    private EditText sName;
    private EditText sAge;
    private EditText sGender;
    private EditText sPhoneNo;
    private EditText sEduLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        mAuth = FirebaseAuth.getInstance();

        sEmailField = findViewById(R.id.srEmailET);
        sPasswordField = findViewById(R.id.srPasswordET);
        sName = findViewById(R.id.srNameET);
        sAge = findViewById(R.id.srAgeET);
        //sGender = findViewById(R.id.srGenderET);
        sPhoneNo = findViewById(R.id.srPhoneNoET);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"U signed in successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,BookingList.class));
        }else {
            Toast.makeText(this,"U didnt signed in",Toast.LENGTH_LONG).show();
        }
    }

    public void createStudentAccount(String email,String password){
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
                            Student s = new Student();
                            s.setEmail(sEmailField.getText().toString());
                            s.setName(sName.getText().toString());
                            s.setAge(sAge.getText().toString());
                            s.setGender(sGender.getText().toString());
                            s.setPhoneNo(sPhoneNo.getText().toString());
                            s.setEduLevel(sEduLvl.getText().toString());
                            s.setType("student");

                            databaseReference.child("users").child(user.getUid()).setValue(s);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(StudentRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    public void onStudentRegister(View v) {
        int i = v.getId();
        if (i == R.id.srRegisterBtn) {
            createStudentAccount(sEmailField.getText().toString(), sPasswordField.getText().toString());
        }
    }
}
