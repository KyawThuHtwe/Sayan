package com.cu.sayan.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Adapter.DetailAdapter;
import com.cu.sayan.Adapter.SayanAdapter;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Model.SayanData;
import com.cu.sayan.R;

import java.util.ArrayList;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<SayanData> sayanData;
    DetailAdapter detailAdapter;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Intent intent=getIntent();
        String type=intent.getStringExtra("Type");
        String value=intent.getStringExtra("Value");
        String from=intent.getStringExtra("From");
        String to=intent.getStringExtra("To");
        String quest=intent.getStringExtra("Quest");
        getSupportActionBar().setTitle(type+value);
        assert from != null;
        assert to != null;
        dataLoading(type,Integer.parseInt(from),Integer.parseInt(to),quest);
        detailAdapter=new DetailAdapter(getApplicationContext(),sayanData);
        recyclerView.setAdapter(detailAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dataLoading(String type, int from, int to,String q){
        try {
            sayanData=new ArrayList<>();
            DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
            Cursor cursor = helper.getSayan();
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if(q.equals("1")){
                        if(cursor.getString(2).equals(type)){
                            String date=cursor.getString(5);
                            int month= Integer.parseInt(date.split("/")[1]);
                            int year= Integer.parseInt(date.split("/")[2]);
                            if(from==year && to==month){
                                sayanData.add(new SayanData(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
                            }
                        }
                    }else if(q.equals("0")){
                        if(cursor.getString(2).equals(type)){
                            String date=cursor.getString(5);
                            int year= Integer.parseInt(date.split("/")[2]);
                            if(from>=year && to<=year){
                                sayanData.add(new SayanData(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
                            }
                        }
                    }

                }
            }

            helper.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}