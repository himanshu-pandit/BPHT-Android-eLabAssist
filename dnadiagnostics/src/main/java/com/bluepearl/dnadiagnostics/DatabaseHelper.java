package com.bluepearl.dnadiagnostics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csa on 3/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static String DATABASE = "database.db";
    public static String TABLE ="mytable";
    public static String NAME ="name";
    public static String ID ="id";
    public static String COMPANY ="company";
    public static String CITY ="city";
    public static String COUNTRY ="country";
    String br;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         //  br= "CREATE TABLE mytable(name TEXT,company TEXT,city TEXT,country TEXT);";
         br = "CREATE TABLE "+TABLE+"("+ID+" integer primary key autoincrement not null,"+NAME+ " Text, "+COMPANY+ " Text, "+CITY+ " Text, "+COUNTRY+ " Text);";
        db.execSQL(br);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+TABLE+" ;");
    }


    public void insertdata(String name,String company ,String city,String country){
        System.out.print("Hello "+br);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();


            contentValues.put(NAME, name);
            contentValues.put(COMPANY, company);
            contentValues.put(CITY,city);
            contentValues.put(COUNTRY,country);
            db.insert(TABLE,null,contentValues);


    }

    public List<DataModel> getdata(){
       // DataModel dataModel = new DataModel();
        List<DataModel> data=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE+" ;",null);
         StringBuffer stringBuffer = new StringBuffer();
        DataModel dataModel = null; 
        while (cursor.moveToNext()) {
            dataModel= new DataModel();
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            //Integer id = Integer.parseInt(ids);
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
            String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
            dataModel.setId(id);
            dataModel.setName(name);
            dataModel.setCity(city);
            dataModel.setCounty(country);
            stringBuffer.append(dataModel);
           // stringBuffer.append(dataModel);
            data.add(dataModel);
        }
        return data;
    }



    public boolean deleteitem(int id){

        SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("delete from "+TABLE+" where "+ID+" ="+id);

        return  true;

    }




}
