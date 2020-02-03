package sg.edu.np.educaate1.Activity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.R;

public class StudentReview extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String TUID = "tutorid";

    TextView Review;
    TextView Rating;
    Button Submit;

    String id;
    String desc;
    int score;
    String UID;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_review);
        InitialiseView();

        sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        String tutorID =sharedPreferences.getString(TUID,"") ;

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(tutorID);
        UID = mAuth.getCurrentUser().getUid();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc = Review.getText().toString();
                score = Integer.parseInt(Rating.getText().toString());

                String ratingID = databaseReference.child("rating").push().getKey();

                Rating rating = new Rating(ratingID,UID,desc,score);
                databaseReference.child("rating").child(ratingID).setValue(rating);
            }
        });
    }

    public void InitialiseView(){
        Review = findViewById(R.id.stuentReviewReview);
        Rating = findViewById(R.id.studentReviewRating);
        Submit = findViewById(R.id.studentReviewSubmit);
    }
}
