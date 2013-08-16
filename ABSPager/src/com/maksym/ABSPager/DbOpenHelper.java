package com.maksym.ABSPager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mitrofany4 on 8/14/13.
 */
public class DbOpenHelper extends SQLiteOpenHelper{

    private static final String LOG = "DbOpenHelper";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "imagesearch";

    public static final String KEY_ID = "_id";
    public static final String TABLE_NAME = "images";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_THMBURL = "image_thmburl";
    public static final String TITLE = "title";
    public static final String FAVORITE = "isFavorite";

    private static final String CREATE_TABLE = "create table " +
            TABLE_NAME + " ( " +
            KEY_ID + " INTEGER primary key autoincrement, " +
            IMAGE_URL + " TEXT, " +
            IMAGE_THMBURL + " TEXT, " +
            TITLE + " TEXT, " +
            FAVORITE + " INTEGER);";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
        Log.d(LOG, "CREATE_TABLE " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(DbOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
