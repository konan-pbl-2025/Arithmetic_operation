package com.example.arithmetic_operation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button easyButton = (Button)findViewById(R.id.easy);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,easy.class);
                startActivity(intent);
            }
        });

        Button nomalButtton = (Button)findViewById(R.id.nomal);
        nomalButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,arithmetic.class);
                startActivity(intent);
            }
        });

        Button hardButtton = (Button)findViewById(R.id.hard);
        hardButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Start.this,hard.class);
                startActivity(intent);
            }
        });
    }
}