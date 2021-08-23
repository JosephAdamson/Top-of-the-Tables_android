package com.joeadamson.topofthetables.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Handler for the app's local mini data base, which consists of a one row table holding
 * personal best scores for each activity (multiplication/division/addition/subtraction).
 *
 * @author Joseph Adamson
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "personalbest.db";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE = "personal_best";
    public static final String COLUMN_MULTI = "multiplication";
    public static final String COLUMN_DIV = "division";
    public static final String COLUMN_ADD = "addition";
    public static final String COLUMN_SUB = "subtraction";

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    COLUMN_MULTI + " INTEGER, " +
                    COLUMN_DIV + " INTEGER, " +
                    COLUMN_ADD + " INTEGER, " +
                    COLUMN_SUB + " INTEGER" +
                    ");";

    // Array used to match mode data from TrainerActivity to get
    // the correct column for updatePB method
    public static final String[] modes = {COLUMN_MULTI, COLUMN_DIV,
            COLUMN_ADD, COLUMN_SUB};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates table (and consequently user.db file) when database
     * is first accessed (basically initializes the database).
     *
     * @param db SQLite database obj
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        ContentValues contentValues = new ContentValues();

        // Initialize the single row in the table.
        contentValues.put(COLUMN_MULTI, 0);
        contentValues.put(COLUMN_DIV, 0);
        contentValues.put(COLUMN_ADD, 0);
        contentValues.put(COLUMN_SUB, 0);

        db.insert(USER_TABLE, null, contentValues);
    }

    /**
     * Overridden to upgrade database if needed.
     *
     * @param db SQLite database obj
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        this.onCreate(db);
    }

    /**
     * Retrieve a personal best for a particular activity.
     *
     * @param gameMode parameter retrieved from TrainerActivity, corresponds with
     *              member of the same name in that class
     * @return personal best for that activity (number of stars)
     */
    public int getPersonalBest(int gameMode) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery("SELECT * FROM " + USER_TABLE + ";", null);

        int pb = cursor.getInt(gameMode);

        db.close();

        return pb;
    }

    /**
     * Executes simple update statement on our one row table.
     *
     * @param gameMode: parameter retrieved from TrainerActivity, corresponds with
     *                member of the same name in that class
     * @param starScore: session score
     * @return true if personal best is successful update, false otherwise
     */
    public boolean updatePB(int gameMode, int starScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        String column = modes[gameMode];

        String updateStmt =
                "UPDATE " + USER_TABLE +
                        " SET " + column + " = " + starScore;

        Cursor cursor = db.rawQuery(updateStmt, null);

        db.close();

        return cursor.getInt(gameMode) == starScore;
    }
}
