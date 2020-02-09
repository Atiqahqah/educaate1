package sg.edu.np.educaate1.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import sg.edu.np.educaate1.Activity.Home;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.R;

public class StudentRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";
    SharedPreferences pref;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    private EditText sEmailField;
    private EditText sPasswordField;
    private EditText sName;
    private EditText sPhoneNo;
    private EditText sEduLvl;
    EditText sConfirmPass;
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
        setContentView(R.layout.activity_student_register);

        mAuth = FirebaseAuth.getInstance();

        sEmailField = findViewById(R.id.srEmailET);
        sPasswordField = findViewById(R.id.srPasswordET);
        sConfirmPass=findViewById(R.id.srPasswordET2);
        sName = findViewById(R.id.srNameET);
        errorMsg=findViewById(R.id.studentError);

        errorMsg.setText("");
        //sGender = findViewById(R.id.srGenderET);
        //sPhoneNo = findViewById(R.id.srPhoneNoET);

        d=findViewById(R.id.dayT);
        m=findViewById(R.id.monthT);
        y=findViewById(R.id.yearT);

        Spinner mySpinner=(Spinner)findViewById(R.id.studentGender);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(StudentRegister.this);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"Account created successfully",Toast.LENGTH_LONG).show();
            String uid=account.getUid();
            Intent intent=new Intent(StudentRegister.this, Home.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }else {
            Toast.makeText(this,"Registration unsuccessful",Toast.LENGTH_LONG).show();
        }
    }

    public void createStudentAccount(String email,String password){
                pref= PreferenceManager.getDefaultSharedPreferences(this);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(   this, new OnCompleteListener<AuthResult>() {
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
                                    s.setAge(age);
                                    s.setGender(gender);
                                    //s.setPhoneNo(sPhoneNo.getText().toString());
                                    s.setEduLevel("not specified");
                                    s.setType("student");
                                    s.setId(user.getUid());

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
        //if (i == R.id.srRegisterBtn) {
            if(sEmailField.getText().toString().equals("")||sName.getText().toString().equals("")||sPasswordField.getText().toString().equals("")||d.getText().toString().equals("")||m.getText().toString().equals("")||y.getText().toString().equals("")){
                errorMsg.setText("Please fill up all the details");
            }
            else {
                day=Integer.parseInt(d.getText().toString());
                month=Integer.parseInt(m.getText().toString());
                year=Integer.parseInt(y.getText().toString());
                age=getAge(year,month,day);
                if (Integer.parseInt(age) < 16) {
                    errorMsg.setText("You must be 16 or older to sign up");
                }
                else if (!sPasswordField.getText().toString().equals(sConfirmPass.getText().toString())) {
                    sPasswordField.setText("");
                    sConfirmPass.setText("");
                    errorMsg.setText("Passwords do not match");
                }
                else {
                    createStudentAccount(sEmailField.getText().toString(), sPasswordField.getText().toString());
                }
            }
            //createStudentAccount(sEmailField.getText().toString(), sPasswordField.getText().toString());
        //}
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
