package com.cu.sayan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME="Sayan.db";
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Sayan (ID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORY TEXT,TYPE TEXT,TITLE TEXT,AMOUNT TEXT,DATE TEXT)");
        db.execSQL("CREATE TABLE Type (ID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORY TEXT,TYPE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Sayan");
        db.execSQL("DROP TABLE IF EXISTS Type");
    }
    public boolean insertSayan(String category,String type,String title,String amount,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("CATEGORY",category);
        contentValues.put("TYPE",type);
        contentValues.put("TITLE",title);
        contentValues.put("AMOUNT",amount);
        contentValues.put("DATE",date);
        long result=db.insert("Sayan",null,contentValues);
        db.close();
        return result != -1;
    }
    public Cursor getSayan(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Sayan",null);
        return res;
    }
    public int deleteSayan(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("Sayan","ID=?",new String[]{id});
        return res;
    }

    public boolean updateSayan(String id,String category,String type,String title,String amount,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("CATEGORY",category);
        contentValues.put("TYPE",type);
        contentValues.put("TITLE",title);
        contentValues.put("AMOUNT",amount);
        contentValues.put("DATE",date);
        int result=db.update("Sayan",contentValues,"ID=?",new String[]{id});
        return result > 0;
    }
    public boolean insertType(String category,String type){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("CATEGORY",category);
        contentValues.put("TYPE",type);
        long result=db.insert("Type",null,contentValues);
        db.close();
        return result != -1;
    }
    public Cursor getType(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Type",null);
        return res;
    }
    public int deleteType(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("Type","ID=?",new String[]{id});
        return res;
    }

    public boolean updateType(String id,String category,String type){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("CATEGORY",category);
        contentValues.put("TYPE",type);
        int result=db.update("Type",contentValues,"ID=?",new String[]{id});
        return result > 0;
    }

    public Cursor type(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor count=sqLiteDatabase.rawQuery("Select * from Sayan Group By TYPE Having count>0",null);
        return count;
    }

    public boolean deleteSayanTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("Sayan",null,null);
        return affectedRows>0;
    }
    public boolean deleteTypeTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("Type",null,null);
        return affectedRows>0;
    }
}
