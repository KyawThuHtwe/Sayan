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
        if(isOnce()){
            finish();
            Once(false);
            startActivity(new Intent(getApplicationContext(),FontActivity.class));
        }else {
            if(isExist()){
                finish();
                startActivity(new Intent(getApplicationContext(),PasswordActivity.class));
            }else{
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }
    }
    public boolean isExist(){
        SharedPreferences preferences=getSharedPreferences("Password", Context.MODE_PRIVATE);
        return preferences.getBoolean("Exit",false);
    }
    public void Once(boolean res){
        SharedPreferences preferences=getSharedPreferences("Font", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("Once",res).apply();
    }
    public boolean isOnce(){
        SharedPreferences preferences=getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Once",true);
    }
}