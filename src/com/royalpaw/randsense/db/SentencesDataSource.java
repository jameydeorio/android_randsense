package com.royalpaw.randsense.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamey
 * Date: 2/23/13
 * Time: 4:47 PM
 */
public class SentencesDataSource {
    private static final String TAG = "SentencesDataSource";

    private SQLiteDatabase database;
    private SentencesSQLHelper dbHelper;
    private String[] allColumns = {
            SentencesSQLHelper.COLUMN_ID,
            SentencesSQLHelper.COLUMN_PK,
            SentencesSQLHelper.COLUMN_SENTENCE,
            SentencesSQLHelper.COLUMN_IS_FAVORITE,
    };

    public SentencesDataSource(Context context) {
        dbHelper = new SentencesSQLHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Sentence createSentence(String sentence, long pk) {
        this.open();

        ContentValues values = new ContentValues();
        values.put(SentencesSQLHelper.COLUMN_SENTENCE, sentence);
        values.put(SentencesSQLHelper.COLUMN_PK, pk);
        long insertId = database.insert(SentencesSQLHelper.TABLE_SENTENCES, null, values);
        Cursor cursor = database.query(
                SentencesSQLHelper.TABLE_SENTENCES,
                allColumns,
                SentencesSQLHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null
        );
        cursor.moveToFirst();
        Sentence newSentence = cursorToSentence(cursor);

        cursor.close();
        this.close();

        return newSentence;
    }

    public List<Sentence> getAllSentences() {
        this.open();

        List<Sentence> sentences = new ArrayList<Sentence>();

        Cursor cursor = database.query(
                SentencesSQLHelper.TABLE_SENTENCES,
                allColumns,
                null, null, null, null, "pk desc"
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Sentence sentence = cursorToSentence(cursor);
            sentences.add(sentence);
            cursor.moveToNext();
        }

        cursor.close();
        this.close();

        return sentences;
    }

    public void deleteAllSentences() {
        this.open();
        database.delete(SentencesSQLHelper.TABLE_SENTENCES, null, null);
        this.close();
    }

    public void deleteSentence(Sentence sentence) {
        this.open();
        long id = sentence.getId();
        Log.i(TAG, "Sentence deleted with id " + id);
        database.delete(
                SentencesSQLHelper.TABLE_SENTENCES,
                SentencesSQLHelper.COLUMN_ID + " = " + id,
                null
        );
        this.close();
    }

    private Sentence cursorToSentence(Cursor cursor) {
        Sentence sentence = new Sentence();
        sentence.setId(cursor.getLong(0));
        sentence.setPk(cursor.getLong(1));
        sentence.setSentence(cursor.getString(2));
        sentence.setIsFavorite(cursor.getLong(3) == 1);
        return sentence;
    }
}
