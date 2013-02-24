package com.royalpaw.randsense.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * User: jamey
 * Date: 2/23/13
 * Time: 4:23 PM
 */
public class SentencesSQLHelper extends SQLiteOpenHelper {
    public static final String TAG = "SentencesSQLHelper";

    public static final String TABLE_SENTENCES = "sentences";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PK = "pk";
    public static final String COLUMN_SENTENCE = "sentence";
    public static final String COLUMN_IS_FAVORITE = "is_favorite";

    private static final String DATABASE_NAME = "sentences.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_SENTENCES + "("
            + COLUMN_ID + "integer primary key autoincrement, "
            + COLUMN_PK + "integer not null, "
            + COLUMN_SENTENCE + "text not null, "
            + COLUMN_IS_FAVORITE + "boolean not null default 0";

    public SentencesSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, String.format("Upgrading database from version %d to %d, wiping all existing data", oldVersion, newVersion));
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENTENCES);
        onCreate(db);
    }
}
