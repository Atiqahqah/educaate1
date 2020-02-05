package sg.edu.np.educaate1.Activity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import sg.edu.np.educaate1.Classes.Chat;
import sg.edu.np.educaate1.Fragments.StudentHomeFragment;
import sg.edu.np.educaate1.Fragments.TutorHomeFragment;
import sg.edu.np.educaate1.R;

public class ViewSchedule extends AppCompatActivity {

    private Button bookBtn;
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
    String strTutorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        Intent i=getIntent();//get intent from student/tutor home fragment

        strName = i.getStringExtra("name");
        TextView name=findViewById(R.id.nameFld);
        name.setText(strName);

        strSubj = i.getStringExtra("subj");
        TextView subj=findViewById(R.id.subjFld);
        subj.setText(strSubj);

        strLocation = i.getStringExtra("location");
        TextView location=findViewById(R.id.locationFld);
        location.setText(strLocation);

        strDate = i.getStringExtra("date");
        strTime = i.getStringExtra("time");
        TextView datetime=findViewById(R.id.datetimeFld);
        datetime.setText(strDate+" "+strTime);

        strPrice = i.getStringExtra("price");
        TextView price=findViewById(R.id.priceFld);
        price.setText("$"+strPrice);

        strDesc = i.getStringExtra("desc");
        TextView desc=findViewById(R.id.descFld);
        desc.setText(strDesc);

        strId = i.getStringExtra("id");

        strTutorId=i.getStringExtra("tutorid");

        strType=i.getStringExtra("type");

        strStatus=i.getStringExtra("status");

        bookBtn = findViewById(R.id.bookScheduleBtn);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConfirmation();//when user clicks on the delete button, displayConfirmation() will be called and alert dialog will be displayed
            }


        });


        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                Intent i=new Intent(ViewSchedule.this, Home.class);
                i.putExtra("uid",user.getUid());
                startActivity(i);
                finish();
            }
        });

        ImageView profile = findViewById(R.id.profilePicture);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                Intent i=new Intent(ViewSchedule.this, OthersProfile.class);
                i.putExtra("postUID",strTutorId);
                startActivity(i);
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
                                booking.setType(strType);
                                booking.setTutorid(strTutorId);

                                //Log.d(TAG,strType);
                                if(strType.equals("Student")){
                                    Chat chat=new Chat();
                                    chat.setStudentID(strTutorId);
                                    String key=databaseReference.child("chat").child(strId).push().getKey();
                                    chat.setId(key);
                                    chat.setTutorID(user.getUid());
                                    chat.setBookingType(strType);
                                    databaseReference.child("chat").child(strId).child(key).setValue(chat);
                                }
                                else{
                                    Chat chat=new Chat();
                                    chat.setStudentID(user.getUid());
                                    String key=databaseReference.child("chat").child(strId).push().getKey();
                                    chat.setId(key);
                                    chat.setTutorID(strTutorId);
                                    chat.setBookingType(strType);
                                    databaseReference.child("chat").child(strId).child(key).setValue(chat);
                                }


                                databaseReference.child("users").child(user.getUid()).child("booking").child(strId).setValue(booking);
                                Toast.makeText(getApplicationContext(),"Scheduled has been booked!",Toast.LENGTH_LONG).show();




                                /*Intent intent=new Intent(ViewSchedule.this,StudentMessage.class);

                                intent.putExtra("id",strId);
                                intent.putExtra("tutorid",strTutorId);
                                intent.putExtra("studentid",user.getUid());

                                startActivity(intent);*/
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

    public void backBtn(View v){
        if(strType.equals("Student")){
            //Intent i=new Intent(ViewSchedule.this, TutorHomeFragment.class);
            //startActivity(i);
        }
        else{

            //Intent i=new Intent(ViewSchedule.this,StudentHomeFragment.class);
            //startActivity(i);
        }
    }

}
