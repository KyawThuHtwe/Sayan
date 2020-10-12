package com.cu.sayan.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
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
                    if(!res){
                        setCount("unicode");
                    }
                    zawgyiFont(res);
                    if(getCount().equals("unicode")){
                        reLoading();
                    }
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        Cursor cursor1=helper.getType();
        if(cursor1.getCount()>0){
            while (cursor1.moveToNext()){
                if(!isZawgyiFont()){
                    helper.updateType(cursor1.getString(0)+"", Rabbit.zg2uni(cursor1.getString(1))+"",Rabbit.zg2uni(cursor1.getString(2)+""));
                }else {
                    helper.updateType(cursor1.getString(0)+"", Rabbit.uni2zg(cursor1.getString(1))+"",Rabbit.uni2zg(cursor1.getString(2)+""));
                }
            }
        }
        Cursor cursor2=helper.getSayan();
        if(cursor2.getCount()>0){
            while (cursor2.moveToNext()){
                if(!isZawgyiFont()){
                    helper.updateSayan(cursor2.getString(0)+"", Rabbit.zg2uni(cursor2.getString(1))+"",Rabbit.zg2uni(cursor2.getString(2)+""),Rabbit.zg2uni(cursor2.getString(3)+""),Rabbit.zg2uni(cursor2.getString(4)+""),cursor2.getString(5)+"");
                }else {
                    helper.updateSayan(cursor2.getString(0)+"", Rabbit.uni2zg(cursor2.getString(1))+"",Rabbit.uni2zg(cursor2.getString(2)+""),Rabbit.uni2zg(cursor2.getString(3)+""),Rabbit.uni2zg(cursor2.getString(4)+""),cursor2.getString(5)+"");
                }
            }
        }
    }
    public String getCount(){
        SharedPreferences preferences=getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getString("Choose","zawgyi");
    }
    public void setCount(String font){
        SharedPreferences preferences=getSharedPreferences("Font", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Choose",font).apply();
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