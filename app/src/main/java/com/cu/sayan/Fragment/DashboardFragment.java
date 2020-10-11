package com.cu.sayan.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Adapter.ProfitAdapter;
import com.cu.sayan.Adapter.ReportAdapter;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.TypeData;
import com.cu.sayan.R;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {

    RecyclerView recyclerView,recyclerView1;
    ArrayList<TypeData> typeData;
    ReportAdapter reportAdapter;
    ProfitAdapter profitAdapter;
    Spinner monthSpinner,yearSpinner;
    ArrayAdapter monthAdapter,yearAdapter;

    String[] months = {"ဇန္နဝါရီလ", "ေဖေဖာ္ဝါရီလ", "မတ္လ", "ဧပရယ္လ", "ေမလ", "ဇြန္လ", "ဇူလိုင္လ", "ျသဂုတ္လ", "စက္တင္ဘာလ", "ေအာက္တိုဘာလ", "ႏိုဝင္ဘာလ", "ဒီဇင္ဘာလ"};
    String[] years = {"၂၀၂၀ ခုႏွစ္", "၂၀၂၁ ခုႏွစ္", "၂၀၂၂ ခုႏွစ္", "၂၀၂၂ ခုႏွစ္", "၂၀၂၃ ခုႏွစ္", "၂၀၂၄ ခုႏွစ္", "၂၀၂၅ ခုႏွစ္", "၂၀၂၆ ခုႏွစ္", "၂၀၂၇ ခုႏွစ္", "၂၀၂၈ ခုႏွစ္", "၂၀၂၉ ခုႏွစ္", "၂၀၃၀ ခုႏွစ္"};

    ImageView hide;
    TextView submit,to;
    LinearLayout showHide,layout;
    RadioGroup radioGroup;
    TextView total,profit;
    boolean monthly=true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        profit=view.findViewById(R.id.profit);
        hide=view.findViewById(R.id.hide);
        submit=view.findViewById(R.id.submit);
        to=view.findViewById(R.id.to);
        showHide=view.findViewById(R.id.showHide);
        layout=view.findViewById(R.id.layout);
        if(!isZawgyiFont()){
            for(int i=0;i<months.length;i++){
                months[i]=Rabbit.zg2uni(months[i]);
            }
            for(int i=0;i<years.length;i++){
                years[i]=Rabbit.zg2uni(years[i]);
            }
            TextView sayan=view.findViewById(R.id.sayan);
            TextView t1=view.findViewById(R.id.t1);
            TextView t2=view.findViewById(R.id.t2);
            RadioButton monthly=view.findViewById(R.id.monthly);
            RadioButton yearly=view.findViewById(R.id.yearly);
            RadioButton income=view.findViewById(R.id.income);
            RadioButton outcome=view.findViewById(R.id.outcome);
            sayan.setText(Rabbit.zg2uni(sayan.getText().toString()));
            monthly.setText(Rabbit.zg2uni(monthly.getText().toString()));
            yearly.setText(Rabbit.zg2uni(yearly.getText().toString()));
            income.setText(Rabbit.zg2uni(income.getText().toString()));
            outcome.setText(Rabbit.zg2uni(outcome.getText().toString()));
            submit.setText(Rabbit.zg2uni(submit.getText().toString()));
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            t2.setText(Rabbit.zg2uni(t2.getText().toString()));
            to.setText(Rabbit.zg2uni(to.getText().toString()));
        }

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHide.setVisibility(View.VISIBLE);
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHide.setVisibility(View.VISIBLE);
            }
        });
        monthSpinner=view.findViewById(R.id.monthSpinner);
        yearSpinner=view.findViewById(R.id.yearSpinner);
        total=view.findViewById(R.id.total);
        spinner(true);
        Calendar calendar=Calendar.getInstance();
        final int month=calendar.get(Calendar.MONTH)+1;
        final int year=calendar.get(Calendar.YEAR);
        selectSpinner(month,year);
        to.setVisibility(View.GONE);
        radioGroup=view.findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.monthly:
                        monthly=true;
                        spinner(true);
                        selectSpinner(month,year);
                        recyclerView.setAdapter(null);
                        if(!isZawgyiFont()){
                            total.setText("၀"+Rabbit.zg2uni("က်ပ္"));
                        }else {
                            total.setText("၀"+"က်ပ္");
                        }
                        to.setVisibility(View.GONE);
                        profit.setVisibility(View.GONE);
                        recyclerView1.setAdapter(null);
                        break;
                    case R.id.yearly:
                        monthly=false;
                        spinner(false);
                        recyclerView.setAdapter(null);
                        if(!isZawgyiFont()){
                            total.setText("၀"+Rabbit.zg2uni("က်ပ္"));
                        }else {
                            total.setText("၀"+"က်ပ္");
                        }
                        to.setVisibility(View.VISIBLE);
                        profit.setVisibility(View.GONE);
                        recyclerView1.setAdapter(null);
                        break;
                }
            }
        });
        final RadioGroup group=view.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.income:
                        if(!isZawgyiFont()){
                            refreshData(Rabbit.zg2uni("ဝင္ေငြ"),month,year);
                            if(monthly){
                                totalMonthly(Rabbit.zg2uni("ဝင္ေငြ"),month,year);
                            }else {
                                totalYearly(Rabbit.zg2uni("ဝင္ေငြ"),month,year);
                            }
                        }else {
                            refreshData("ဝင္ေငြ",month,year);
                            if(monthly){
                                totalMonthly("ဝင္ေငြ",month,year);
                            }else {
                                totalYearly("ဝင္ေငြ",month,year);
                            }
                        }

                        break;
                    case R.id.outcome:
                        if(!isZawgyiFont()){
                            refreshData(Rabbit.zg2uni("ထြက္ေငြ"),month,year);
                            if(monthly){
                                totalMonthly(Rabbit.zg2uni("ထြက္ေငြ"),month,year);
                            }else {
                                totalYearly(Rabbit.zg2uni("ထြက္ေငြ"),month,year);
                            }
                        }else {
                            refreshData("ထြက္ေငြ",month,year);
                            if(monthly){
                                totalMonthly("ထြက္ေငြ",month,year);
                            }else {
                                totalYearly("ထြက္ေငြ",month,year);
                            }
                        }
                        break;
                }
            }
        });

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
        recyclerView1=view.findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));

        if(!isZawgyiFont()){
            refreshData(Rabbit.zg2uni("ဝင္ေငြ"),month,year);
            if(monthly){
                totalMonthly(Rabbit.zg2uni("ဝင္ေငြ"),month,year);
            }else {
                totalYearly(Rabbit.zg2uni("ဝင္ေငြ"),month,year);
            }
        }else {
            refreshData("ဝင္ေငြ",month,year);
            if(monthly){
                totalMonthly("ဝင္ေငြ",month,year);
            }else {
                totalYearly("ဝင္ေငြ",month,year);
            }
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isZawgyiFont()){
                    int month_s=getMonth(Rabbit.uni2zg(monthSpinner.getSelectedItem()+""));
                    int year_s= getYear(Rabbit.uni2zg(yearSpinner.getSelectedItem()+""));
                    refreshData(Rabbit.zg2uni("ဝင္ေငြ"),month_s,year_s);
                    if(monthly){
                        totalMonthly(Rabbit.zg2uni("ဝင္ေငြ"),month_s,year_s);
                    }else {
                        totalYearly(Rabbit.zg2uni("ဝင္ေငြ"),month_s,year_s);
                    }
                }else {
                    int month_s=getMonth(monthSpinner.getSelectedItem()+"");
                    int year_s= getYear(yearSpinner.getSelectedItem()+"");
                    refreshData("ဝင္ေငြ",month_s,year_s);
                    if(monthly){
                        totalMonthly("ဝင္ေငြ",month_s,year_s);
                    }else {
                        totalYearly("ဝင္ေငြ",month_s,year_s);
                    }
                }

                group.check(R.id.income);
                profit.setVisibility(View.VISIBLE);

            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void spinner(boolean monthly){
        if(monthly){
            monthAdapter=new ArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_dropdown_item,months);
        }else {
            monthAdapter=new ArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_dropdown_item,years);
        }
        yearAdapter=new ArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_dropdown_item,years);
        monthSpinner.setAdapter(monthAdapter);
        yearSpinner.setAdapter(yearAdapter);
    }
    public void selectSpinner(int month,int year){
        switch (month) {
            case 1:
                monthSpinner.setSelection(0);
                break;
            case 2:
                monthSpinner.setSelection(1);
                break;
            case 3:
                monthSpinner.setSelection(2);
                break;
            case 4:
                monthSpinner.setSelection(3);
                break;
            case 5:
                monthSpinner.setSelection(4);
                break;
            case 6:
                monthSpinner.setSelection(5);
                break;
            case 7:
                monthSpinner.setSelection(6);
                break;
            case 8:
                monthSpinner.setSelection(7);
                break;
            case 9:
                monthSpinner.setSelection(8);
                break;
            case 10:
                monthSpinner.setSelection(9);
                break;
            case 11:
                monthSpinner.setSelection(10);
                break;
            case 12:
                monthSpinner.setSelection(11);
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void refreshData(String data,int month,int year){
        try {
            typeData = new ArrayList<>();
            typeData.clear();
            DatabaseHelper helper = new DatabaseHelper(getContext());
            Cursor cursor = helper.getType();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if(cursor.getString(1).equals(data)){
                        typeData.add(new TypeData(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<String> profit_list=new ArrayList<>();
        if(!isZawgyiFont()){
            profit_list.add(Rabbit.zg2uni("ဝင္ေငြ"));
            profit_list.add(Rabbit.zg2uni("ထြက္ေငြ"));
        }else {
            profit_list.add("ဝင္ေငြ");
            profit_list.add("ထြက္ေငြ");
        }

        if(monthly){
            reportAdapter=new ReportAdapter(getContext(),typeData,month,year,1);
            profitAdapter=new ProfitAdapter(getContext(),profit_list,month,year,1);
            profit(month,year,1);
        }else {
            reportAdapter=new ReportAdapter(getContext(),typeData,month,year,0);
            profitAdapter=new ProfitAdapter(getContext(),profit_list,month,year,0);
            profit(month,year,0);
        }
        recyclerView.setAdapter(reportAdapter);
        recyclerView1.setAdapter(profitAdapter);
    }
    double income_ks=0,outcome_ks=0,profit_ks=0;
    @SuppressLint("SetTextI18n")
    private void profit(int month, int year, int quest) {
        try {
            DatabaseHelper helper = new DatabaseHelper(getContext());
            Cursor cursor=helper.getSayan();
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String date = cursor.getString(5);
                    int ret_month = Integer.parseInt(date.split("/")[1]);
                    int ret_year = Integer.parseInt(date.split("/")[2]);
                    if(quest==1){
                        if(!isZawgyiFont()){
                            if (month == ret_month && year == ret_year) {
                                if(cursor.getString(1).equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                                    income_ks+=Integer.parseInt(cursor.getString(4));
                                }else if(cursor.getString(1).equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                                    outcome_ks+=Integer.parseInt(cursor.getString(4));
                                }
                            }
                        }else {
                            if (month == ret_month && year == ret_year) {
                                if(cursor.getString(1).equals("ဝင္ေငြ")){
                                    income_ks+=Integer.parseInt(cursor.getString(4));
                                }else if(cursor.getString(1).equals("ထြက္ေငြ")){
                                    outcome_ks+=Integer.parseInt(cursor.getString(4));
                                }
                            }
                        }

                    }else if(quest==0){
                        if(!isZawgyiFont()){
                            if (year >= ret_year && month <= ret_month) {
                                if(cursor.getString(1).equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                                    income_ks+=Integer.parseInt(cursor.getString(4));
                                }else if(cursor.getString(1).equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                                    outcome_ks+=Integer.parseInt(cursor.getString(4));
                                }
                            }
                        }else {
                            if (year >= ret_year && month <= ret_month) {
                                if(cursor.getString(1).equals("ဝင္ေငြ")){
                                    income_ks+=Integer.parseInt(cursor.getString(4));
                                }else if(cursor.getString(1).equals("ထြက္ေငြ")){
                                    outcome_ks+=Integer.parseInt(cursor.getString(4));
                                }
                            }
                        }

                    }

                }

            }
            profit_ks=income_ks-outcome_ks;
            if(outcome_ks>income_ks){
                profit.setText("(-"+change(profit_ks+"")+"က်ပ္)");
                profit.setBackgroundColor(Color.RED);
            }else {
                profit.setText("("+change(profit_ks+"")+"က်ပ္)");
            }
            if(!isZawgyiFont()){
                profit.setText(Rabbit.zg2uni(profit.getText().toString()));
            }
            income_ks=0;outcome_ks=0;
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public int getMonth(String data){
        int i=0;
        switch (data) {
            case "ဇန္နဝါရီလ":
                i = 1;
                break;
            case "ေဖေဖာ္ဝါရီလ":
                i = 2;
                break;
            case "မတ္လ":
                i = 3;
                break;
            case "ဧပရယ္လ":
                i = 4;
                break;
            case "ေမလ":
                i = 5;
                break;
            case "ဇြန္လ":
                i = 6;
                break;
            case "ဇူလိုင္လ":
                i = 7;
                break;
            case "ျသဂုတ္လ":
                i = 8;
                break;
            case "စက္တင္ဘာလ":
                i = 9;
                break;
            case "ေအာက္တိုဘာလ":
                i = 10;
                break;
            case "ႏိုဝင္ဘာလ":
                i = 11;
                break;
            case "ဒီဇင္ဘာလ":
                i = 12;
                break;
        }
        return i;
    }
    public int getYear(String str){
        int i=0;
        switch (str){
            case "၂၀၂၀ ခုႏွစ္":
                i=2020;
                break;
            case "၂၀၂၁ ခုႏွစ္":
                i=2021;
                break;
            case "၂၀၂၂ ခုႏွစ္":
                i=2022;
                break;
            case "၂၀၂၃ ခုႏွစ္":
                i=2023;
                break;
            case "၂၀၂၄ ခုႏွစ္":
                i=2024;
                break;
            case "၂၀၂၅ ခုႏွစ္":
                i=2025;
                break;
            case "၂၀၂၆ ခုႏွစ္":
                i=2026;
                break;
            case "၂၀၂၇ ခုႏွစ္":
                i=2027;
                break;
            case "၂၀၂၈ ခုႏွစ္":
                i=2028;
                break;
            case "၂၀၂၉ ခုႏွစ္":
                i=2029;
                break;
            case "၂၀၃၀ ခုႏွစ္":
                i=2030;
                break;
        }
        return i;
    }
    int total_value=0;
    @SuppressLint("SetTextI18n")
    public void totalMonthly(String str, int m, int y){
        try {
            DatabaseHelper helper=new DatabaseHelper(getContext());
            Cursor res = helper.getSayan();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(res.getString(1).equals(str)){
                        String date=res.getString(5);
                        int month= Integer.parseInt(date.split("/")[1]);
                        int year= Integer.parseInt(date.split("/")[2]);
                        if(m==month && y==year){
                            int value= Integer.parseInt(res.getString(4));
                            total_value+=value;
                        }
                    }
                }
            }
            if (!isZawgyiFont()){
                total.setText("("+change(total_value+"")+Rabbit.zg2uni("က်ပ္")+")");
            }else {
                total.setText("("+change(total_value+"")+"က်ပ္"+")");
            }
            total_value=0;
            helper.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("SetTextI18n")
    public void totalYearly(String str, int to, int from){
        try {
            DatabaseHelper helper=new DatabaseHelper(getContext());
            Cursor res = helper.getSayan();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(res.getString(1).equals(str)){
                        String date=res.getString(5);
                        int year= Integer.parseInt(date.split("/")[2]);
                        if(from>=year && to<=year){
                            int value= Integer.parseInt(res.getString(4));
                            total_value+=value;
                        }
                    }
                }
            }
            if(!isZawgyiFont()){
                total.setText("("+change(total_value+"")+Rabbit.zg2uni("က်ပ္")+")");
            }else {
                total.setText("("+change(total_value+"")+"က်ပ္"+")");
            }

            total_value=0;
            helper.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
    public boolean isZawgyiFont(){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}