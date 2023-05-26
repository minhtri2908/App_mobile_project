package com.example.myapplication.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Model.History;
import com.example.myapplication.Model.Manga;

import java.util.List;

@Dao
public interface MangaDao {
    @Insert
    void insert(Manga manga);

    @Query("SELECT * FROM watchlist")
    List<Manga> getAll();

    @Query("DELETE FROM watchlist")
    void deleteAllManga();

    @Query("DELETE FROM watchlist WHERE id = :mangaId")
    void delete(String mangaId);
}
