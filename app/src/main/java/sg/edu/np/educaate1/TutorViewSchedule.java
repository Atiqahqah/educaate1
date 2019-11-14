package sg.edu.np.educaate1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.educaate1.Classes.Student;

public class TutorViewSchedule extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<Student> studentList;
    ArrayAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_schedule);

        Intent i=getIntent();
        TextView subj=findViewById(R.id.tSubj);
        subj.setText(i.getStringExtra("subj"));
        TextView location=findViewById(R.id.tLoc);
        location.setText(i.getStringExtra("location"));
        TextView datetime=findViewById(R.id.tDateTime);
        datetime.setText(i.getStringExtra("date")+" "+i.getStringExtra("time"));
        TextView price=findViewById(R.id.tPrice);
        price.setText("$"+i.getStringExtra("price"));
        TextView desc=findViewById(R.id.tDesc);
        desc.setText(i.getStringExtra("desc"));

        final String id=i.getStringExtra("id");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        studentList=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("type").getValue().toString().equals("student")){
                        for(DataSnapshot msgSnapshot:snapshot.child("booking").getChildren()){
                            if(id.equals(msgSnapshot.getKey())){
                                Student s=snapshot.getValue(Student.class);
                                studentList.add(s);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter=new RequestAdapter(this,R.layout.requestlayout,studentList);
        listView=(ListView)findViewById(R.id.requestList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Student b = (Student) parent.getItemAtPosition(position);
                Intent intent = new Intent(TutorViewSchedule.this,
                        TutorMessage.class);

                intent.putExtra("name",b.getName());
                /*SharedPreferences.Editor editor=pref.edit();
                editor.putString("DATE",b.getDate());
                editor.apply();*/

                startActivity(intent);
            }
        });
    }
}
