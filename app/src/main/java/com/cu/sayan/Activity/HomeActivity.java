package com.cu.sayan.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.cu.sayan.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(isExist()){
            finish();
            startActivity(new Intent(getApplicationContext(),PasswordActivity.class));
        }else{
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }
    public boolean isExist(){
        SharedPreferences preferences=getSharedPreferences("Password", Context.MODE_PRIVATE);
        return preferences.getBoolean("Exit",false);
    }
}