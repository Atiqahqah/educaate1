package sg.edu.np.educaate1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import sg.edu.np.educaate1.Activity.Home;
import sg.edu.np.educaate1.Activity.OthersProfile;
import sg.edu.np.educaate1.Activity.StudentMessage;
import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.Classes.Tutor;

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
    String tutorId;
    String studentId;

    Button chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_booking);

        Intent i=getIntent();//get intent from

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

        tutorId=i.getStringExtra("tutorid");
        studentId=i.getStringExtra("studentid");

        strType=i.getStringExtra("type");

        strStatus=i.getStringExtra("status");
        Log.d("Status", strStatus);

        //get price

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();

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

        Button backBtn = findViewById(R.id.delBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                Intent i=new Intent(DeleteBooking.this, Home.class);
                i.putExtra("uid",user.getUid());
                startActivity(i);
                finish();
            }
        });

        ImageView profile = findViewById(R.id.delProfilePicture);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                Intent i=new Intent(DeleteBooking.this, OthersProfile.class);

                if(user.getUid().equals(tutorId)) {
                    i.putExtra("postUID",studentId);
                }
                else if (user.getUid().equals(studentId)){
                    i.putExtra("postUID",tutorId);
                }
                startActivity(i);
            }
        });

        Log.d("userid",user.getUid());
        Log.d("student",tutorId);

        chatBtn=findViewById(R.id.button5);
        chatBtn.setOnClickListener(new View.OnClickListener() {//this is currently for students only
            @Override
            public void onClick(View view) {
                if(user.getUid().equals(studentId)){
                    Intent i=new Intent(DeleteBooking.this, StudentMessage.class);
                    i.putExtra("id",strId);
                    i.putExtra("tutorid",tutorId);
                    i.putExtra("studentid",studentId);

                    i.putExtra("name",strName);
                    i.putExtra("date",strDate);
                    i.putExtra("time",strTime);
                    i.putExtra("desc",strDesc);
                    i.putExtra("location",strLocation);
                    i.putExtra("price",strPrice);
                    i.putExtra("subj",strSubj);
                    i.putExtra("type",strType);

                    startActivity(i);
                }
                else if(user.getUid().equals(tutorId)){
                    Intent i=new Intent(DeleteBooking.this, TutorMessage.class);
                    i.putExtra("id",strId);
                    i.putExtra("tutorid",tutorId);
                    i.putExtra("studentid",studentId);

                    i.putExtra("name",strName);
                    i.putExtra("date",strDate);
                    i.putExtra("time",strTime);
                    i.putExtra("desc",strDesc);
                    i.putExtra("location",strLocation);
                    i.putExtra("price",strPrice);
                    i.putExtra("subj",strSubj);
                    i.putExtra("type",strType);

                    startActivity(i);
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