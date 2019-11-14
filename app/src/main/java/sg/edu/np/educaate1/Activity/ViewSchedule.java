package sg.edu.np.educaate1.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

public class ViewSchedule extends AppCompatActivity {

    private Button bookBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String strName;
    String strSubj;
    String strLocation;
    String strDate;
    String strTime;
    String strPrice;
    String strDesc;
    String strId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        Intent i=getIntent();

        strName = i.getStringExtra("name");
        TextView name=findViewById(R.id.nameFld);
        name.setText(i.getStringExtra("name"));

        strSubj = i.getStringExtra("subj");
        TextView subj=findViewById(R.id.subjFld);
        subj.setText(i.getStringExtra("subj"));

        strLocation = i.getStringExtra("location");
        TextView location=findViewById(R.id.locationFld);
        location.setText(i.getStringExtra("location"));

        strDate = i.getStringExtra("date");
        strTime = i.getStringExtra("time");
        TextView datetime=findViewById(R.id.datetimeFld);
        datetime.setText(i.getStringExtra("date")+" "+i.getStringExtra("time"));

        strPrice = i.getStringExtra("price");
        TextView price=findViewById(R.id.priceFld);
        price.setText("$"+i.getStringExtra("price"));

        strDesc = i.getStringExtra("desc");
        TextView desc=findViewById(R.id.descFld);
        desc.setText(i.getStringExtra("desc"));

        strId = i.getStringExtra("id");

        bookBtn = findViewById(R.id.bookScheduleBtn);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConfirmation();//when user clicks on the delete button, displayConfirmation() will be called and alert dialog will be displayed
            }
        });
    }

    public void displayConfirmation(){
        new AlertDialog.Builder(ViewSchedule.this)//alert dialog
                .setTitle("Confirm?")
                .setMessage("Book this schedule?")
                .setPositiveButton("Yes",//option for yes
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference= FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                                Booking booking=new Booking();

                                booking.setDate(strDate);
                                booking.setTime(strTime);
                                booking.setSubject(strSubj);
                                booking.setDesc(strDesc);
                                booking.setPrice(strPrice);
                                booking.setLocation(strLocation);
                                booking.setName(strName);
                                booking.setId(strId);
                                booking.setStatus("Pending");

                                databaseReference.child("users").child(user.getUid()).child("booking").child(strId).setValue(booking);
                                databaseReference.child("booking").child(user.getUid()).push().setValue(booking);
                            }
                        })
                .setNegativeButton("Cancel",//option for cancel
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();//closes the alert dialog
                            }
                        })
                .show();
    }
}
