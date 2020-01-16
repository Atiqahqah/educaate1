package sg.edu.np.educaate1.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import sg.edu.np.educaate1.R;

public class PaymentConfirm extends AppCompatActivity {
    TextView Payee;
    TextView Payer;
    TextView Date;
    TextView PaymentType;
    TextView Price;
    Button Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        InitialiseView();


    }

    public void InitialiseView(){
        Payee = findViewById(R.id.confirmPaymentPayee);
        Payer = findViewById(R.id.confirmPaymentPayer);
        Date = findViewById(R.id.confirmPaymentDate);
        PaymentType = findViewById(R.id.confirmPaymentType);
        Price = findViewById(R.id.confirmPaymentPrice);
        Confirm = findViewById(R.id.confirmPaymentBtn);
    }

}
