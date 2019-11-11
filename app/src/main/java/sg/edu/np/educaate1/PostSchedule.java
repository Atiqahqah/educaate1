package sg.edu.np.educaate1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostSchedule extends AppCompatActivity {
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_schedule);
    }

    public void onClick(View v){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        Booking booking=new Booking();
        EditText time=findViewById(R.id.timeField);
        EditText date=findViewById(R.id.dateField);
        EditText subj=findViewById(R.id.subjField);
        EditText desc=findViewById(R.id.descField);
        EditText price=findViewById(R.id.priceField);
        EditText location=findViewById(R.id.locationField);

        booking.setDate(time.getText().toString());
        booking.setTime(date.getText().toString());
        booking.setSubject(subj.getText().toString());
        booking.setDesc(desc.getText().toString());
        booking.setPrice(price.getText().toString());
        booking.setLocation(location.getText().toString());

        databaseReference.child("users").child(user.getUid()).child("booking").setValue(booking);
        databaseReference.child("booking").child(user.getUid()).setValue(booking);
    }
}
