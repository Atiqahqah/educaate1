package sg.edu.np.educaate1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");

        BottomNavigationView navigationView = findViewById(R.id.btm_nav);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                //home fragment
                if (id == R.id.home) {
                    //Bring user to different home page
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String type=dataSnapshot.child(uid).child("type").getValue().toString();
                            Log.d(TAG, type);

                            if(type.equals("student")){
                                StudentHomeFragment fragment = new StudentHomeFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commitAllowingStateLoss();

                            }
                            else if(type.equals("tutor")){
                                TutorHomeFragment fragment = new TutorHomeFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                //appointment fragment
                if (id == R.id.appointments) {
                    //Bring user to different home page
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String type=dataSnapshot.child(uid).child("type").getValue().toString();
                            Log.d(TAG, type);

                            if(type.equals("student")){
                                StudentApptFragment fragment = new StudentApptFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                            else if(type.equals("tutor")){
                                TutorApptFragment fragment = new TutorApptFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commitAllowingStateLoss();
                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                //profile fragment
                if (id == R.id.profile) {
                    //Bring user to different home page
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String type=dataSnapshot.child(uid).child("type").getValue().toString();
                            Log.d(TAG, type);

                            if(type.equals("student")){
                                StudentProfileFragment fragment = new StudentProfileFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                            else if(type.equals("tutor")){
                                TutorProfileFragment fragment = new TutorProfileFragment();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout, fragment);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                return true;
            }
        });
        // so the default fragment is home fragment
        navigationView.setSelectedItemId(R.id.home);
    }
}