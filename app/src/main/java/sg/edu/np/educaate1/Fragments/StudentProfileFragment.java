package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Adapters.SectionPagerAdapter;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.Fragments.StudentChildFragment.RatingsFragment;
import sg.edu.np.educaate1.Fragments.StudentChildFragment.ReviewFragment;
import sg.edu.np.educaate1.Fragments.StudentChildFragment.SummaryFragment;
import sg.edu.np.educaate1.R;
import sg.edu.np.educaate1.Activity.StudentUpdate;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {
    //FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String UID;

    //object and Data
    private Student student;

    //Views
    ImageView profilepic;
    TextView name;
    ImageButton editprofile;
    Button summary;
    Button rating;
    Button review;

    //Fragments and TabLayout
    ViewPager viewPager;
    TabLayout tabLayout;

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

        adapter.addFragment(new SummaryFragment(),"Summary");
        adapter.addFragment(new RatingsFragment(),"Ratings");
        adapter.addFragment(new ReviewFragment(),"Reviews");

        viewPager.setAdapter(adapter);
    }

    public void InitialiseViews(View v){
        name = v.findViewById(R.id.sProfileNameTV);
        profilepic = v.findViewById(R.id.sProfileImage);
        editprofile = v.findViewById(R.id.sUpdateProfileBtn);

        viewPager = v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.tabLayout);
    }



}
