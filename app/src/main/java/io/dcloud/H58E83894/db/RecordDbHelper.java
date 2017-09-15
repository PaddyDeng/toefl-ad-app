package io.dcloud.H58E83894.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fire on 2017/7/18  14:45.
 */

public class RecordDbHelper extends SQLiteOpenHelper {

    private static String DBHELPER_NAME = "record.db";

    public RecordDbHelper(Context context) {
        super(context, DBHELPER_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String grammar_sql = "CREATE TABLE " + MakeTable.TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MakeTable.USER_UID + " int,"
                + MakeTable.GRAMMAR_ID + " text,"
                + MakeTable.GRAMMAR_JSON + " text,"
                + MakeTable.MAKE_DATE + " text,"
                + MakeTable.YOU_RESULT + " text"
                + ");";

        String core_sql = "CREATE TABLE " + MakeTable.CORE_TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MakeTable.USER_UID + " int,"
                + MakeTable.GRAMMAR_ID + " text,"
                + MakeTable.GRAMMAR_JSON + " text,"
                + MakeTable.MAKE_DATE + " text,"
                + MakeTable.YOU_RESULT + " text"
                + ");";
        String random_sql = "CREATE TABLE " + MakeTable.RANDOM_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MakeTable.TIMES + " int,"
                + MakeTable.TYPE + " int,"
                + MakeTable.DATE + " text,"
                + MakeTable.COURSEID + " text"
                + ");";

        db.execSQL(grammar_sql);
        db.execSQL(core_sql);
        db.execSQL(random_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
