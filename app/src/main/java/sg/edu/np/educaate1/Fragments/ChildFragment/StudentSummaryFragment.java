package sg.edu.np.educaate1.Fragments.ChildFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentSummaryFragment extends Fragment {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String UID;

    //object and Data
    Student student;

    //Views
    TextView Name;
    TextView Email;
    TextView Phone;
    TextView Age;
    TextView Gender;
    TextView Edu;

    public StudentSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_summary, container, false);

        InitialiseView(view);

        //mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student = dataSnapshot.getValue(Student.class);
                Name.setText(student.getName());
                Email.setText(student.getEmail());
                Age.setText(student.getAge());
                Gender.setText(student.getGender());
                Phone.setText(student.getPhoneNo());
                Edu.setText(student.getEduLevel());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public void InitialiseView(View v){
        Name = v.findViewById(R.id.sNameText);
        Email = v.findViewById(R.id.sEmailText);
        Age = v.findViewById(R.id.sAgeText);
        Gender = v.findViewById(R.id.sGenderText);
        Phone = v.findViewById(R.id.sPhoneText);
        Edu = v.findViewById(R.id.sEduText);
    }

}
