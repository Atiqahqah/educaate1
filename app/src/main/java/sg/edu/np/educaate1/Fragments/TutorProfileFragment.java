package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.gson.Gson;

import java.util.ArrayList;

import sg.edu.np.educaate1.Activity.LogIn;
import sg.edu.np.educaate1.Adapters.SectionPagerAdapter;
import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.Classes.Tutor;
import sg.edu.np.educaate1.Fragments.ChildFragment.RatingsFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.ReviewFragment;
import sg.edu.np.educaate1.Fragments.ChildFragment.TutorSummaryFragment;
import sg.edu.np.educaate1.R;
import sg.edu.np.educaate1.Activity.TutorUpdate;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorProfileFragment extends Fragment {
    //FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseUser user;
    private String UID;

    //object and Data
    private Tutor tutor;
    private Rating rating;

    //Views
    ImageView profilepic;
    TextView name;
    ImageButton editprofile;
    Button logout;
    //Fragments and TabLayout
    ViewPager viewPager;
    TabLayout tabLayout;

    int i;

    //ArrayList for reviewobject
    ArrayList<Rating> ReviewList;

    public TutorProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_profile, container, false);
        ReviewList = new ArrayList<>();
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        InitialiseViews(view);

        //get data from firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("rating");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ReviewList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    rating = dataSnapshot1.getValue(Rating.class);
                    ReviewList.add(rating);
                    Log.d("RatingID",ReviewList.get(i).getReviewId());
                    i+=1;
                }
                Gson gson = new Gson();
                final String json = gson.toJson(ReviewList);
                editor.putString("review",json);
                editor.apply();
                Log.d("JSON String", json);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tutor = dataSnapshot.getValue(Tutor.class);
                //Set data to View
                name.setText(tutor.getName());
                editor.putString("tutor email",tutor.getEmail());
                editor.putString("tutor age",tutor.getAge());
                editor.putString("tutor gender",tutor.getGender());
                editor.putString("tutor phone",tutor.getPhoneNo());
                editor.putString("tutor edu",tutor.getEduLevel());
                editor.putString("tutor qual",tutor.getQualification());
                editor.putString("tutor desc",tutor.getDescription());
                editor.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        i = 0;




        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TutorUpdate.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), LogIn.class);
                startActivity(i);
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

        adapter.addFragment(new TutorSummaryFragment(),"Summary");
        adapter.addFragment(new RatingsFragment(),"Ratings");
        adapter.addFragment(new ReviewFragment(),"Review");

        viewPager.setAdapter(adapter);
    }

    public void InitialiseViews(View v){
        name = v.findViewById(R.id.tProfileNameTV);
        profilepic = v.findViewById(R.id.tProfileImage);
        editprofile = v.findViewById(R.id.tUpdateProfileBtn);

        logout = v.findViewById(R.id.tLogOut);

        viewPager = v.findViewById(R.id.tViewPager);
        tabLayout = v.findViewById(R.id.tTabLayout);
    }

}
