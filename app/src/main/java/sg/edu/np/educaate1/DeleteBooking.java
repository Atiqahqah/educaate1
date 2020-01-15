package sg.edu.np.educaate1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.edu.np.educaate1.Classes.Booking;

public class DeleteBooking extends AppCompatActivity {

    private Button delBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String TAG;
    String strName;
    String strSubj;
    String strLocation;
    String strDate;
    String strTime;
    String strPrice;
    String strDesc;
    String strId;
    String strType;
    String strStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_booking);

        Intent i=getIntent();

        strName = i.getStringExtra("name");
        TextView name=findViewById(R.id.delNameFld);
        name.setText(strName);

        strSubj = i.getStringExtra("subj");
        TextView subj=findViewById(R.id.delSubjFld);
        subj.setText(strSubj);

        strLocation = i.getStringExtra("location");
        TextView location=findViewById(R.id.delLocationFld);
        location.setText(strLocation);

        strDate = i.getStringExtra("date");
        strTime = i.getStringExtra("time");
        TextView datetime=findViewById(R.id.delDatetimeFld);
        datetime.setText(strDate+" "+strTime);

        strPrice = i.getStringExtra("price");
        TextView price=findViewById(R.id.delPriceFld);
        price.setText("$"+strPrice);

        strDesc = i.getStringExtra("desc");
        TextView desc=findViewById(R.id.delDescFld);
        desc.setText(strDesc);

        strId = i.getStringExtra("id");

        strType=i.getStringExtra("type");

        strStatus=i.getStringExtra("status");
        Log.d("Status", strStatus);

        delBtn = findViewById(R.id.delScheduleBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(strStatus.equals("Confirm")) {
                    displayConfirmStatus();
                }
                else {
                    displayDelConfirmation();
                }
            }
        });
    }

    public void displayDelConfirmation(){
        new AlertDialog.Builder(DeleteBooking.this)//alert dialog
                .setTitle("Confirm?")
                .setMessage("Delete this schedule?")
                .setPositiveButton("Yes",//option for yes
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference= FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                                databaseReference.child("users").child(user.getUid()).child("booking").child(strId).removeValue();
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

    public void displayConfirmStatus(){
        new AlertDialog.Builder(DeleteBooking.this)//alert dialog
                .setTitle("Unable to perform action")
                .setMessage("Please talk to the tutor to cancel the booking.")
                .setPositiveButton("Noted",//option for yes
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();//closes the alert dialog
                            }
                        })
                .show();
    }
}