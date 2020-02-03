package sg.edu.np.educaate1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.Classes.Tutor;
import sg.edu.np.educaate1.R;

public class TutorRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";
    SharedPreferences pref;

    private EditText tEmailField;
    private EditText tPasswordField;
    private EditText tName;
    private EditText tPhoneNo;
    private EditText tEduLvl;
    private EditText tQuali;
    private EditText tDesc;
    TextView errorMsg;

    String gender;

    String age;
    int day;
    int month;
    int year;
    EditText d;
    EditText m;
    EditText y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);
        mAuth = FirebaseAuth.getInstance();

        errorMsg=findViewById(R.id.textView4);
        errorMsg.setText("");

        tEmailField = findViewById(R.id.trEmailET);
        tPasswordField = findViewById(R.id.trPasswordET);
        tName = findViewById(R.id.trNameET);
        tPhoneNo = findViewById(R.id.trPhoneNoET);
        tEduLvl = findViewById(R.id.trEduLvlET);
        tQuali = findViewById(R.id.trQualificationET);
        tDesc = findViewById(R.id.trDescriptionET);

        d=findViewById(R.id.tDay);
        m=findViewById(R.id.tMonth);
        y=findViewById(R.id.tYear);

        Spinner mySpinner=(Spinner)findViewById(R.id.tutorGender);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(TutorRegister.this);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"Account created successfully",Toast.LENGTH_LONG).show();
            String uid=account.getUid();
            Intent intent=new Intent(TutorRegister.this, Home.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }else {
            Toast.makeText(this,"Registration unsuccessful",Toast.LENGTH_LONG).show();
        }
    }

    public void createTutorAccount(String email,String password){
        day=Integer.parseInt(d.getText().toString());
        month=Integer.parseInt(m.getText().toString());
        year=Integer.parseInt(y.getText().toString());
        age=getAge(year,month,day);
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
                            t.setAge(age);
                            t.setGender(gender);
                            t.setPhoneNo(tPhoneNo.getText().toString());
                            t.setEduLevel(tEduLvl.getText().toString());
                            t.setQualification(tQuali.getText().toString());
                            t.setDescription(tDesc.getText().toString());
                            t.setType("tutor");
                            t.setId(user.getUid());

                            databaseReference.child("users").child(user.getUid()).setValue(t);
                            //databaseReference.child("email").child("tutor").child("tutorEmail").setValue(tEmailField.getText().toString());
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
        if(tEmailField.getText().toString().equals("")||tName.getText().toString().equals("")||tPasswordField.getText().toString().equals("")
                ||d.getText().toString().equals("")||m.getText().toString().equals("")||y.getText().toString().equals("")
        ||tDesc.getText().toString().equals("")||tEduLvl.getText().toString().equals("")||tPhoneNo.getText().toString().equals("")||tQuali.getText().toString().equals("")){
            errorMsg.setText("Please fill up all the details");
        }
        else if(Integer.parseInt(d.getText().toString())>31||Integer.parseInt(m.getText().toString())>12){
            errorMsg.setText("Please enter the correct date format");
            d.setText("");
            m.setText("");
        }
        else {
            createTutorAccount(tEmailField.getText().toString(), tPasswordField.getText().toString());
            errorMsg.setText("");
        }
    }

    private String getAge(int year,int month,int day){
        Calendar dob=Calendar.getInstance();
        Calendar today=Calendar.getInstance();

        dob.set(year, month, day);

        int age=today.get(Calendar.YEAR)-dob.get(Calendar.YEAR);

        if(today.get(Calendar.DAY_OF_YEAR)<dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        Integer ageInt=new Integer(age);
        String ageS=ageInt.toString();

        return ageS;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        gender="Male";
    }
}
