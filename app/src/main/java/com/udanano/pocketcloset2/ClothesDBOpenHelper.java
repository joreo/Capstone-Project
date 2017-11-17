package com.udanano.pocketcloset2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by JB on 11/19/2016.
 */

public class ClothesDBOpenHelper extends SQLiteOpenHelper {

    //constructors and such
    private static final int DATABASE_VERSION = 2;
    //change the DB number if we ever change the structure. Or need to dump the DB maybe?
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
                COLUMN_LAST_DATE + " TEXT " +  //this may need to change so date comparisons work
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
        //we optioned to drop table on upgrades, but we can use an appropriate edit if we wanted to add columns or something instead
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOTHES);
        Log.i(LOGTAG, "Table has been upgraded");
        onCreate(db);
    }

    public void addEntry(ClothesDBOpenHelper dop, String cloth_image, String cloth_desc, String cloth_cat, String cloth_last_date){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //i *think* i leave the ID blank since I want it to be an autoincrement
        //cv.put(TableData.TableInfo.COLUMN_ID, cloth_id);
        cv.put(TableData.TableInfo.COLUMN_IMAGE, cloth_image);
        cv.put(TableData.TableInfo.COLUMN_DESC, cloth_desc);
        cv.put(TableData.TableInfo.COLUMN_CAT, cloth_cat);
        cv.put(TableData.TableInfo.COLUMN_LAST_DATE, cloth_last_date);

        Log.d("@@@Added to DB", "One row: " + cloth_desc);
    }

}
