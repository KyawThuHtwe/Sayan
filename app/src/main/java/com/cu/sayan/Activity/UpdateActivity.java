package com.cu.sayan.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.R;

import java.util.ArrayList;
import java.util.Objects;

public class UpdateActivity extends AppCompatActivity {

    ImageView back;
    TextView insert,date,type;
    EditText title,amount;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_update);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type=findViewById(R.id.type);
        final RadioGroup radioGroup=findViewById(R.id.radio);
        date=findViewById(R.id.date);
        title=findViewById(R.id.title);
        amount=findViewById(R.id.amount);
        insert=findViewById(R.id.insert);

        Intent intent=getIntent();
        final String get_id=intent.getStringExtra("Id");
        final String get_category=intent.getStringExtra("Category");
        String get_type=intent.getStringExtra("Type");
        String get_title=intent.getStringExtra("Title");
        String get_amount=intent.getStringExtra("Amount");
        final String get_date=intent.getStringExtra("Date");
        if(!isZawgyiFont()){
            if(get_category.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                radioGroup.check(R.id.outcome);
            }else if(get_category.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                radioGroup.check(R.id.income);
            }
        }else{
            if(get_category.equals("ထြက္ေငြ")){
                radioGroup.check(R.id.outcome);
            }else if(get_category.equals("ဝင္ေငြ")){
                radioGroup.check(R.id.income);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!isZawgyiFont()){
                    if(get_category.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                        radioGroup.check(R.id.outcome);
                    }else if(get_category.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                        radioGroup.check(R.id.income);
                    }
                }else {
                    if(get_category.equals("ထြက္ေငြ")){
                        radioGroup.check(R.id.outcome);
                    }else if(get_category.equals("ဝင္ေငြ")){
                        radioGroup.check(R.id.income);
                    }
                }
            }
        });
        type.setText(get_type);
        title.setText(get_title);
        amount.setText(get_amount);
        int day= Integer.parseInt(get_date.split("/")[0]);
        int month= Integer.parseInt(get_date.split("/")[1]);
        int year= Integer.parseInt(get_date.split("/")[2]);

        date.setText(monthChange(month)+" ၊ "+DYChange(day+"")+"ရက္"+" ၊ "+DYChange(year+"")+"ႏွစ္");
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(get_id,get_category,type.getText().toString(),title.getText().toString(),amount.getText().toString(),get_date);
            }
        });
        if(!isZawgyiFont()){
            TextView t1=findViewById(R.id.t1);
            TextView t2=findViewById(R.id.t2);
            TextView t3=findViewById(R.id.t3);
            TextView t4=findViewById(R.id.t4);
            TextView t5=findViewById(R.id.t5);
            TextView t6=findViewById(R.id.t6);
            RadioButton income=findViewById(R.id.income);
            RadioButton outcome=findViewById(R.id.outcome);
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            t2.setText(Rabbit.zg2uni(t2.getText().toString()));
            t3.setText(Rabbit.zg2uni(t3.getText().toString()));
            t4.setText(Rabbit.zg2uni(t4.getText().toString()));
            t5.setText(Rabbit.zg2uni(t5.getText().toString()));
            t6.setText(Rabbit.zg2uni(t6.getText().toString()));
            income.setText(Rabbit.zg2uni(income.getText().toString()));
            outcome.setText(Rabbit.zg2uni(outcome.getText().toString()));
            date.setText(Rabbit.zg2uni(date.getText().toString()));
            title.setHint(Rabbit.zg2uni(title.getHint().toString()));
            insert.setText(Rabbit.zg2uni(insert.getText().toString()));
        }
    }
    public String monthChange(int ch){
        String change=null;
        switch (ch){
            case 1:
                change="ဇန္နဝါရီလ";break;
            case 2:
                change="ေဖေဖာ္ဝါရီလ";break;
            case 3:
                change="မတ္လ";break;
            case 4:
                change="ဧပရယ္လ";break;
            case 5:
                change="ေမလ";break;
            case 6:
                change="ဇြန္လ";break;
            case 7:
                change="ဇူလိုင္လ";break;
            case 8:
                change="ျသဂုတ္လ";break;
            case 9:
                change="စက္တင္ဘာလ";break;
            case 10:
                change="ေအာက္တိုဘာလ";break;
            case 11:
                change="ႏိုဝင္ဘာလ";break;
            case 12:
                change="ဒီဇင္ဘာလ";break;
        }
        return change;
    }
    public String DYChange(String str){
        StringBuilder change_str = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0') {
                change_str.append("၀");
            } else if (str.charAt(i) == '1') {
                change_str.append("၁");
            } else if (str.charAt(i) == '2') {
                change_str.append("၂");
            } else if (str.charAt(i) == '3') {
                change_str.append("၃");
            } else if (str.charAt(i) == '4') {
                change_str.append("၄");
            } else if (str.charAt(i) == '5') {
                change_str.append("၅");
            } else if (str.charAt(i) == '6') {
                change_str.append("၆");
            } else if (str.charAt(i) == '7') {
                change_str.append("၇");
            } else if (str.charAt(i) == '8') {
                change_str.append("၈");
            } else if (str.charAt(i) == '9') {
                change_str.append("၉");
            }
        }
        return change_str.toString();
    }
    private void update(String id, String category, String type, String title, String amount, String date) {
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        boolean res=helper.updateSayan(id+"",category+"",type+"",title+"",amount+"",date);
        if(res){
            finish();
            Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}