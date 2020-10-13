package com.cu.sayan.Adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Activity.DetailActivity;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.TypeData;
import com.cu.sayan.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    public Context context;
    public ArrayList<TypeData> typeData;
    float setProgress_value=0;
    float setProgress_max=0;
    int monthOriginal,yearOriginal;
    int quest;
    float pro;

    public ReportAdapter(Context context, ArrayList<TypeData> typeData,int monthOriginal, int yearOriginal,int quest) {
        this.context=context;
        this.typeData=typeData;
        this.monthOriginal=monthOriginal;
        this.yearOriginal=yearOriginal;
        this.quest=quest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        int min = 0;
        int max = 255;
        Random r = new Random();
        final int r1 = r.nextInt(max - min) + min;
        final int r2 = r.nextInt(max - min) + min;
        final int r3 = r.nextInt(max - min) + min;
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(r1,r2,r3)));
        if(!isZawgyiFont()){
            income(Rabbit.zg2uni("ဝင္ေငြ"),holder.progressBar);
        }else {
            income("ဝင္ေငြ",holder.progressBar);
        }

        outcome(typeData.get(position).getCategory(),typeData.get(position).getType(),holder.progressBar,holder.type,holder.extra,holder.layout);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(v,typeData.get(position).getType()+"",holder.extra.getText().toString(),yearOriginal+"",monthOriginal+"");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void openBottomSheet(View v, final String type, final String value, final String from, final String to) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
        dialog.setContentView(view);
        dialog.show();
        TextView type_t,list_t,amount,t1;
        type_t=view.findViewById(R.id.type);
        amount=view.findViewById(R.id.amount);
        list_t=view.findViewById(R.id.list);
        t1=view.findViewById(R.id.t1);
        type_t.setText(type);
        if(!isZawgyiFont()){
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            list_t.setText(Rabbit.zg2uni(list_t.getText().toString()));
        }
        amount.setText(value);
        list_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("Type",type);
                intent.putExtra("Value",value);
                intent.putExtra("From",from);
                intent.putExtra("To",to);
                intent.putExtra("Quest",quest+"");
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private void income(String category,ProgressBar progressBar) {
        try {
            DatabaseHelper helper=new DatabaseHelper(context);
            Cursor res = helper.getSayan();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(quest==1){
                        if(res.getString(1).equals(category)){
                            String date=res.getString(5);
                            int month= Integer.parseInt(date.split("/")[1]);
                            int year= Integer.parseInt(date.split("/")[2]);
                            if(monthOriginal==month && yearOriginal==year){
                                int value= Integer.parseInt(res.getString(4));
                                setProgress_max+=value;
                            }
                        }
                    }else if (quest==0){
                        if(res.getString(1).equals(category)){
                            String date=res.getString(5);
                            int year= Integer.parseInt(date.split("/")[2]);
                            if(yearOriginal>=year && monthOriginal<=year){
                                int value= Integer.parseInt(res.getString(4));
                                setProgress_max+=value;
                            }
                        }
                    }
                }
            }
            if(setProgress_max>0){
                progressBar.setMax((int) setProgress_max);
            }else {
                progressBar.setMax((int) setProgress_max);
            }
            setProgress_max=0;

            helper.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("SetTextI18n")
    public void outcome(String category, String type, final ProgressBar progressBar, TextView type_text,TextView extra,LinearLayout linearLayout) {
        try {
            DatabaseHelper helper = new DatabaseHelper(context);
            Cursor res = helper.getSayan();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if (quest == 1) {
                        if (res.getString(1).equals(category) && res.getString(2).equals(type)) {
                            String date = res.getString(5);
                            int month = Integer.parseInt(date.split("/")[1]);
                            int year = Integer.parseInt(date.split("/")[2]);
                            if (monthOriginal == month && yearOriginal == year) {
                                int value = Integer.parseInt(res.getString(4));
                                setProgress_value += value;
                            }
                        }
                    } else if (quest == 0) {
                        if (res.getString(1).equals(category) && res.getString(2).equals(type)) {
                            String date = res.getString(5);
                            int year = Integer.parseInt(date.split("/")[2]);
                            if (yearOriginal >= year && monthOriginal <= year) {
                                int value = Integer.parseInt(res.getString(4));
                                setProgress_value += value;
                            }
                        }
                    }

                }
            }
            if (setProgress_value > 0){
                linearLayout.setVisibility(View.VISIBLE);
                ObjectAnimator.ofInt(progressBar, "progress", (int) setProgress_value)
                        .setDuration(2000)
                        .start();
            }else{
                linearLayout.setVisibility(View.GONE);
                progressBar.setProgress((int)setProgress_value);
            }
            if(!isZawgyiFont()){
                extra.setText("("+change(setProgress_value+"")+ Rabbit.zg2uni("က်ပ္")+")");
                type_text.setText(type+"("+change(setProgress_value+"")+ Rabbit.zg2uni("က်ပ္")+")");
            }else {
                extra.setText("("+change(setProgress_value+"")+"က်ပ္"+")");
                type_text.setText(type+"("+change(setProgress_value+"")+"က်ပ္"+")");
            }
            setProgress_value=0;

            helper.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        SharedPreferences preferences=context.getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
    @Override
    public int getItemCount() {
        return typeData.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView type,extra;
        public ProgressBar progressBar;
        public LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.extra=itemView.findViewById(R.id.extra);
            this.progressBar=itemView.findViewById(R.id.progress);
            this.layout=itemView.findViewById(R.id.layout);
        }
    }
}
