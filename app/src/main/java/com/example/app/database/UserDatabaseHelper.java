package com.example.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.app.models.User;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "auth.db";
    private static final int DB_VERSION = 2; // bump for constraints, indices, audit columns

    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT NOT NULL, "
                + COL_EMAIL + " TEXT, "
                + COL_PASSWORD + " TEXT NOT NULL, "
                + "created_at INTEGER NOT NULL DEFAULT (strftime('%s','now')*1000), "
                + "updated_at INTEGER NOT NULL DEFAULT (strftime('%s','now')*1000), "
                + "UNIQUE(" + COL_USERNAME + ") ON CONFLICT ABORT, "
                + "UNIQUE(" + COL_EMAIL + ") ON CONFLICT ABORT"
                + ")";
        db.execSQL(createUsers);
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_username ON " + TABLE_USERS + "(" + COL_USERNAME + ")");
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_email ON " + TABLE_USERS + "(" + COL_EMAIL + ")");
        // trigger to maintain updated_at
        db.execSQL("CREATE TRIGGER IF NOT EXISTS trg_users_update_ts AFTER UPDATE ON " + TABLE_USERS + " \n" +
                "BEGIN \n" +
                "  UPDATE " + TABLE_USERS + " SET updated_at=(strftime('%s','now')*1000) WHERE rowid=NEW.rowid; \n" +
                "END");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add audit columns and indices if upgrading from v1
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN created_at INTEGER");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN updated_at INTEGER");
            db.execSQL("UPDATE " + TABLE_USERS + " SET created_at = COALESCE(created_at, strftime('%s','now')*1000), updated_at = COALESCE(updated_at, strftime('%s','now')*1000)");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_username ON " + TABLE_USERS + "(" + COL_USERNAME + ")");
            db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_email ON " + TABLE_USERS + "(" + COL_EMAIL + ")");
            db.execSQL("CREATE TRIGGER IF NOT EXISTS trg_users_update_ts AFTER UPDATE ON " + TABLE_USERS + " \n" +
                    "BEGIN \n" +
                    "  UPDATE " + TABLE_USERS + " SET updated_at=(strftime('%s','now')*1000) WHERE rowid=NEW.rowid; \n" +
                    "END");
        }
    }

    public long insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());
        return db.insert(TABLE_USERS, null, values);
    }

    public java.util.List<User> getAllUsers() {
        java.util.List<User> result = new java.util.ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, null, null, null, null, null, COL_ID + " DESC");
        try {
            while (c != null && c.moveToNext()) {
                User u = new User();
                int idxId = c.getColumnIndex(COL_ID);
                int idxUser = c.getColumnIndex(COL_USERNAME);
                int idxEmail = c.getColumnIndex(COL_EMAIL);
                int idxPass = c.getColumnIndex(COL_PASSWORD);
                if (idxId >= 0) u.setId(c.getLong(idxId));
                if (idxUser >= 0) u.setUsername(c.getString(idxUser));
                if (idxEmail >= 0) u.setEmail(c.getString(idxEmail));
                if (idxPass >= 0) u.setPassword(c.getString(idxPass));
                result.add(u);
            }
        } finally {
            if (c != null) c.close();
        }
        return result;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, null, COL_USERNAME + "=?", new String[]{username}, null, null, null);
        try {
            if (c != null && c.moveToFirst()) {
                User u = new User();
                int idxId = c.getColumnIndex(COL_ID);
                int idxUser = c.getColumnIndex(COL_USERNAME);
                int idxEmail = c.getColumnIndex(COL_EMAIL);
                int idxPass = c.getColumnIndex(COL_PASSWORD);

                if (idxId >= 0) u.setId(c.getLong(idxId));
                if (idxUser >= 0) u.setUsername(c.getString(idxUser));
                if (idxEmail >= 0) u.setEmail(c.getString(idxEmail));
                if (idxPass >= 0) u.setPassword(c.getString(idxPass));
                return u;
            }
        } finally {
            if (c != null) c.close();
        }
        return null;
    }

    public int updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_PASSWORD, newPassword);
        return db.update(TABLE_USERS, v, COL_USERNAME + "=?", new String[]{username});
    }

    public int countUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public int updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_EMAIL, user.getEmail());
        v.put(COL_PASSWORD, user.getPassword());
        return db.update(TABLE_USERS, v, COL_USERNAME + "=?", new String[]{user.getUsername()});
    }

    public int deleteUser(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_USERS, COL_ID + "=?", new String[]{String.valueOf(id)});
    }
    
}


