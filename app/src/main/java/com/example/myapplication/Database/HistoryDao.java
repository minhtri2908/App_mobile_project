package com.example.myapplication.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntry {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public String date;
}
