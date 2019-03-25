package be.cosci.ibm.ucllwatson.db;

import android.provider.BaseColumns;

/**
 * Created by Petr on 27-Mar-18.
 */
public interface PhotoTable extends BaseColumns {

    String TABLE_NAME_PHOTOS = "Photos";
    String KEY_INGREDIENT_NAME = "ingredient_name";
    String KEY_PATH = "path";
    String KEY_TIME = "time";

    String CREATE_TABLE_PHOTOS = "CREATE TABLE " + TABLE_NAME_PHOTOS + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_INGREDIENT_NAME + " TEXT,"
            + KEY_PATH + " TEXT,"
            + KEY_TIME + " DOUBLE"
            + ")";
}