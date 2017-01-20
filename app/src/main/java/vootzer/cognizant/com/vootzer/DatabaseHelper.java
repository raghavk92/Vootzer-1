package vootzer.cognizant.com.vootzer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Geeta on 1/20/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_COMPANY = "company";
    private static final String COLUMN_USABILITY = "usability";
    SQLiteDatabase db;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL," +
            COLUMN_NAME + " TEXT NOT NULL," + COLUMN_NUMBER + " TEXT NOT NULL," + COLUMN_COMPANY + " TEXT," + COLUMN_USABILITY + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    //**** Method to insert data in database
    public void insertUser(Users u) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        String query = "select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();


        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, u.getName());
        values.put(COLUMN_NUMBER, u.getNumber());
        values.put(COLUMN_COMPANY, u.getCompany());
        values.put(COLUMN_USABILITY, u.getUsability());
        if (!searchDetails(u.getNumber()).equals(u.getName())) { //### only functioning for 0 id
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
    }

    //*** Search methods
    //###### check karna hai
    public String searchDetails(String number) {
        db = this.getReadableDatabase();
        String query = "select name,number from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        a = "not found";
        if (cursor.moveToFirst()) {
            do {

                b = cursor.getString(1); //number

                if (b.equals(number)) {
                    a = cursor.getString(0); //name
                    break;
                }
            } while (cursor.moveToNext());
        }
        return a;
    }

    public Cursor getAllData() {
        db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }

}
