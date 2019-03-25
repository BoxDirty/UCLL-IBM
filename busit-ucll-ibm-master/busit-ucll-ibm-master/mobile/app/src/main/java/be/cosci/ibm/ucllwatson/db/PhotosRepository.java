package be.cosci.ibm.ucllwatson.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import be.cosci.ibm.ucllwatson.db.item.PhotoItem;

/**
 *
 */
public class PhotosRepository implements PhotoTable {

    private Context context;
    private DBHelper dbHelper;
    private ContentValues contentValues;

    String[] columns = {
            _ID,
            KEY_INGREDIENT_NAME,
            KEY_PATH,
            KEY_TIME
    };


    public PhotosRepository(Context context) {
        this.context = context;
        this.contentValues = new ContentValues();
        this.dbHelper = DBHelper.getInstance(context.getApplicationContext());
    }

    public void prepareContentValues(PhotoItem photoItem) {
        contentValues.clear();
        contentValues.put(KEY_INGREDIENT_NAME, photoItem.getIngredientName());
        contentValues.put(KEY_PATH, photoItem.getPath());
        contentValues.put(KEY_TIME, photoItem.getTime());
    }

    private PhotoItem getPhotoItem(Cursor cursor) {
        return new PhotoItem(
                cursor.getLong(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT_NAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PATH)),
                cursor.getDouble(cursor.getColumnIndex(KEY_TIME))
        );
    }

    public void add(PhotoItem photoItem) {
        prepareContentValues(photoItem);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(
                TABLE_NAME_PHOTOS,
                null,
                contentValues
        );
        photoItem.setId(id);
    }

    public void update(PhotoItem photoItem) {
        prepareContentValues(photoItem);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(
                TABLE_NAME_PHOTOS,
                contentValues,
                _ID + " =? ",
                new String[]{String.valueOf(photoItem.getId())}
        );
    }

    public void remove(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                TABLE_NAME_PHOTOS,
                _ID + " =? ",
                new String[]{String.valueOf(id)}
        );
    }

    public PhotoItem getItem(long id) {
        PhotoItem photoItem = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_PHOTOS,
                columns,
                _ID + " =? ",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            photoItem = getPhotoItem(cursor);
        }
        cursor.close();
        return photoItem;
    }

    public List<PhotoItem> getAllItems() {
        List<PhotoItem> dataRecordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME_PHOTOS,
                columns,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                PhotoItem row = getPhotoItem(cursor);
                dataRecordList.add(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataRecordList;
    }
    public boolean allHaveBeenNamed(){
        int counter = 0;
        for (PhotoItem photoItem: getAllItems()) {
            if(!photoItem.getIngredientName().isEmpty()) counter++;
        }
        return counter == getAllItems().size();
    }
}