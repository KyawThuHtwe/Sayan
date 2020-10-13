package com.cu.sayan.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cu.sayan.Database.DatabaseHelper;
import com.cu.sayan.Font.Rabbit;
import com.cu.sayan.Model.TypeData;
import com.cu.sayan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    ArrayList<TypeData> typeData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Menu menu=navView.getMenu();
        MenuItem home = menu.findItem(R.id.navigation_home);
        MenuItem daily = menu.findItem(R.id.navigation_daily);
        MenuItem report = menu.findItem(R.id.navigation_dashboard);
        MenuItem setting = menu.findItem(R.id.navigation_notifications);
        if(!isZawgyiFont()){
            home.setTitle(Rabbit.zg2uni(home.getTitle().toString()));
            daily.setTitle(Rabbit.zg2uni(daily.getTitle().toString()));
            report.setTitle(Rabbit.zg2uni(report.getTitle().toString()));
            setting.setTitle(Rabbit.zg2uni(setting.getTitle().toString()));
        }

        try {
            if(isOnce()) {
                notOnce(false);
                typeData = new ArrayList<>();
                typeData.add(new TypeData("1", "ထြက္ေငြ", "ေစ်းဖိုး"));
                typeData.add(new TypeData("2", "ထြက္ေငြ", "ဖုန္းေဘ"));
                typeData.add(new TypeData("3", "ထြက္ေငြ", "အဝတ္အစား"));
                typeData.add(new TypeData("4", "ထြက္ေငြ", "ထမင္းစား"));
                typeData.add(new TypeData("4", "ထြက္ေငြ", "သေရစာ"));
                typeData.add(new TypeData("5", "ထြက္ေငြ", "အေပ်ာ္အပါး"));
                typeData.add(new TypeData("6", "ထြက္ေငြ", "လက္ေဆာင္"));
                typeData.add(new TypeData("7", "ထြက္ေငြ", "က်န္းမာေရး"));
                typeData.add(new TypeData("8", "ထြက္ေငြ", "ေက်ာင္းအသံုးစာရိတ္"));
                typeData.add(new TypeData("9", "ထြက္ေငြ", "အားကစား"));
                typeData.add(new TypeData("10", "ထြက္ေငြ", "ခရီးသြား"));
                typeData.add(new TypeData("11", "ထြက္ေငြ", "ဆိုင္ကယ္"));
                typeData.add(new TypeData("12", "ထြက္ေငြ", "ဆိုင္ကယ္ဆီ"));
                typeData.add(new TypeData("13", "ထြက္ေငြ", "အေထြေထြ"));
//
                typeData.add(new TypeData("14", "ဝင္ေငြ", "အျမတ္ေငြ"));
                typeData.add(new TypeData("14", "ဝင္ေငြ", "လစာ"));
                typeData.add(new TypeData("15", "ဝင္ေငြ", "ေဘာနပ္"));
                typeData.add(new TypeData("16", "ဝင္ေငြ", "မုန္႔ဖိုး"));
                typeData.add(new TypeData("17", "ဝင္ေငြ", "အျခား"));
                for (int i = 0; i < typeData.size(); i++) {
                    if(!isZawgyiFont()){
                        insert(Rabbit.zg2uni(typeData.get(i).getCategory())+ "", Rabbit.zg2uni(typeData.get(i).getType()) + "");
                    }else {
                        insert(typeData.get(i).getCategory() + "", typeData.get(i).getType() + "");
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void insert(String category,String type){
        DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
        boolean res=helper.insertType(category,type);
        if(res){
            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
        }
    }
    public void notOnce(boolean res){
        SharedPreferences preferences=getSharedPreferences("Exit", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("exit",res).apply();
    }
    public boolean isOnce(){
        SharedPreferences preferences=getSharedPreferences("Exit", Context.MODE_PRIVATE);
        return preferences.getBoolean("exit",true);
    }
    public boolean isZawgyiFont(){
        SharedPreferences preferences= getSharedPreferences("Font", Context.MODE_PRIVATE);
        return preferences.getBoolean("Font",true);
    }
}