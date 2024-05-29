package com.example.sanmusic.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavDB extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String DATABASE_NAME = "FavoritesDB";
    public static String TABLE_NAME = "FavoritesTable";
    public static String FAV_STATUS = "fStatus";
    public static String SONG_POSITION = "songPosition";
    public static String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+FAV_STATUS+" TEXT,"+SONG_POSITION+" INTEGER)";
    public FavDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDataIntoDB(int position, String fStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SONG_POSITION, position);
        cv.put(FAV_STATUS, fStatus);
        db.insert(TABLE_NAME, null, cv);
    }

    public Cursor readAllData(int pos){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from "+TABLE_NAME+" where "+SONG_POSITION+" = "+pos;
        return db.rawQuery(sql, null, null);
    }

    public Cursor readTableData(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME, null);
    }

    public Cursor selectAllInList(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from "+TABLE_NAME+" where "+FAV_STATUS+" = '1'";
        return db.rawQuery(sql, null, null);
    }

    public void removeFav(int pos){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "update "+TABLE_NAME+" set "+FAV_STATUS+" ='0' where "+SONG_POSITION+"="+pos;
        db.execSQL(sql);
    }
}
