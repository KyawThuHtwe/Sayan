package com.cu.sayan.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Activity.SayanActivity;
import com.cu.sayan.Activity.UpdateActivity;
import com.cu.sayan.Adapter.SayanAdapter;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.SayanData;
import com.cu.sayan.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyFragment extends Fragment {

    LinearLayout pieChartShow,listShow;
    PieChart pieChart;
    RecyclerView recyclerView;
    SayanAdapter sayanAdapter;
    ArrayList<SayanData> sayanData;
    TextView today,income_change,outcome_change;
    TextView today_type,today_in_out;
    boolean change=false;
    int dayOfFinal,monthOfFinal,yearOfFinal;
    boolean exchange=false;
    @SuppressLint({"ResourceAsColor", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_daily, container, false);
        final FloatingActionButton floatingActionButton = view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SayanActivity.class));
            }
        });
        today_type=view.findViewById(R.id.today_type);
        today_in_out=view.findViewById(R.id.today_in_out);
        today=view.findViewById(R.id.today);
        initialMonthDate(today);
        income_change=view.findViewById(R.id.income_change);
        outcome_change=view.findViewById(R.id.outcome_change);
        //
        pieChartShow=view.findViewById(R.id.pieChartShow);
        pieChart=view.findViewById(R.id.pieChart);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh(false);
        income_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(false);
                exchange=false;
            }
        });
        outcome_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(true);
                exchange=true;
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date(today,v);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {

                int position=holder.getAdapterPosition();
                String id=sayanData.get(position).getId();
                String category=sayanData.get(position).getCategory();
                String type=sayanData.get(position).getType();
                String title=sayanData.get(position).getTitle();
                String amount=sayanData.get(position).getAmount();
                String date=sayanData.get(position).getDate();
                action(id,category,type,title,amount,date);
                if(exchange){
                    refresh(true);
                }else {
                    refresh(false);
                }
            }
        }).attachToRecyclerView(recyclerView);

        if(!isZawgyiFont()){
            TextView t1=view.findViewById(R.id.t1);
            TextView t4=view.findViewById(R.id.t4);
            TextView ks4=view.findViewById(R.id.ks4);
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            t4.setText(Rabbit.zg2uni(t4.getText().toString()));
            ks4.setText(Rabbit.zg2uni(ks4.getText().toString()));
            income_change.setText(Rabbit.zg2uni(income_change.getText().toString()));
            outcome_change.setText(Rabbit.zg2uni(outcome_change.getText().toString()));
        }
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

        pieChartShow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceType"})
            @Override
            public void onClick(View v) {
                pieChart.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                pieChartShow.setBackgroundResource(R.drawable.corner_radius_bound);
                pieChartShow.setBackgroundTintList(ColorStateList.valueOf(R.color.colorAccent));
                listShow.setBackgroundResource(Color.TRANSPARENT);
            }
        });
        listShow=view.findViewById(R.id.listShow);
        listShow.setBackgroundTintList(ColorStateList.valueOf(R.color.colorAccent));
        listShow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NewApi", "ResourceType"})
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                listShow.setBackgroundResource(R.drawable.corner_radius_bound);
                listShow.setBackgroundTintList(ColorStateList.valueOf(R.color.colorAccent));
                pieChartShow.setBackgroundResource(Color.TRANSPARENT);
            }
        });

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void action(final String id, final String category, final String type, final String title, final String amount, final String date) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.action_category,null);
        ImageView close=view.findViewById(R.id.close);
        TextView delete=view.findViewById(R.id.delete);
        TextView type_t=view.findViewById(R.id.type);
        type_t.setText(type);
        final TextView update=view.findViewById(R.id.update);
        if(!isZawgyiFont()){
            delete.setText(Rabbit.zg2uni(delete.getText().toString()));
            update.setText(Rabbit.zg2uni(update.getText().toString()));
        }else {
            delete.setText(delete.getText().toString());
            update.setText(update.getText().toString());
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
        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ad.dismiss();
                DatabaseHelper helper=new DatabaseHelper(getContext());
                int res=helper.deleteSayan(id);
                if(res==1){
                    if(exchange){
                        refresh(true);
                    }else {
                        refresh(false);
                    }
                    Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                Intent intent=new Intent(getContext(), UpdateActivity.class);
                intent.putExtra("Id",id);
                intent.putExtra("Category",category);
                intent.putExtra("Type",type);
                intent.putExtra("Title",title);
                intent.putExtra("Amount",amount);
                intent.putExtra("Date",date);
                startActivity(intent);

            }
        });
    }
    public void Date(final TextView textView, View v){
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            public void onDateSet(DatePicker view, int yearOfYear, int monthOfYear, int dayOfMonth) {
                int month=(monthOfYear + 1);
                dayOfFinal= dayOfMonth;
                monthOfFinal=month;
                yearOfFinal= yearOfYear;
                if(!isZawgyiFont()){
                    textView.setText(Rabbit.zg2uni(monthChange(month)+" ၊ "+change(dayOfMonth +"")+"ရက္"+" ၊ "+change(yearOfYear +"")+"ႏွစ္"));
                }else {
                    textView.setText(monthChange(month)+" ၊ "+change(dayOfMonth +"")+"ရက္"+" ၊ "+change(yearOfYear +"")+"ႏွစ္");
                }
                refresh(false);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }
    double income_ks=0,outcome_ks=0,profit_ks=0;
    double daily_income=0,daily_outcome=0;
    @SuppressLint({"SetTextI18n", "ResourceType"})
    public void initialize(String str){
        try {
            DatabaseHelper helper = new DatabaseHelper(getContext());
            Cursor cursor=helper.getSayan();
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String date = cursor.getString(5);
                    int day = Integer.parseInt(date.split("/")[0]);
                    int month = Integer.parseInt(date.split("/")[1]);
                    int year = Integer.parseInt(date.split("/")[2]);

                    Calendar calendar=Calendar.getInstance();
                    int cur_month=calendar.get(Calendar.MONTH)+1;
                    int cur_year=calendar.get(Calendar.YEAR);

                    if (cur_month == month && cur_year == year) {
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
                    if (dayOfFinal==day && monthOfFinal== month && yearOfFinal == year) {
                        if(!isZawgyiFont()){
                            if(str.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                                if(cursor.getString(1).equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                                    daily_income+=Integer.parseInt(cursor.getString(4));
                                }
                            }else if(str.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                                if(cursor.getString(1).equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                                    daily_outcome+=Integer.parseInt(cursor.getString(4));
                                }
                            }
                        }else {
                            if(str.equals("ဝင္ေငြ")){
                                if(cursor.getString(1).equals("ဝင္ေငြ")){
                                    daily_income+=Integer.parseInt(cursor.getString(4));
                                }
                            }else if(str.equals("ထြက္ေငြ")){
                                if(cursor.getString(1).equals("ထြက္ေငြ")){
                                    daily_outcome+=Integer.parseInt(cursor.getString(4));
                                }
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
                    if(str.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                        today_type.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.today_income)));
                        today_in_out.setText(change(daily_income+""));
                    }else if(str.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                        today_type.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.today_outcome)));
                        today_in_out.setText(change(daily_outcome+""));
                    }
                }else {
                    if(str.equals("ဝင္ေငြ")){
                        today_type.setText(requireContext().getResources().getString(R.string.today_income));
                        today_in_out.setText(change(daily_income+""));
                    }else if(str.equals("ထြက္ေငြ")){
                        today_type.setText(requireContext().getResources().getString(R.string.today_outcome));
                        today_in_out.setText(change(daily_outcome+""));
                    }
                }
            }else {
                if(!isZawgyiFont()){
                    if(str.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                        today_type.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.income)));
                        today_in_out.setText(change(daily_income+""));
                    }else if(str.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                        today_type.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.outcome)));
                        today_in_out.setText(change(daily_outcome+""));
                    }
                }else {
                    if(str.equals("ဝင္ေငြ")){
                        today_type.setText(requireContext().getResources().getString(R.string.income));
                        today_in_out.setText(change(daily_income+""));
                    }else if(str.equals("ထြက္ေငြ")){
                        today_type.setText(requireContext().getResources().getString(R.string.outcome));
                        today_in_out.setText(change(daily_outcome+""));
                    }
                }
            }
            daily_income=0;
            daily_outcome=0;
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
    @SuppressLint("SetTextI18n")
    public void initialMonthDate(TextView today){
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        dayOfFinal=day;
        monthOfFinal=month;
        yearOfFinal=year;
        if(!isZawgyiFont()){
            today.setText(Rabbit.zg2uni(monthChange(month)+" ၊ "+change(day+"")+"ရက္"+" ၊ "+change(year+"")+"ႏွစ္"));
        }else {
            today.setText(monthChange(month)+" ၊ "+change(day+"")+"ရက္"+" ၊ "+change(year+"")+"ႏွစ္");
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint({"ResourceAsColor", "NewApi"})
    public void refresh(boolean res){
        change=res;
        if(change){
            outcome_change.setTextColor(R.color.colorWhite);
            outcome_change.setBackgroundResource(R.color.colorIncome);
            income_change.setBackgroundResource(R.color.colorTransparent);
            if(!isZawgyiFont()){
                refreshData(Rabbit.zg2uni("ထြက္ေငြ"));
                initialize(Rabbit.zg2uni("ထြက္ေငြ"));
            }else {
                refreshData("ထြက္ေငြ");
                initialize("ထြက္ေငြ");
            }

        }else {
            income_change.setTextColor(R.color.colorWhite);
            income_change.setBackgroundResource(R.color.colorIncome);
            outcome_change.setBackgroundResource(R.color.colorTransparent);
            if(!isZawgyiFont()){
                refreshData(Rabbit.zg2uni("ဝင္ေငြ"));
                initialize(Rabbit.zg2uni("ဝင္ေငြ"));
            }else {
                refreshData("ဝင္ေငြ");
                initialize("ဝင္ေငြ");
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void refreshData(String data){
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
            ArrayList<PieEntry> entries=new ArrayList<>();
            entries.clear();
            Cursor s=helper.getSayan();
            if(s.getCount()>0){
                while (s.moveToNext()){
                    if(s.getString(1).equals(data)){
                        String date=s.getString(5);
                        int day= Integer.parseInt(date.split("/")[0]);
                        int month= Integer.parseInt(date.split("/")[1]);
                        int year= Integer.parseInt(date.split("/")[2]);
                        if(dayOfFinal==day && monthOfFinal==month && yearOfFinal==year){
                            entries.add(new PieEntry(Float.parseFloat(s.getString(4)),s.getString(2)));
                        }
                    }
                }
            }
            PieDataSet pieDataSet=new PieDataSet(entries,"Sayan");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(12f);
            PieData pieData=new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Sayan");
            //pieChart.setUsePercentValues(true);
            Legend legend=pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

            pieChart.animateXY(1400,1400);
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    PieEntry pe=(PieEntry) e;
                    if(!isZawgyiFont()){
                        pieChart.setCenterText(pe.getLabel()+"\n"+pe.getValue()+Rabbit.zg2uni("က်ပ္"));
                    }else{
                        pieChart.setCenterText(pe.getLabel()+"\n"+pe.getValue()+"က်ပ္");
                    }
                    //Snackbar.make(getView() ,pe.getLabel()+pe.getValue() ,Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
            //pieChart.animate();


        }catch (Exception e){
            e.printStackTrace();
        }
        sayanAdapter=new SayanAdapter(getContext(),sayanData);
        recyclerView.setAdapter(sayanAdapter);
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}