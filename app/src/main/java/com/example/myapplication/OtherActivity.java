package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        TextView textView = findViewById(R.id.textView);
        String link = getIntent().getStringExtra("link");
        String channel = getIntent().getStringExtra("channel");
        String new_text = "Deep Link shared via " + channel + ":\n" + link;
        textView.setText(new_text);
    }
}