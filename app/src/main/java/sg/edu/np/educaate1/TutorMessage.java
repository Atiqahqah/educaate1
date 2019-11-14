package sg.edu.np.educaate1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TutorMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_message);
    }

    public void send(View v){
        EditText text=findViewById(R.id.editText);
        TextView sentmsg=findViewById(R.id.textView8);
        String msg=text.getText().toString();
        if(msg!=null){
            sentmsg.setText("success");
        }
        else{
            sentmsg.setText("write a message");
        }
    }

    public void cancel(View v){
        Intent intent=new Intent(TutorMessage.this,TutorViewSchedule.class);
        startActivity(intent);
    }
}
