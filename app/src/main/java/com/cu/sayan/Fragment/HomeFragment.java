package com.cu.sayan.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Activity.SayanActivity;
import com.cu.sayan.Adapter.SayanAdapter;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.SayanData;
import com.cu.sayan.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.objectweb.asm.Handle;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    TextView income,outcome,profit,month,today,t2;
    int dayOfFinal,monthOfFinal,yearOfFinal;
    TextView average,daily_spend_ks,daily_left_ks,daily_spend,daily_left,percent_text,over_ks;
    ProgressBar progressBar;
    LinearLayout over_layout,daily_outcome_detail;
    ArrayList<SayanData> sayanData;
    SayanAdapter sayanAdapter;
    String[] months = {"ယခုလ", "ယခင္လ"};
    AppCompatSpinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final FloatingActionButton floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SayanActivity.class));
            }
        });
        if(!isZawgyiFont()){
            for(int i=0;i<months.length;i++){
                months[i]=Rabbit.zg2uni(months[i]);
            }
        }
        spinner=view.findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter=new ArrayAdapter<>(requireContext(),R.layout.spinner_item,months);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        progressBar=view.findViewById(R.id.daily_progress);
        percent_text=view.findViewById(R.id.percent_text);
        income=view.findViewById(R.id.income);
        outcome=view.findViewById(R.id.outcome);
        profit=view.findViewById(R.id.profit);
        month=view.findViewById(R.id.month);
        over_layout=view.findViewById(R.id.over_layout);
        over_ks=view.findViewById(R.id.over_ks);
        daily_outcome_detail=view.findViewById(R.id.daily_outcome_detail);
        t2=view.findViewById(R.id.t2);
        initialMonthDate(month,today);
        if(!isZawgyiFont()){
            TextView t1=view.findViewById(R.id.t1);
            TextView t3=view.findViewById(R.id.t3);
            TextView ks1=view.findViewById(R.id.ks1);
            TextView ks2=view.findViewById(R.id.ks2);
            TextView ks3=view.findViewById(R.id.ks3);
            TextView sayan=view.findViewById(R.id.sayan);
            TextView daily_over=view.findViewById(R.id.daily_over);
            TextView daily_average=view.findViewById(R.id.daily_average);
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            t2.setText(Rabbit.zg2uni(t2.getText().toString()));
            t3.setText(Rabbit.zg2uni(t3.getText().toString()));
            ks1.setText(Rabbit.zg2uni(ks1.getText().toString()));
            ks2.setText(Rabbit.zg2uni(ks2.getText().toString()));
            ks3.setText(Rabbit.zg2uni(ks3.getText().toString()));
            sayan.setText(Rabbit.zg2uni(sayan.getText().toString()));
            daily_average.setText(Rabbit.zg2uni(daily_average.getText().toString()));
            daily_over.setText(Rabbit.zg2uni(daily_over.getText().toString()));
        }
        final ImageView addDaily=view.findViewById(R.id.addDaily);
        average=view.findViewById(R.id.average);
        daily_left=view.findViewById(R.id.daily_left);
        daily_left_ks=view.findViewById(R.id.daily_left_ks);
        daily_spend=view.findViewById(R.id.daily_spend);
        daily_spend_ks=view.findViewById(R.id.daily_spend_ks);
        addDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDaily();
            }
        });
        dailyRefresh();
        selectSpinner();
        return view;
    }

    private void selectSpinner() {

        if(spinner.getSelectedItemPosition()==0){
            Calendar calendar=Calendar.getInstance();
            int cur_day=calendar.get(Calendar.DAY_OF_MONTH);
            int cur_month=calendar.get(Calendar.MONTH)+1;
            int cur_year=calendar.get(Calendar.YEAR);
            initialize(cur_day,cur_month,cur_year);
        }else if(spinner.getSelectedItemPosition()==1){
            Calendar calendar=Calendar.getInstance();
            int pre_day=calendar.get(Calendar.DAY_OF_MONTH);
            int pre_month=calendar.get(Calendar.MONTH);
            int pre_year=calendar.get(Calendar.YEAR);
            initialize(pre_day,pre_month,pre_year);
        }
    }

    @SuppressLint("SetTextI18n")
    public void dailyRefresh(){
        if(!isZawgyiFont()){
            average.setText(change(getDailyAverage())+Rabbit.zg2uni("က်ပ္"));
        }else {
            average.setText(change(getDailyAverage())+"က်ပ္");
        }
    }
    public void addNewDaily(){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.add_daily_average,null);
        ImageView close=view.findViewById(R.id.close);
        TextView delete=view.findViewById(R.id.delete);
        TextView update=view.findViewById(R.id.update);
        final EditText amount=view.findViewById(R.id.amount);
        amount.setText(getDailyAverage());
        if(!isZawgyiFont()){
            TextView t1=view.findViewById(R.id.t1);
            delete.setText(Rabbit.zg2uni(delete.getText().toString()));
            update.setText(Rabbit.zg2uni(update.getText().toString()));
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog ad=builder.create();
        ad.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDailyAverage(amount.getText().toString());
                dailyRefresh();
                selectSpinner();
                ad.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDailyAverage("0");
                dailyRefresh();
                selectSpinner();
                ad.dismiss();
            }
        });
    }
    public void addDailyAverage(String average){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Daily",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Average",average).apply();
    }
    public String getDailyAverage(){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Daily",Context.MODE_PRIVATE);
        return preferences.getString("Average","0");
    }
    double income_ks=0,outcome_ks=0,profit_ks=0;
    double daily_outcome=0;
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void initialize(int select_day,int select_month,int select_year){
        try {
            DatabaseHelper helper = new DatabaseHelper(getContext());
            Cursor cursor=helper.getSayan();
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String date = cursor.getString(5);
                    int day = Integer.parseInt(date.split("/")[0]);
                    int month = Integer.parseInt(date.split("/")[1]);
                    int year = Integer.parseInt(date.split("/")[2]);

                    if (select_month == month && select_year == year) {
                        if(!isZawgyiFont()){
                            if(cursor.getString(1).equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                                income_ks+=Integer.parseInt(cursor.getString(4));
                            }else if(cursor.getString(1).equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                                outcome_ks+=Integer.parseInt(cursor.getString(4));
                            }
                        }else {
                            if(cursor.getString(1).equals("ဝင္ေငြ")){
                                income_ks+=Integer.parseInt(cursor.getString(4));
                            }else if(cursor.getString(1).equals("ထြက္ေငြ")){
                                outcome_ks+=Integer.parseInt(cursor.getString(4));
                            }
                        }
                    }

                    Calendar calendar=Calendar.getInstance();
                    int cur_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int cur_month=calendar.get(Calendar.MONTH)+1;
                    int cur_year=calendar.get(Calendar.YEAR);

                    if (cur_day==day && cur_month== month && cur_year == year) {
                        if(!isZawgyiFont()){
                            if (cursor.getString(1).equals(Rabbit.zg2uni("ထြက္ေငြ"))) {
                                daily_outcome += Integer.parseInt(cursor.getString(4));
                            }
                        }else {
                            if(cursor.getString(1).equals("ထြက္ေငြ")){
                                daily_outcome+=Integer.parseInt(cursor.getString(4));
                            }
                        }
                    }
                }
            }
            Calendar calendar=Calendar.getInstance();
            int cur_day=calendar.get(Calendar.DAY_OF_MONTH);
            int cur_month=calendar.get(Calendar.MONTH)+1;
            int cur_year=calendar.get(Calendar.YEAR);
            if(dayOfFinal==cur_day && monthOfFinal==cur_month && yearOfFinal==cur_year){
                if(!isZawgyiFont()){
                    daily_spend.setText(Rabbit.zg2uni(daily_spend.getText().toString()));
                    daily_spend_ks.setText(Rabbit.zg2uni(change(daily_outcome+"")+"က်ပ္"));
                }else {
                    daily_spend.setText(daily_spend.getText().toString());
                    daily_spend_ks.setText(change(daily_outcome+"")+"က်ပ္");
                }
            }

            double daily_average= Integer.parseInt(getDailyAverage());
            double daily_ks= daily_average-daily_outcome;
            if(daily_average>0){
                progressBar.setMax((int)daily_average);
            }else {
                progressBar.setMax(0);
            }

            if (daily_outcome > 0){
                ObjectAnimator.ofInt(progressBar, "progress", (int) daily_outcome)
                        .setDuration(2000)
                        .start();
            }else {
                progressBar.setProgress(0);
            }

            double per=(daily_outcome/daily_average)*100;

            if(per>100){
                percent_text.setText("Over");
            }else {
                percent_text.setText(per+"");
            }

            if(daily_outcome>daily_average){
                over_layout.setVisibility(View.VISIBLE);
                if(!isZawgyiFont()){
                    daily_left.setText(Rabbit.zg2uni(daily_left.getText().toString()));
                    daily_left_ks.setText("-"+Rabbit.zg2uni(change(daily_ks+"")+"က်ပ္"));
                    over_ks.setText(Rabbit.zg2uni(change(daily_ks+"")+"က်ပ္"));
                }else {
                    daily_left.setText(daily_left.getText().toString());
                    daily_left_ks.setText("-"+change(daily_ks+"")+"က်ပ္");
                    over_ks.setText(change(daily_ks+"")+"က်ပ္");
                }
                daily_left_ks.setBackgroundColor(Color.RED);
                daily_left_ks.setTextColor(Color.WHITE);
            }else {
                over_layout.setVisibility(View.GONE);
                if(!isZawgyiFont()){
                    daily_left.setText(Rabbit.zg2uni(daily_left.getText().toString()));
                    daily_left_ks.setText(Rabbit.zg2uni(change(daily_ks+"")+"က်ပ္"));
                }else {
                    daily_left.setText(daily_left.getText().toString());
                    daily_left_ks.setText(change(daily_ks+"")+"က်ပ္");
                }
            }

            daily_outcome_detail.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    dailyOutcomeDetail(v,t2.getText().toString(),daily_spend_ks.getText().toString());
                }
            });

            daily_outcome=0;

            profit_ks=income_ks-outcome_ks;
            if(outcome_ks>income_ks){
                profit.setText("(-"+change(profit_ks+"")+")");
                profit.setBackgroundColor(Color.RED);
            }else {
                profit.setText(change(profit_ks+""));
            }
            income.setText(change(income_ks+""));
            outcome.setText(change(outcome_ks+""));
            income_ks=0;outcome_ks=0;
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void dailyOutcomeDetail(View v, String s, String ks) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.daily_outcome_detail, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
        dialog.setContentView(view);
        dialog.show();
        TextView  daily_s=view.findViewById(R.id.daily_spend);
        TextView  daily_sks=view.findViewById(R.id.daily_spend_ks);
        if(!isZawgyiFont()){
            daily_s.setText(Rabbit.zg2uni(daily_s.getText().toString()));
        }
        daily_sks.setText(ks);
        RecyclerView  recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyOutcomeData(s);
        sayanAdapter=new SayanAdapter(getContext(),sayanData);
        recyclerView.setAdapter(sayanAdapter);
    }
    public String change(String str){
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
            }else if (str.charAt(i) == '.') {
                break;
            }
        }
        return change_str.toString();
    }
    @SuppressLint("SetTextI18n")
    public void initialMonthDate(TextView months, TextView today){
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        dayOfFinal=day;
        monthOfFinal=month;
        yearOfFinal=year;
        if(!isZawgyiFont()){
            //today.setText(Rabbit.zg2uni(change(day+"")+"ရက္"+" ၊ "+change(year+"")+"ႏွစ္"));
            months.setText(Rabbit.zg2uni(monthChange(month)+" ၊ "+change(day+"")+"ရက္"+" ၊ "+change(year+"")+"ႏွစ္"));
        }else {
            //today.setText(change(day+"")+"ရက္"+" ၊ "+change(year+"")+"ႏွစ္");
            months.setText(monthChange(month)+" ၊ "+change(day+"")+"ရက္"+" ၊ "+change(year+"")+"ႏွစ္");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dailyOutcomeData(String data){
        try{
            sayanData=new ArrayList<>();
            DatabaseHelper helper=new DatabaseHelper(getContext());
            Cursor cursor=helper.getSayan();
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    if(cursor.getString(1).equals(data)){
                        String date=cursor.getString(5);
                        int day= Integer.parseInt(date.split("/")[0]);
                        int month= Integer.parseInt(date.split("/")[1]);
                        int year= Integer.parseInt(date.split("/")[2]);
                        if(dayOfFinal==day && monthOfFinal==month && yearOfFinal==year){
                            sayanData.add(new SayanData(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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
    public boolean isZawgyiFont(){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}