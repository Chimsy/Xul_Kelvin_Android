/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package zw.co.chimsy.xulkelvin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import static zw.co.chimsy.xulkelvin.utils.AppConstants.DATABASE_NAME;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.DATABASE_VERSION;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ACTUAL_TOKEN;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_EMAIL;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_ID;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_NAME;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_PROGRAM;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_REG_NUMBER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_SEMESTER;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.KEY_YEAR;
import static zw.co.chimsy.xulkelvin.utils.AppConstants.TABLE_USER;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ACTUAL_TOKEN + " TEXT UNIQUE,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT ,"
                + KEY_REG_NUMBER + " TEXT ,"
                + KEY_YEAR + " INTEGER,"
                + KEY_SEMESTER + " INTEGER,"
                + KEY_PROGRAM + " TEXT" +
                ")";

        db.execSQL(CREATE_LOGIN_TABLE);

        Log.i(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */

    public void addUser(String token, String name, String email, String reg_num, int year,
						int semester, String program) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTUAL_TOKEN, token); // Email
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_REG_NUMBER, reg_num); // Created At
        values.put(KEY_YEAR, year); // Created At
        values.put(KEY_SEMESTER, semester); // Created At
        values.put(KEY_PROGRAM, program); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.i(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("actual_token", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("reg_num", cursor.getString(4));
            user.put("year", cursor.getString(5));
            user.put("semester", cursor.getString(6));
            user.put("program", cursor.getString(7));
        }
        cursor.close();
        db.close();
        // return user
        Log.i(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.i(TAG, "Deleted all user info from sqlite");
    }

}
