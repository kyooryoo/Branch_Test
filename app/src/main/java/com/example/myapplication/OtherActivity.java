package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        TextView textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String text = "Restart app or go back to share a link";
        String link = intent.getStringExtra("link");
        String channel = intent.getStringExtra("channel");
        if (link != null && channel != null) {
            text = "Deep Link shared via " + channel + ":\n" + link;
        }
        textView.setText(text);
    }
}