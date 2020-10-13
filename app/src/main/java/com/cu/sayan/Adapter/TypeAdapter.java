package com.cu.sayan.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.TypeData;
import com.cu.sayan.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    Context context;
    ArrayList<TypeData> typeData;
    Integer[] colors= new Integer[]{R.color.colorIncome,R.color.colorOutcome,R.color.colorProfit};

    public TypeAdapter(Context context, ArrayList<TypeData> typeData) {
        this.context=context;
        this.typeData=typeData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /*
        int min = 0;
        int max = 255;
        Random r = new Random();
        final int r1 = r.nextInt(max - min) + min;
        final int r2 = r.nextInt(max - min) + min;
        final int r3 = r.nextInt(max - min) + min;
        holder.layout.setBackgroundColor(Color.rgb(r1,r2,r3));

         */
        holder.type.setText(typeData.get(position).getType());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(v,typeData.get(position).getId(),typeData.get(position).getCategory(),typeData.get(position).getType());
            }
        });
    }

    private void action(View v, final String id, final String category, final String type) {
        View view= LayoutInflater.from(v.getContext()).inflate(R.layout.action_category,null);
        ImageView close=view.findViewById(R.id.close);
        TextView delete=view.findViewById(R.id.delete);
        TextView type_t=view.findViewById(R.id.type);
        type_t.setText(type);
        final TextView update=view.findViewById(R.id.update);
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
        builder.setView(view);
        final AlertDialog ad=builder.create();
        ad.show();
        if(!isZawgyiFont()){
            delete.setText(Rabbit.zg2uni(delete.getText().toString()));
            update.setText(Rabbit.zg2uni(update.getText().toString()));
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                DatabaseHelper helper=new DatabaseHelper(context);
                int res=helper.deleteType(id);
                if(res==1){
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                updateCategory(v,id,category,type);
            }
        });
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    private void updateCategory(View v, final String id, final String category, String type_t) {
        View view= LayoutInflater.from(v.getContext()).inflate(R.layout.addnew_type,null);
        final EditText type=view.findViewById(R.id.type);
        ImageView close=view.findViewById(R.id.close);
        close.setVisibility(View.GONE);
        TextView title=view.findViewById(R.id.title);
        RadioGroup group=view.findViewById(R.id.radioGroup);
        if(!isZawgyiFont()){
            TextView t1=view.findViewById(R.id.t1);
            RadioButton income=view.findViewById(R.id.income);
            RadioButton outcome=view.findViewById(R.id.outcome);
            t1.setText(Rabbit.zg2uni(t1.getText().toString()));
            income.setText(Rabbit.zg2uni(income.getText().toString()));
            outcome.setText(Rabbit.zg2uni(outcome.getText().toString()));
            type.setHint(Rabbit.zg2uni(context.getResources().getString(R.string.type)));
            title.setText(Rabbit.zg2uni("ျပင္ဆင္မည္"));
            if(category.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                group.check(R.id.income);
            }else if(category.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                group.check(R.id.outcome);
            }
        }else {
            title.setText("ျပင္ဆင္မည္");
            if(category.equals("ဝင္ေငြ")){
                group.check(R.id.income);
            }else if(category.equals("ထြက္ေငြ")){
                group.check(R.id.outcome);
            }
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!isZawgyiFont()){
                    if(category.equals(Rabbit.zg2uni("ဝင္ေငြ"))){
                        group.check(R.id.income);
                    }else if(category.equals(Rabbit.zg2uni("ထြက္ေငြ"))){
                        group.check(R.id.outcome);
                    }
                }else {
                    if(category.equals("ဝင္ေငြ")){
                        group.check(R.id.income);
                    }else if(category.equals("ထြက္ေငြ")){
                        group.check(R.id.outcome);
                    }
                }
            }
        });
        type.setText(type_t);
        TextView ok=view.findViewById(R.id.ok);
        TextView cancel=view.findViewById(R.id.cancel);
        if(!isZawgyiFont()){
            cancel.setText(Rabbit.zg2uni("မလုပ္ေတာ့ပါ"));
            ok.setText(Rabbit.zg2uni("လုပ္မည္"));
        }else {
            cancel.setText("မလုပ္ေတာ့ပါ");
            ok.setText("လုပ္မည္");
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
        builder.setView(view);
        final AlertDialog ad=builder.create();
        ad.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                DatabaseHelper helper=new DatabaseHelper(context);
                boolean res=helper.updateType(id,category,type.getText().toString());
                if(res){
                    Toast.makeText(context,"Update",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        TextView type;
        ImageView more;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.more=itemView.findViewById(R.id.more);
            this.layout=itemView.findViewById(R.id.layout);
        }
    }
}
