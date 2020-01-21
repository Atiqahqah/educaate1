package sg.edu.np.educaate1.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sg.edu.np.educaate1.R;

public class PaymentOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        Button cashBtn = findViewById(R.id.paymentCashBtn);
        Button cardBtn = findViewById(R.id.PaymentCardBtn);


        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOption.this,PaymentConfirm.class);
                intent.putExtra("paymenttype","Cash");
                startActivity(intent);
            }
        });

        cardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOption.this,PaymentConfirm.class);
                intent.putExtra("paymenttype","Card");
                startActivity(intent);
            }
        });
    }
}
