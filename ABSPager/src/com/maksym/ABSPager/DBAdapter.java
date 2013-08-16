package com.maksym.ABSPager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mitrofany4 on 8/14/13.
 */
public class DBAdapter {

    private static final String LOG = "DbAdapter";
    private Context context;
    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;

    public DBAdapter(Context context) {
        this.context = context;
    }

    public DBAdapter OpentoWrite() throws SQLException {
        dbHelper = new DbOpenHelper(context);
        db = dbHelper.getWritableDatabase();
        Log.d(LOG, "DB Opened to write");
        return this;
    }

    public DBAdapter OpentoRead() throws SQLException {
        dbHelper = new DbOpenHelper(context);
        db = dbHelper.getReadableDatabase();
        Log.d(LOG, "DB Opened to read");
        return this;
    }

    public void Close() {
        dbHelper.close();
    }

    public long createImageBean(GoogleImageBean object) {

        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.IMAGE_THMBURL, object.getThumbUrl());
        values.put(DbOpenHelper.IMAGE_URL, object.getUrl());
        values.put(DbOpenHelper.TITLE, object.getTitle());

        if (object.getFavorite()) {
            values.put(DbOpenHelper.FAVORITE, 1);}
        else
            values.put(DbOpenHelper.FAVORITE, 0);

        Log.d(LOG, "Task created");
        return db.insert(DbOpenHelper.TABLE_NAME, null, values);

    }

    public ArrayList<GoogleImageBean> getAllFavorites(){
        ArrayList<GoogleImageBean> images = new ArrayList<GoogleImageBean>();
        images.clear();
        String selection=DbOpenHelper.FAVORITE+" =1";
        Cursor cursor = db.query(DbOpenHelper.TABLE_NAME,
                /*new String[] {DbOpenHelper.KEY_ID, DbOpenHelper.IMAGE_URL, DbOpenHelper.TITLE},*/null,
                selection,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DbOpenHelper.KEY_ID));
                String thmbUrl=cursor.getString(cursor.getColumnIndex(DbOpenHelper.IMAGE_URL));
                String title = cursor.getString(cursor.getColumnIndex(DbOpenHelper.TITLE));
                GoogleImageBean imageBean = new GoogleImageBean(thmbUrl,title);
                images.add(imageBean);
            } while (cursor.moveToNext());

        }

        cursor.close();
        return images;

    }

}
