package com.udanano.pocketcloset2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JB on 11/19/2016.
 */

public class ClothesDBOpenHelper extends SQLiteOpenHelper {

    //constructors and such
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "clothes.db";

    private static final String LOGTAG = "POCKETCLOSET";

    private static final String TABLE_CLOTHES = "clothes";
    private static final String COLUMN_ID = "cloth_id";
    private static final String COLUMN_IMAGE = "cloth_image";
    private static final String COLUMN_DESC = "cloth_desc";
    private static final String COLUMN_CAT = "cloth_cat";
    private static final String COLUMN_LAST_DATE = "cloth_last_date";

    private static final String TABLE_CREATE =
        "CREATE TABLE " + TABLE_CLOTHES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_DESC + " TEXT, " +
                COLUMN_CAT + " TEXT, " +
                COLUMN_LAST_DATE + " TEXT, " +
                ")";




    public ClothesDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TABLE_CREATE);
        Log.i(LOGTAG, "Table has been created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOTHES);
        onCreate(db);
    }

}
