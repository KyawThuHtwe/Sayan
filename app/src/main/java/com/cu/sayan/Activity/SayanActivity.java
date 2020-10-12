package com.cu.sayan.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
import com.cu.sayan.Model.TypeData;
import com.cu.sayan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SayanActivity extends AppCompatActivity {

    ImageView back;
    TextView insert,date;
    EditText title,amount;
    Spinner type;
    ArrayList<String> typeData;
    String category;
    LinearLayout datePicker;
    String InsertDate;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_sayan);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type=findViewById(R.id.type);
        RadioGroup radioGroup=findViewById(R.id.radio);
        if(!isZawgyiFont()){
            category= Rabbit.zg2uni( "ထြက္ေငြ");
        }else {
            category="ထြက္ေငြ";
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.outcome:
                        if(!isZawgyiFont()){
                            category= Rabbit.zg2uni( "ထြက္ေငြ");
                        }else {
                            category="ထြက္ေငြ";
                        }
                        reload();
                        break;
                    case R.id.income:
                        if(!isZawgyiFont()){
                            category= Rabbit.zg2uni( "ဝင္ေငြ");
                        }else {
                            category="ဝင္ေငြ";
                        }
                        reload();
                        break;
                }
            }
        });
        reload();
        date=findViewById(R.id.date);
        datePicker=findViewById(R.id.datePicker);
        initialDate(date);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date(date,v);
            }
        });
        title=findViewById(R.id.title);
        amount=findViewById(R.id.amount);
        insert=findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(category+"",type.getSelectedItem()+"",title.getText()+"",amount.getText()+"",InsertDate);
            }
        });
        if(!isZawgyiFont()){
            TextView t1=findViewById(R.id.t1);
            TextView t2=findViewById(R.id.t2);
            TextView t3=findViewById(R.id.t3);
            TextView t4=findViewById(R.id.t4);
            TextView t5=findViewById(R.id.t5);
            TextView t6=findViewById(R.id.t6);
            TextView insert=findViewById(R.id.insert);
            RadioButton income=findViewById(R.id.income);
            RadioButton outcome=findViewById(R.id.outcome);
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            t2.setText(Rabbit.zg2uni(t2.getText().toString()));
            t3.setText(Rabbit.zg2uni(t3.getText().toString()));
            t4.setText(Rabbit.zg2uni(t4.getText().toString()));
            t5.setText(Rabbit.zg2uni(t5.getText().toString()));
            t6.setText(Rabbit.zg2uni(t6.getText().toString()));
            insert.setText(Rabbit.zg2uni(insert.getText().toString()));
            income.setText(Rabbit.zg2uni(income.getText().toString()));
            outcome.setText(Rabbit.zg2uni(outcome.getText().toString()));
            title.setHint(Rabbit.zg2uni(getResources().getString(R.string.title)));
            amount.setHint(Rabbit.zg2uni(getResources().getString(R.string.amount)));
        }

    }
    public void reload(){
        try {
            typeData = new ArrayList<>();
            typeData.clear();
            DatabaseHelper helper=new DatabaseHelper(this);
            Cursor cursor=helper.getType();
            if(cursor.getCount()>0 && cursor!=null){
                while (cursor.moveToNext()){
                    if(!isZawgyiFont()){
                        if(category.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                            if(cursor.getString(1).equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                                typeData.add(cursor.getString(2));
                            }
                        }else if(category.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                            if(cursor.getString(1).equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                                typeData.add(cursor.getString(2));
                            }
                        }
                    }else {
                        if(category.equals("ထြက္ေငြ")){
                            if(cursor.getString(1).equals("ထြက္ေငြ")){
                                typeData.add(cursor.getString(2));
                            }
                        }else if(category.equals("ဝင္ေငြ")){
                            if(cursor.getString(1).equals("ဝင္ေငြ")){
                                typeData.add(cursor.getString(2));
                            }
                        }
                    }
                }
            }
            ArrayAdapter arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,typeData);
            type.setAdapter(arrayAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void insert(String category,String type, String title, String amount, String date){
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        boolean res=helper.insertSayan(category,type,title,amount,date);
        if(res){
            finish();
            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
        }
    }
    public void Date(final TextView textView, View v){
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            public void onDateSet(DatePicker view, int yearOfYear, int monthOfYear, int dayOfMonth) {
                int day=dayOfMonth;
                int month=(monthOfYear + 1);
                int year=yearOfYear;
                InsertDate=day+"/"+month+"/"+year;
                if(!isZawgyiFont()){
                    textView.setText(Rabbit.zg2uni(monthChange(month)+" ၊ "+DYChange(day+"")+"ရက္"+" ၊ "+DYChange(year+"")+"ႏွစ္"));
                }else {
                    textView.setText(monthChange(month)+" ၊ "+DYChange(day+"")+"ရက္"+" ၊ "+DYChange(year+"")+"ႏွစ္");
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }
    @SuppressLint("SetTextI18n")
    public void initialDate(TextView textView){
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        InsertDate=day+"/"+month+"/"+year;
        if(!isZawgyiFont()){
            textView.setText(Rabbit.zg2uni(monthChange(month)+" ၊ "+DYChange(day+"")+"ရက္"+" ၊ "+DYChange(year+"")+"ႏွစ္"));
        }else {
            textView.setText(monthChange(month)+" ၊ "+DYChange(day+"")+"ရက္"+" ၊ "+DYChange(year+"")+"ႏွစ္");
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
    public boolean isZawgyiFont(){
        SharedPreferences preferences= getSharedPreferences("Font",Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}