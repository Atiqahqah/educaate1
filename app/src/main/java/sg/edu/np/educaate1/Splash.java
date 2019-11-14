package sg.edu.np.educaate1;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1800);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null && mRunnable != null)
        mHandler.removeCallbacks(mRunnable);
    }
}
