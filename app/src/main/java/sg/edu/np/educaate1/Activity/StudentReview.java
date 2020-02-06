package sg.edu.np.educaate1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Rating;
import sg.edu.np.educaate1.Classes.Student;
import sg.edu.np.educaate1.DeleteBooking;
import sg.edu.np.educaate1.R;

public class StudentReview extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String RUID = "reviewerid";

    TextView Review;
    TextView Rating;
    Button Submit;

    String id;
    String desc;
    double score;
    String UID;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_review);
        InitialiseView();

        sharedPreferences = getSharedPreferences(MyPREFERENCES ,0);
        String reviewerID =sharedPreferences.getString(RUID,"") ;

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(reviewerID);
        UID = mAuth.getCurrentUser().getUid();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc = Review.getText().toString();
                if(desc.equals("")){
                    Toast toast=Toast.makeText(getApplicationContext(),"Please enter a description",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{

                    try{
                        score = Double.parseDouble(Rating.getText().toString());
                        if(score <= 5.0){
                            String ratingID = databaseReference.child("rating").push().getKey();

                            Rating rating = new Rating(desc,ratingID,score,UID);
                            databaseReference.child("rating").child(ratingID).setValue(rating);


                            Intent i=new Intent(StudentReview.this, Home.class);
                            i.putExtra("uid",UID);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast toast=Toast.makeText(getApplicationContext(),"Please enter the Right score (0.0-5.0)",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }catch(Exception e) {
                        Toast toast=Toast.makeText(getApplicationContext(),"Please enter the Right Score (0.0-5.0)",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
    }

    public void InitialiseView(){
        Review = findViewById(R.id.stuentReviewReview);
        Rating = findViewById(R.id.studentReviewRating);
        Submit = findViewById(R.id.studentReviewSubmit);
    }
}
