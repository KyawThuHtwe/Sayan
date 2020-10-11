package com.cu.sayan.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.sayan.Activity.FontActivity;
import com.cu.sayan.Activity.MainActivity;
import com.cu.sayan.Activity.PasswordActivity;
import com.cu.sayan.Activity.TypeActivity;
import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.R;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class NotificationsFragment extends Fragment {

    LinearLayout type,delete;
    CheckBox checkBox;
    TextView password,t1,t2;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        type=view.findViewById(R.id.type);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TypeActivity.class));
            }
        });
        delete=view.findViewById(R.id.database);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        checkBox=view.findViewById(R.id.checkbox);
        password=view.findViewById(R.id.password);
        t1=view.findViewById(R.id.t1);
        t2=view.findViewById(R.id.t2);
        onOff(checkBox,1);

        if(isZawgyiFont()){
            password.setText(requireContext().getResources().getString(R.string.password_not));
            t1.setText(requireContext().getResources().getString(R.string.type));
            t2.setText(requireContext().getResources().getString(R.string.add_new));
        }else {
            password.setText(changeUnicode(requireContext().getResources().getString(R.string.password_not)));
            t1.setText(changeUnicode(requireContext().getResources().getString(R.string.type)));
            t2.setText(changeUnicode(requireContext().getResources().getString(R.string.add_new)));
        }
        LinearLayout language=view.findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FontActivity.class));
            }
        });
        return view;
    }

    public String changeUnicode(String string){
        return Rabbit.zg2uni(string);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onOff(CheckBox checkBox, final int i){
        if(isExist()){
            checkBox.setChecked(true);
            password.setText(requireContext().getResources().getString(R.string.password_has));
        }else {
            password.setText(requireContext().getResources().getString(R.string.password_not));
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(i==1){
                    if(isChecked){
                        startActivity(new Intent(getContext(), PasswordActivity.class));
                    }else {
                        remove();
                    }
                }

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void remove() {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.action_category,null);
        ImageView close=view.findViewById(R.id.close);
        close.setVisibility(View.GONE);
        TextView delete=view.findViewById(R.id.delete);
        TextView type_t=view.findViewById(R.id.type);
        final TextView update=view.findViewById(R.id.update);

        if(!isZawgyiFont()){
            type_t.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.password_remove)));
            delete.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.cancel)));
            update.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.ok)));
        }else {
            type_t.setText(requireContext().getResources().getString(R.string.password_remove));
            delete.setText(requireContext().getResources().getString(R.string.cancel));
            update.setText(requireContext().getResources().getString(R.string.ok));
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog ad=builder.create();
        ad.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ad.dismiss();
                checkBox.setChecked(true);
                onOff(checkBox,0);
                if(!isZawgyiFont()){
                    password.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.password_has)));
                }else {
                    password.setText(requireContext().getResources().getString(R.string.password_has));
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ad.dismiss();
                checkBox.setChecked(false);
                if(!isZawgyiFont()){
                    password.setText(Rabbit.zg2uni(requireContext().getResources().getString(R.string.password_not)));
                }else {
                    password.setText(requireContext().getResources().getString(R.string.password_not));
                }
                savePassword("",false);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savePassword(String password, boolean res){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Password",password);
        editor.putBoolean("Exit",res);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isExist(){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Password", Context.MODE_PRIVATE);
        return preferences.getBoolean("Exit",false);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void delete(){
        View view= LayoutInflater.from(getContext()).inflate(R.layout.action_category,null);
        ImageView close=view.findViewById(R.id.close);
        TextView delete=view.findViewById(R.id.delete);
        TextView type=view.findViewById(R.id.type);
        final TextView update=view.findViewById(R.id.update);
        if(isZawgyiFont()){
            type.setText("အၿပီးဖ်က္သိမ္းမည္");
            delete.setText("မလုပ္ေတာ့ပါ");
            update.setText("လုပ္မည္");
        }else {
            type.setText(Rabbit.zg2uni("အၿပီးဖ်က္သိမ္းမည္"));
            delete.setText(Rabbit.zg2uni("မလုပ္ေတာ့ပါ"));
            update.setText(Rabbit.zg2uni("လုပ္မည္"));
        }
        close.setVisibility(View.GONE);
        AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setView(view);
        final AlertDialog ad=builder.create();
        ad.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                ad.dismiss();
                DatabaseHelper helper=new DatabaseHelper(getContext());
                boolean res=helper.deleteSayanTable();
                if(res){
                    Toast.makeText(getContext(),"Deleted Database",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= requireActivity().getSharedPreferences("Font",Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}