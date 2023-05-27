package com.example.myapplication.Database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Model.Manga;


@Database(entities = {Manga.class}, version = 2)
public abstract class WatchlistDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "watchlist.db";
    private static WatchlistDatabase instance;

    public static synchronized WatchlistDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), WatchlistDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract MangaDao mangaDao();
}
