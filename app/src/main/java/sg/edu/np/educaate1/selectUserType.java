package sg.edu.np.educaate1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class selectUserType extends AppCompatActivity {
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
    }

    public void onStudent(View v){
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString("USER","student");
        editor.apply();
        Intent intent=new Intent(selectUserType.this,SignUp.class);
        startActivity(intent);
    }

    public void onTutor(View v){
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString("USER","tutor");
        editor.apply();
        Intent intent=new Intent(selectUserType.this,SignUp.class);
        startActivity(intent);
    }
}
