package com.wael.mydiary.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {User.class, Entry.class}, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    private static final String DATABASE_NAME = "diary";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if(sInstance == null){
            synchronized (LOCK) {
                Log.d(TAG, "getsInstance: Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(TAG, "getsInstance: Getting the database instance");
        return sInstance;
    }
    public abstract EntryDao entryDao();
    public abstract UserDao userDao();


}
