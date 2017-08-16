package com.udanano.pocketcloset2;

        import android.provider.BaseColumns;

/**
 * Created by JB on 8/16/2017.
 */

public class TableData {

    public TableData() {}

    public static abstract class TableInfo implements BaseColumns {
        public static final String DATABASE_NAME = "clothes.db";
        public static final String TABLE_CLOTHES = "clothes";
        public static final String COLUMN_ID = "cloth_id";
        public static final String COLUMN_IMAGE = "cloth_image";
        public static final String COLUMN_DESC = "cloth_desc";
        public static final String COLUMN_CAT = "cloth_cat";
        public static final String COLUMN_LAST_DATE = "cloth_last_date";
    }

}
