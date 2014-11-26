package com.pefi.Busse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pererikfinstad on 30/09/14.
 */
public class DBHandler extends SQLiteOpenHelper {

    final static String TAG = "DBHandler";

    //database
    static String DATABASE_NAME = "FavouriteLines";
    static int    DATABASE_VERSION = 2;


    //Friends table
    static String TABLE_FAV = "favourites";
    static String FAV_ID = "_ID"; // auto generates and auto increments primary key
    static String FAV_STOP_ID = "stopId";
    static String FAV_LINE_NO = "lineNo";
    static String FAV_DESTINATION = "destination";
    static String FAV_FROM_STOP = "fromStop";
    //Create Friends tables query
    private static final String CREATE_TABLE_FAV = "CREATE TABLE " + TABLE_FAV + "(" + FAV_ID + " INTEGER PRIMARY KEY," + FAV_STOP_ID + " TEXT," + FAV_LINE_NO + " TEXT," + FAV_DESTINATION + " TEXT," + FAV_FROM_STOP + " TEXT" +")";



    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create friends table query
        Log.d("SQL", CREATE_TABLE_FAV); //for debugging
        db.execSQL(CREATE_TABLE_FAV);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        onCreate(db);
    }


    //DATABASE QUERIES FOR FAVOURITES


    public void insertFavourite(Favourite fav){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FAV_STOP_ID, fav.getStopId());
        values.put(FAV_LINE_NO, fav.getLineNo());
        values.put(FAV_DESTINATION, fav.getDestination());
        values.put(FAV_FROM_STOP, fav.getFromStopName());

        db.insert(TABLE_FAV, null, values);
        db.close();

    }


    public boolean deleteFavourite(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_FAV, FAV_ID + "='" + id + "'", null) > 0;

    }


    public Favourite getFavourite(int id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FAV + " WHERE " + FAV_ID + " = " + id;
        Cursor c = db.rawQuery(query,null);

        if (c != null && c.moveToFirst()){

            Favourite f = new Favourite();
            f.setStopId(c.getString(c.getColumnIndex(FAV_STOP_ID)));
            f.setLineNo(c.getString(c.getColumnIndex(FAV_LINE_NO)));
            f.setDestination(c.getString(c.getColumnIndex(FAV_DESTINATION)));
            f.setFromStopName(c.getString(c.getColumnIndex(FAV_FROM_STOP)));

            c.close();
            db.close();
            return f;

        }

        c.close();
        db.close();

        return null;
    }



    public List<Favourite> getAllFavourites(){
        List<Favourite> friendsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FAV;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            do{
                Favourite f = new Favourite();
                f.setStopId(c.getString(c.getColumnIndex(FAV_STOP_ID)));
                f.setLineNo(c.getString(c.getColumnIndex(FAV_LINE_NO)));
                f.setDestination(c.getString(c.getColumnIndex(FAV_DESTINATION)));
                f.setFromStopName(c.getString(c.getColumnIndex(FAV_FROM_STOP)));
                f.setId(c.getInt(c.getColumnIndex(FAV_ID)));

                friendsList.add(f);
            }while (c.moveToNext());
        }

        db.close();
        return friendsList;
    }





    /**
     * Deletes all entries in the table
     *
     * @return true if all deleted entries is more than 0
     */
    public boolean deleteAllFavourites(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAV,null,null) > 0;

    }


}//end of class DBHandler
