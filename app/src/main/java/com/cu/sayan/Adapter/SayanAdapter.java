package com.cu.sayan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.SayanData;
import com.cu.sayan.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SayanAdapter extends RecyclerView.Adapter<SayanAdapter.ViewHolder> {
    public Context context;
    public ArrayList<SayanData> sayanData;
    Integer[] colors= new Integer[]{R.color.colorIncome,R.color.colorOutcome,R.color.colorProfit};

    public SayanAdapter(Context context, ArrayList<SayanData> sayanData) {
        this.context=context;
        this.sayanData=sayanData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sayan_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       /* int min = 0;
        int max = colors.length;
        Random r = new Random();
        int i = r.nextInt(max - min) + min;
        holder.color.setBackgroundResource(colors[i]);
        */
        int min = 0;
        int max = 255;
        Random r = new Random();
        final int r1 = r.nextInt(max - min) + min;
        final int r2 = r.nextInt(max - min) + min;
        final int r3 = r.nextInt(max - min) + min;
        holder.color.setBackgroundColor(Color.rgb(r1,r2,r3));
        holder.type.setText(sayanData.get(position).getType());
        holder.title.setText(sayanData.get(position).getTitle());

        String getDate=sayanData.get(position).getDate();
        String day=getDate.split("/")[0];
        int month= Integer.parseInt(getDate.split("/")[1]);
        String year=getDate.split("/")[2];
        if(!isZawgyiFont()){
            holder.amount.setText(change(sayanData.get(position).getAmount())+Rabbit.zg2uni(" က်ပ္"));
            holder.date.setText(Rabbit.zg2uni(monthChange(month)+" ၊ "+change(day)+"ရက္"+" ၊ "+change(year)+"ႏွစ္"));
        }else {
            holder.amount.setText(change(sayanData.get(position).getAmount())+" က်ပ္");
            holder.date.setText(monthChange(month)+" ၊ "+change(day)+"ရက္"+" ၊ "+change(year)+"ႏွစ္");
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
            }
        }
        return change_str.toString();
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
    @Override
    public int getItemCount() {
        return sayanData.size();
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences=context.getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView type,title,amount,date;
        public LinearLayout color;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.title=itemView.findViewById(R.id.title);
            this.amount=itemView.findViewById(R.id.amount);
            this.date=itemView.findViewById(R.id.date);
            this.color=itemView.findViewById(R.id.color);
        }
    }
}
