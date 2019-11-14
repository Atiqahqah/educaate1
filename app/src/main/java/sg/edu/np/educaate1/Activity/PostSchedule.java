package sg.edu.np.educaate1.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

public class PostSchedule extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_schedule);

        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name=dataSnapshot.child(user.getUid()).child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        booking.setDate(date.getText().toString());
        booking.setTime(time.getText().toString());
        booking.setSubject(subj.getText().toString());
        booking.setDesc(desc.getText().toString());
        booking.setPrice(price.getText().toString());
        booking.setLocation(location.getText().toString());
        booking.setName(name);

        String key = databaseReference.child("users").child(user.getUid()).child("booking").push().getKey();
        booking.setId(key);
        booking.setStatus("Open");

        databaseReference.child("users").child(user.getUid()).child("booking").child(key).setValue(booking);
        databaseReference.child("booking").child(user.getUid()).push().setValue(booking);
    }
}
