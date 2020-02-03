package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Adapters.SectionPagerAdapter;
import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.Fragments.ChildFragment.RatingsFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.ReviewFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.StudentSummaryFragment;
import sg.edu.np.educaate1.R;
import sg.edu.np.educaate1.Activity.StudentUpdate;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {
    //FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseUser user;
    private String UID;

    //object and Data
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

    //ArrayList for reviewobject
    ArrayList<Rating> ReviewList = new ArrayList<>();

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        InitialiseViews(view);

        //get data from firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                //Set Data to View
                name.setText(student.getName());
                editor.putString("student email",student.getEmail());
                editor.putString("student age",student.getAge());
                editor.putString("student gender",student.getGender());
                editor.putString("student phone",student.getPhoneNo());
                editor.putString("student edu",student.getEduLevel());
                editor.apply();
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
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                rating = dataSnapshot.getValue(Rating.class);
                ReviewList.add(rating);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StudentUpdate.class));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new StudentSummaryFragment(),"Summary");
        adapter.addFragment(new RatingsFragment(),"Ratings");
        adapter.addFragment(new ReviewFragment(),"Review");

        viewPager.setAdapter(adapter);
    }

    public void InitialiseViews(View v){
        name = v.findViewById(R.id.sProfileNameTV);
        profilepic = v.findViewById(R.id.sProfileImage);
        editprofile = v.findViewById(R.id.sUpdateProfileBtn);

        viewPager = v.findViewById(R.id.sViewPager);
        tabLayout = v.findViewById(R.id.sTabLayout);
    }



}
