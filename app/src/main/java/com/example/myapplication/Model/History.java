package com.example.myapplication.Model;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "history")
public class History implements Serializable {
    @PrimaryKey @NonNull
    private String id;
    private String mangaName;
    private String mangaImage;

    private String mangaAuthor;

    private String mangaStatus;

    private String mangaDescription;

    private String chapter;

    private int chapterIndex;

    public History() {
    }

    public History(Manga manga, String chapter, int chapterIndex) {
        this.id = manga.getId();
        this.mangaName = manga.getName();
        this.mangaImage = manga.getImage();
        this.mangaAuthor = manga.getAuthor();
        this.mangaStatus = manga.getStatus();
        this.mangaDescription = manga.getDescription();
        this.chapter = chapter;
        this.chapterIndex = chapterIndex;
    }

    public Manga getManga(){
        Manga manga = new Manga(id, mangaName, mangaImage, mangaAuthor, mangaStatus, mangaDescription);
        return manga;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }

    public String getMangaImage() {
        return mangaImage;
    }

    public void setMangaImage(String mangaImage) {
        this.mangaImage = mangaImage;
    }

    public String getMangaAuthor() {
        return mangaAuthor;
    }

    public void setMangaAuthor(String mangaAuthor) {
        this.mangaAuthor = mangaAuthor;
    }

    public String getMangaStatus() {
        return mangaStatus;
    }

    public void setMangaStatus(String mangaStatus) {
        this.mangaStatus = mangaStatus;
    }

    public String getMangaDescription() {
        return mangaDescription;
    }

    public void setMangaDescription(String mangaDescription) {
        this.mangaDescription = mangaDescription;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    @Override
    public String toString() {
        return "History{id='" + id + "', title='" + mangaName + "', chapter=" + chapter + "}";
    }
}
