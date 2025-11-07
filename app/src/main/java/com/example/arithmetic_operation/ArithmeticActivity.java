package com.example.arithmetic_operation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ArithmeticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arithmetic);
    }

    public static int Plus(int x,int y){
        int ans=x+y;
        return ans;
    }
    public static int Minus(int x,int y){
        int ans=x-y;
        return ans;
    }
}