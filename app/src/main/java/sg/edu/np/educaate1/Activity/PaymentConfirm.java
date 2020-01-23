package sg.edu.np.educaate1.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

public class PaymentConfirm extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView Payee;
    TextView Payer;
    TextView Date;
    TextView PaymentType;
    TextView Price;
    Button Confirm;

    String tutorId;
    String studentId;
    String id;
    String chatID;

    String strName;
    String strSubj;
    String strLocation;
    String strDate;
    String strTime;
    String strPrice;
    String strDesc;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UID = "Uid";

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        Intent i = getIntent();
        tutorId=i.getStringExtra("tutorid");
        studentId=i.getStringExtra("studentid");
        id=i.getStringExtra("bookingid");

        strName=i.getStringExtra("name");
        strDate=i.getStringExtra("date");
        strDesc=i.getStringExtra("desc");
        strPrice=i.getStringExtra("price");
        strLocation=i.getStringExtra("name");
        strSubj=i.getStringExtra("subj");
        strTime=i.getStringExtra("time");

        Payee = findViewById(R.id.confirmPaymentPayee);
        Payee.setText(strName);

        //Payer = findViewById(R.id.confirmPaymentPayer);

        //Date = findViewById(R.id.confirmPaymentDate);
        //PaymentType = findViewById(R.id.confirmPaymentType);
        Price = findViewById(R.id.confirmPaymentPrice);
        Price.setText(strPrice);

        Spinner mySpinner=(Spinner)findViewById(R.id.paymentMethod);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.paymentMethods,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(PaymentConfirm.this);
    }

    public void InitialiseView(){
        Payee = findViewById(R.id.confirmPaymentPayee);
        Price = findViewById(R.id.confirmPaymentPrice);
        Confirm = findViewById(R.id.confirmPaymentBtn);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
