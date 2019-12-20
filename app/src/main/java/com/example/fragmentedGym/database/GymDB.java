package com.example.fragmentedGym.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class GymDB extends SQLiteOpenHelper {
    private static final String TAG = "TAG_X",
            DATABASE_NAME = "gym.db",
            TABLE_NAME = "inventory";

    public static final String COLUMN_ID = "inventory_id",
            COLUMN_NAME = "inventory_name",
            COLUMN_AMOUNT = "inventory_amount";

    private static int DATABASE_VERSION = 1;

    public GymDB(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE TABLE "+ TABLE_NAME+
                " ("+
                COLUMN_ID +" INTEGER PRIMARY KEY, "+
                COLUMN_NAME +" TEXT, "+
                COLUMN_AMOUNT +" INTEGER"+
                ")";
        db.execSQL(createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DATABASE_VERSION = newVersion;
        String updateQuery = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(updateQuery);
        onCreate(db);
    }

    //add to the db
    public void onPurchase(String material, int amount){
        int num = amount+getAmount(material);
        Log.d(TAG, "onPurchase: "+num);
        SQLiteDatabase db = getWritableDatabase();
        String setStatement = "UPDATE "+TABLE_NAME+
                " SET "+ COLUMN_AMOUNT
                +" = " +num+
                " WHERE "+ COLUMN_NAME +" = '"+material+"';";
        db.execSQL(setStatement);
    }

    public int getAmount(String material){
        SQLiteDatabase db = getReadableDatabase();
        String getStatement = "SELECT "+ COLUMN_AMOUNT +
                " FROM "+TABLE_NAME+
                " WHERE "+ COLUMN_NAME +
                " = "+"'"+material+"';";
        Cursor value = db.rawQuery(getStatement,null);
        int n = 0;
        if(value.getCount()==1){
            value.moveToFirst();
            n = value.getInt(value.getColumnIndex(COLUMN_AMOUNT));
        }
        return n;
    }

    public Cursor getInventory(){
        String selectStatement = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = getReadableDatabase();
        Cursor materials = db.rawQuery(selectStatement,null);
        return materials;
    }

    public void insert(String material){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,material);
        contentValues.put(COLUMN_AMOUNT,0);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,contentValues);
    }

}
