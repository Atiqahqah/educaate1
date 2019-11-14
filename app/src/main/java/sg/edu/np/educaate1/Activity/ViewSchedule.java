package sg.edu.np.educaate1.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import sg.edu.np.educaate1.R;

public class ViewSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        Intent i=getIntent();
        TextView name=findViewById(R.id.nameFld);
        name.setText(i.getStringExtra("name"));
        TextView subj=findViewById(R.id.subjFld);
        subj.setText(i.getStringExtra("subj"));
        TextView location=findViewById(R.id.locationFld);
        location.setText(i.getStringExtra("location"));
        TextView datetime=findViewById(R.id.datetimeFld);
        datetime.setText(i.getStringExtra("date")+" "+i.getStringExtra("time"));
        TextView price=findViewById(R.id.priceFld);
        price.setText("$"+i.getStringExtra("price"));
        TextView desc=findViewById(R.id.descFld);
        desc.setText(i.getStringExtra("desc"));

    }
}
