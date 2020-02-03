package sg.edu.np.educaate1.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

import sg.edu.np.educaate1.Classes.Booking;
import sg.edu.np.educaate1.R;

public class PostSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String name;
    TextView errormsg;
    String timing;
    String date;
    TextView datepicker;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_schedule);

        errormsg=findViewById(R.id.textView11);
        errormsg.setText("");

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

        Spinner mySpinner=(Spinner)findViewById(R.id.timeSpinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.time,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(PostSchedule.this);

        //date picker
        datepicker=findViewById(R.id.datepicker);
        datepicker.setText("Select Date");

        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal=Calendar.getInstance();//When the user presses on the date picker, the date picker will show the current date
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(
                        PostSchedule.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());//User is unable to select a date before the current date
                dialog.show();
            }
        });

        //to display the date that the user has selected
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;//The month starts from 0 instead of 1 so january is 0 instead of 1
                date=day+"/"+month+"/"+year;
                datepicker.setText(date);//The textview will display the date chosen instead of "Select Date"
            }
        };
    }

    public void onClick(View v){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        Booking booking=new Booking();
        EditText hour=findViewById(R.id.timeField);
        //EditText date=findViewById(R.id.dateField);
        EditText min=findViewById(R.id.editText);
        EditText subj=findViewById(R.id.subjField);
        EditText desc=findViewById(R.id.descField);
        EditText price=findViewById(R.id.priceField);
        EditText location=findViewById(R.id.locationField);

        String time=hour.getText().toString()+":"+min.getText().toString()+timing;

        if(hour.getText().toString().equals("")||datepicker.getText().toString().equals("Select Date")||subj.getText().toString().equals("")
        ||desc.getText().toString().equals("")||price.getText().toString().equals("")||min.getText().toString().equals("")||location.getText().toString().equals("")){
            errormsg.setText("Please enter all details");
        }
        else if(Integer.parseInt(hour.getText().toString())>12||Integer.parseInt(min.getText().toString())>59||Integer.parseInt(hour.getText().toString())<1){
            errormsg.setText("Please enter the correct time");
            hour.setText("");
            min.setText("");
        }
        else{
            errormsg.setText("");

            booking.setDate(date);
            booking.setTime(time);
            booking.setSubject(subj.getText().toString());
            booking.setDesc(desc.getText().toString());
            booking.setPrice(price.getText().toString());
            booking.setLocation(location.getText().toString());
            booking.setName(name);
            booking.setStatus("Open");
            booking.setType("Tutor");
            booking.setTutorid(user.getUid());

            //get booking id
            String key=databaseReference.child("users").child(user.getUid()).child("booking").push().getKey();
            booking.setId(key);

            databaseReference.child("users").child(user.getUid()).child("booking").child(key).setValue(booking);
            Toast.makeText(this,"Schedule has been posted!",Toast.LENGTH_LONG).show();
        }
        //databaseReference.child("booking").child(user.getUid()).push().setValue(booking);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        timing=adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        timing="AM";
    }
}
