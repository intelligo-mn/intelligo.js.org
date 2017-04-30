package com.dglproject.tour.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * Author: Doljinsuren Enkhbaatar.
 * Project: DglTour
 * URL: https://www.github.com/doljko
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context myContext;

    private static final String TAG = "===DatabaseHandler===";
    private static final int    DATABASE_VERSION = 1;
    private static final String DATABASE_NAME    = "dgltour.db";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            UserAdapter.USER_ID + " INTEGER PRIMARY KEY," +
            UserAdapter.USER_NAME + " TEXT," +
            UserAdapter.USER_EMAIL + " TEXT," +
            UserAdapter.USER_PASSWORD + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_USERS);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(UserAdapter.TABLE_USERS);
        onCreate(db);
    }
    public void dropTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        if (db == null || TextUtils.isEmpty(tableName)) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + UserAdapter.TABLE_USERS);
    }
}
