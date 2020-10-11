package com.cu.sayan.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Adapter.TypeAdapter;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.TypeData;
import com.cu.sayan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TypeActivity extends AppCompatActivity {

    ArrayList<TypeData> typeData;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!isZawgyiFont()){
            getSupportActionBar().setTitle(Rabbit.zg2uni(getResources().getString(R.string.type)));
        }else {
            getSupportActionBar().setTitle(getResources().getString(R.string.type));
        }

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reload();
        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNew(v);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    floatingActionButton.hide();
                }else {
                    floatingActionButton.show();
                }
            }
        });

    }
    public void reload(){
        try {
            typeData = new ArrayList<>();
            typeData.clear();
            DatabaseHelper helper=new DatabaseHelper(this);
            Cursor cursor=helper.getType();
            if(cursor.getCount()>0 && cursor!=null){
                while (cursor.moveToNext()){
                    typeData.add(new TypeData(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
                }
            }
            TypeAdapter typeAdapter=new TypeAdapter(getApplicationContext(),typeData);
            recyclerView.setAdapter(typeAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    boolean result=true;
    public void addNew(View v){
        View view= LayoutInflater.from(v.getContext()).inflate(R.layout.addnew_type,null);
        final EditText type=view.findViewById(R.id.type);
        RadioGroup group=view.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.income:
                        result=true;
                        break;
                    case R.id.outcome:
                        result=false;
                        break;
                }
            }
        });
        ImageView close=view.findViewById(R.id.close);
        TextView ok=view.findViewById(R.id.ok);
        TextView cancel=view.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        if(!isZawgyiFont()){
            TextView title=view.findViewById(R.id.title);
            TextView t1=view.findViewById(R.id.t1);
            RadioButton income=view.findViewById(R.id.income);
            RadioButton outcome=view.findViewById(R.id.outcome);
            income.setText(Rabbit.zg2uni(income.getText().toString()));
            outcome.setText(Rabbit.zg2uni(outcome.getText().toString()));
            ok.setText(Rabbit.zg2uni(ok.getText().toString()));
            cancel.setText(Rabbit.zg2uni(cancel.getText().toString()));
            title.setText(Rabbit.zg2uni(title.getText().toString()));
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            type.setHint(Rabbit.zg2uni(type.getHint().toString()));
        }else {
            ok.setText(ok.getText().toString());
            cancel.setText(cancel.getText().toString());
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
        builder.setView(view);
        final AlertDialog ad=builder.create();
        ad.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                if(!isZawgyiFont()){
                    if(result){
                        insert(Rabbit.zg2uni("ဝင္ေငြ"),type.getText().toString());
                    }else {
                        insert(Rabbit.zg2uni("ထြက္ေငြ"),type.getText().toString());
                    }
                }else {
                    if(result){
                        insert("ဝင္ေငြ",type.getText().toString());
                    }else {
                        insert("ထြက္ေငြ",type.getText().toString());
                    }
                }

            }
        });
    }
    public void insert(String category,String type){
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        boolean res=helper.insertType(category,type);
        if(res){
            reload();
            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}