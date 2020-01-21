package sg.edu.np.educaate1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

public class PaymentConfirm extends AppCompatActivity {
    TextView Payee;
    TextView Payer;
    TextView Date;
    TextView PaymentType;
    TextView Price;
    Button Confirm;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        Intent intent = getIntent();
        SharedPreferences pref = getSharedPreferences(MyPREFERENCES, 0); // 0 - for private mode
        InitialiseView();
        String bookingid = pref.getString("bookingid","");
        String uid = pref.getString("uid","");
        String tuid = pref.getString("tuid","");
        String paymenttype = intent.getStringExtra("paymenttype");

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("booking").child(bookingid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                booking = dataSnapshot.getValue(Booking.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        PaymentType.setText(paymenttype);


    }

    public void InitialiseView(){
        Payee = findViewById(R.id.confirmPaymentPayee);
        Payer = findViewById(R.id.confirmPaymentPayer);
        Date = findViewById(R.id.confirmPaymentDate);
        PaymentType = findViewById(R.id.confirmPaymentType);
        Price = findViewById(R.id.confirmPaymentPrice);
        Confirm = findViewById(R.id.confirmPaymentBtn);
    }

}
