package com.example.zoir.fibonachi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int num1 = 1;
    int num2 = 1;

    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.left_btn);
        b2 = (Button) findViewById(R.id.right_btn);


        final TextView text = (TextView) findViewById(R.id.num);

        b1.setText("1");
        b2.setText("1");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(text.getText() + " " + String.valueOf(num1+num2));
                int c = num1+num2;
                num1 = num2;
                num2 = c;
                b2.setText(String.valueOf(num1+num2));
                b1.setEnabled(false);
                b2.setEnabled(true);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(text.getText() + " " + String.valueOf(num1+num2));
                int c = num1+num2;
                num1 = num2;
                num2 = c;
                b1.setText(String.valueOf(num1+num2));
                b2.setEnabled(false);
                b1.setEnabled(true);
            }
        });
    }
}
