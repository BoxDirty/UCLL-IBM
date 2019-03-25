package be.cosci.ibm.ucllwatson.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 *
 */
public class DBHelper extends SQLiteOpenHelper implements PhotoTable {

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "ucllwatson.db";

    private static DBHelper dbHelper = null;

    public static synchronized DBHelper getInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new DBHelper(context.getApplicationContext());
        return dbHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_PHOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PHOTOS);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PHOTOS);
        onCreate(sqLiteDatabase);
    }
}