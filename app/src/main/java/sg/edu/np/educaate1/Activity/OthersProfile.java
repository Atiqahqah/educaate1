package sg.edu.np.educaate1.Activity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import sg.edu.np.educaate1.Adapters.SectionPagerAdapter;
import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.Classes.Tutor;
import sg.edu.np.educaate1.Fragments.ChildFragment.RatingsFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.ReviewFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.StudentSummaryFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.TutorSummaryFragment;
import sg.edu.np.educaate1.R;

public class OthersProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseUser user;
    private String UID;

    //object and Data
    private Tutor tutor;
    private Student student;
    private Rating rating;

    //Views
    ImageView profilepic;
    TextView name;
    ImageButton editprofile;

    //Fragments and TabLayout
    ViewPager viewPager;
    TabLayout tabLayout;

    int i;
    String type;

    //ArrayList for reviewobject
    ArrayList<Rating> ReviewList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        ReviewList = new ArrayList<>();
        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        InitialiseViews();

        //get data from firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = pref.getString("otheruid","");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    student = dataSnapshot.getValue(Student.class);
                    //Set Data to View
                    name.setText(student.getName());
                    editor.putString("student email",student.getEmail());
                    editor.putString("student age",student.getAge());
                    editor.putString("student gender",student.getGender());
                    editor.putString("student phone",student.getPhoneNo());
                    editor.putString("student edu",student.getEduLevel());
                    editor.putString("type","student");
                    editor.apply();

                }catch(Exception e){

                }
                try{
                    name.setText(tutor.getName());
                    editor.putString("tutor email",tutor.getEmail());
                    editor.putString("tutor age",tutor.getAge());
                    editor.putString("tutor gender",tutor.getGender());
                    editor.putString("tutor phone",tutor.getPhoneNo());
                    editor.putString("tutor edu",tutor.getEduLevel());
                    editor.putString("tutor qual",tutor.getQualification());
                    editor.putString("tutor desc",tutor.getDescription());
                    editor.apply();
                    editor.putString("type","tutor");
                }catch(Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        i = 0;

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("rating");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReviewList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    rating = dataSnapshot1.getValue(Rating.class);
                    ReviewList.add(rating);

                }
                Gson gson = new Gson();
                String json = gson.toJson(ReviewList);
                editor.putString("review",json);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setUpViewPager(ViewPager viewPager){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
        type = pref.getString("type","");
        if (type == "student") {
            adapter.addFragment(new StudentSummaryFragment(), "Summary");
        }
        else{
            adapter.addFragment(new TutorSummaryFragment(),"Summary");
        }
        adapter.addFragment(new RatingsFragment(),"Ratings");
        adapter.addFragment(new ReviewFragment(),"Review");

        viewPager.setAdapter(adapter);
    }



    public void InitialiseViews(){
        name = findViewById(R.id.otherProfileNameTV);
        profilepic = findViewById(R.id.otherProfileIV);

        viewPager = findViewById(R.id.oViewPager);
        tabLayout = findViewById(R.id.oTabLayout);
    }
}
