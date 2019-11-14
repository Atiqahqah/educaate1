package sg.edu.np.educaate1.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import sg.edu.np.educaate1.Classes.Tutor;
import sg.edu.np.educaate1.R;
import sg.edu.np.educaate1.Activity.TutorUpdate;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorProfileFragment extends Fragment {
    //FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String UID;

    //object and Data
    private Tutor tutor;

    //Views
    ImageView profilepic;
    TextView name;
    ImageButton editprofile;
    Button summary;
    Button rating;
    Button review;


    public TutorProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_profile, container, false);
        InitialiseViews(view);

        //get data from firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tutor = dataSnapshot.getValue(Tutor.class);
                //Set data to View
                name.setText(tutor.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TutorUpdate.class));
            }
        });



        return view;
    }

    public void InitialiseViews(View v){
        name = v.findViewById(R.id.tProfileNameTV);
        profilepic = v.findViewById(R.id.tProfileImage);
        editprofile = v.findViewById(R.id.tUpdateProfileBtn);
        summary = v.findViewById(R.id.tSummaryProfileBtn);
        rating = v.findViewById(R.id.tRatingProfileBtn);
        review = v.findViewById(R.id.tReviewProfileBtn);
    }

}
