package com.cu.sayan.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.R;

import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class FontActivity extends AppCompatActivity {

    RadioGroup font;
    boolean res=true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_font);
        try {
            font = findViewById(R.id.font);
            font.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.zawgyi) {
                        res = true;
                        font.check(R.id.zawgyi);
                    } else if (checkedId == R.id.unicode) {
                        res = false;
                        font.check(R.id.unicode);
                    }
                }
            });
            LinearLayout submit = findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zawgyiFont(res);
                    reLoading();
                }
            });
            if (isZawgyiFont()) {
                font.check(R.id.zawgyi);
            } else {
                font.check(R.id.unicode);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    public void reLoading(){
        notOnce(true);
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        helper.deleteTypeTable();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public void notOnce(boolean res){
        SharedPreferences preferences=getSharedPreferences("Exit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("exit",res).apply();
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= getSharedPreferences("Font",Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }

    public void zawgyiFont(boolean res){
        SharedPreferences preferences=getSharedPreferences("Font",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("Font",res);
        editor.apply();
    }
}