package com.example.pavinaveen.slidingtab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class double_Activity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double);

        textView = findViewById(R.id.textView2);

        Intent intent = getIntent();
        textView.setText(intent.getStringExtra(toll_registration.MESSAGE));

    }
}
