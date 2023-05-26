package com.example.myapplication.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Model.History;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(History history);

    @Query("SELECT * FROM history WHERE id = :MangaId")
    History findHistoryById(String MangaId);

    @Query("SELECT * FROM history")
    List<History> getAll();

    @Update
    void update(History history);

    @Query("DELETE FROM history")
    void deleteAllHistory();
}
