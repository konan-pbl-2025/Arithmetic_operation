package com.example.arithmetic_operation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Button backButton = (Button)findViewById(R.id.Back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(result.this,Start.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String timer = intent.getStringExtra("timer");

        TextView textView = findViewById(R.id.Time);
        textView.setText("タイム: "+ timer);

        Button retry = findViewById(R.id.Retry);
        retry.setOnClickListener(v -> finish());

        }
    }
}