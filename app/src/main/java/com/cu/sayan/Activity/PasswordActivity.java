package com.cu.sayan.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.R;

import java.util.Objects;

public class PasswordActivity extends AppCompatActivity {

    TextView password;
    TextView p1,p2,p3,p4;
    TextView k1,k2,k3,k4,k5,k6,k7,k8,k9,k0;
    ImageView backspace;
    String current_password,confirm_password;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_password);
        password=findViewById(R.id.password);
        p1=findViewById(R.id.p1);
        p2=findViewById(R.id.p2);
        p3=findViewById(R.id.p3);
        p4=findViewById(R.id.p4);
        k0=findViewById(R.id.k0);
        k1=findViewById(R.id.k1);
        k2=findViewById(R.id.k2);
        k3=findViewById(R.id.k3);
        k4=findViewById(R.id.k4);
        k5=findViewById(R.id.k5);
        k6=findViewById(R.id.k6);
        k7=findViewById(R.id.k7);
        k8=findViewById(R.id.k8);
        k9=findViewById(R.id.k9);
        backspace=findViewById(R.id.delete);
        keyPress(k0);
        keyPress(k1);
        keyPress(k2);
        keyPress(k3);
        keyPress(k4);
        keyPress(k5);
        keyPress(k6);
        keyPress(k7);
        keyPress(k8);
        keyPress(k9);
        backspace.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        if(!isZawgyiFont()){
            password.setText(Rabbit.zg2uni(getResources().getString(R.string.CurrentP)));
        }else {
            password.setText(getResources().getString(R.string.CurrentP));
        }

    }
    public void keyPress(final TextView key){
        key.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                has(key.getText().toString());
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void has(String keyPress){

        if(p1.getText().equals("")){
            if(isExist()){
                if(!isZawgyiFont()){
                    password.setText(Rabbit.zg2uni(getResources().getString(R.string.CurrentP)));
                }else {
                    password.setText(getResources().getString(R.string.CurrentP));
                }
            }
            p1.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            p1.setText(keyPress);
        }else{
            if(p2.getText().equals("")){
                p2.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                p2.setText(keyPress);
            }else {
                if(p3.getText().equals("")){
                    p3.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    p3.setText(keyPress);
                }else {
                    if(p4.getText().equals("")){
                        p4.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                        p4.setText(keyPress);
                        if(!p4.getText().equals("")){
                            if(current_password==null){
                                if(isExist()){
                                    current_password=p1.getText().toString()+p2.getText().toString()+p3.getText().toString()+p4.getText().toString();
                                    confirm_password=readPassword();
                                    if(confirm_password.equals(current_password)){
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();
                                    }else {
                                        if(!isZawgyiFont()){
                                            password.setText(Rabbit.zg2uni(getResources().getString(R.string.WrongP)));
                                        }else {
                                            password.setText(getResources().getString(R.string.WrongP));
                                        }
                                        current_password=null;
                                        p1.setText("");
                                        p2.setText("");
                                        p3.setText("");
                                        p4.setText("");
                                        p4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                        p3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                        p2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                        p1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    }
                                }else {
                                    current_password=p1.getText().toString()+p2.getText().toString()+p3.getText().toString()+p4.getText().toString();
                                    if(!isZawgyiFont()){
                                        password.setText(Rabbit.zg2uni(getResources().getString(R.string.ConfirmP)));
                                    }else {
                                        password.setText(getResources().getString(R.string.ConfirmP));
                                    }
                                    p1.setText("");
                                    p2.setText("");
                                    p3.setText("");
                                    p4.setText("");
                                    p4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    p3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    p2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    p1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                }
                            }else {
                                confirm_password=p1.getText().toString()+p2.getText().toString()+p3.getText().toString()+p4.getText().toString();
                                if(current_password.equals(confirm_password)){
                                    Toast.makeText(getApplicationContext(),"Password Access",Toast.LENGTH_SHORT).show();
                                    savePassword(confirm_password,true);
                                    finish();
                                }else {
                                    if(!isZawgyiFont()){
                                        password.setText(Rabbit.zg2uni(getResources().getString(R.string.WrongP)));
                                    }else {
                                        password.setText(getResources().getString(R.string.WrongP));
                                    }
                                    current_password=null;
                                    p1.setText("");
                                    p2.setText("");
                                    p3.setText("");
                                    p4.setText("");
                                    p4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    p3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    p2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                    p1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void savePassword(String password,boolean res){
        SharedPreferences preferences=getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Password",password);
        editor.putBoolean("Exit",res);
        editor.apply();
    }
    public boolean isExist(){
        SharedPreferences preferences=getSharedPreferences("Password", Context.MODE_PRIVATE);
        return preferences.getBoolean("Exit",false);
    }
    public String readPassword(){
        SharedPreferences preferences=getSharedPreferences("Password", Context.MODE_PRIVATE);
        return preferences.getString("Password","");
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void delete(){
        if(!p4.getText().equals("")){
            p4.setText("");
            p4.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }else {
            if(!p3.getText().equals("")){
                p3.setText("");
                p3.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }else {
                if(!p2.getText().equals("")){
                    p2.setText("");
                    p2.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }else {
                    if(!p1.getText().equals("")){
                        p1.setText("");
                        p1.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    }
                }
            }
        }
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}