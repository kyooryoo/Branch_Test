package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        TextView textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        String channel = intent.getStringExtra("channel");
        textView.setText(
                (link != null && channel != null) ?
                Html.fromHtml(String.format(
                        "<b>Following URL shared via %s:</b><br>%s", channel, link)) :
                        "Restart app or go back to share a link"
        );

        Button backButton=findViewById(R.id.button2);
        backButton.setOnClickListener(v -> finish());
    }
}