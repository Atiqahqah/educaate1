package sg.edu.np.educaate1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void signUp(View v){
        Button button=findViewById(R.id.signup);
        Intent intent=new Intent(MainActivity.this,selectUserType.class);
        startActivity(intent);
    }

    public void logIn(View v){
        Button button=findViewById(R.id.login);
        Intent intent=new Intent(MainActivity.this,LogIn.class);
        startActivity(intent);
    }

    public void etPhoneHome(View v){
        Button button=findViewById(R.id.homeBtn);
        Intent intent=new Intent(MainActivity.this,Home.class);
        startActivity(intent);
    }
}
