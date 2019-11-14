package sg.edu.np.educaate1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.UUID;

public class LogIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    private EditText mEmailField;
    private EditText mPasswordField;

    SharedPreferences pref;

    String role;

    ArrayList<String> studentEmailList;
    ArrayList<String> tutorEmailList;
    DatabaseReference databaseReference;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);


    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            uid = user.getUid();

                            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(UID, uid);
                            editor.apply();

                            Log.d(TAG,uid);

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void  updateUI(final FirebaseUser account){
        if(account != null){
            //Bring user to different home page
            databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    String type=dataSnapshot.child(uid).child("type").getValue().toString();
                    Log.d(TAG, type);

                    //String type = "student";

                    if(type.equals("student")){
                        Intent intent=new Intent(LogIn.this,Home.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        //Toast.makeText(this,"Student Signed In",Toast.LENGTH_LONG).show();
                        //role = "student";
                    }
                    else if(type.equals("tutor")){
                        Intent intent=new Intent(LogIn.this,Home.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        //role = "tutor";
                    }
                    else {
                        Intent intent=new Intent(LogIn.this,success.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(this,"Sign In successful.",Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this,success.class)); //currently after signing in and signing up, it brings u to the same page

        }else {
            Toast.makeText(this,"Error logging in. Are details correct?",Toast.LENGTH_LONG).show();
        }
    }

    public void onLogIn(View v) {
        signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    }

    public void clickSignup(View v){
        startActivity(new Intent(LogIn.this, selectUserType.class));
    }
}
