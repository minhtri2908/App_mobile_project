package com.example.myapplication.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Model.History;

@Database(entities = {History.class}, version = 2)
public abstract class HistoryDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "cache.db";
    private static HistoryDatabase instance;

    public static synchronized HistoryDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract HistoryDao historyDao();
}
