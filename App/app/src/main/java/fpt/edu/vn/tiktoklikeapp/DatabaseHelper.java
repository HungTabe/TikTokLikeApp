package fpt.edu.vn.tiktoklikeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "VideoLinks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "links";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_URL + " TEXT)";
        db.execSQL(createTable);

        // Thêm seed data
        insertSeedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void insertSeedData(SQLiteDatabase db) {
        String[] videoNames = {
                "Video 1", "Video 2", "Video 3", "Video 4", "Video 5", "Video 6", "Video 7"
        };
        String[] videoUrls = {
                "https://www.youtube.com/embed/wlLuZwa9jxk",
                "https://www.youtube.com/embed/zJ4zL0H9Ouc",
                "https://www.youtube.com/embed/dQumqzNudyo",
                "https://www.youtube.com/embed/m-xR4WYc6tM",
                "https://www.youtube.com/embed/HMfY48KgxiA",
                "https://www.youtube.com/embed/gHozJJCnUyg",
                "https://www.youtube.com/embed/mQNDVfucqI4"
        };

        for (int i = 0; i < videoNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, videoNames[i]);
            values.put(COLUMN_URL, videoUrls[i]);
            db.insert(TABLE_NAME, null, values);
        }
    }

    public boolean addLink(String name, String url) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_URL, url);
            long result = db.insert(TABLE_NAME, null, values);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public Cursor getAllLinks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean updateLink(int id, String name, String url) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_URL, url);
            int result = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public boolean deleteLink(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            int result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}