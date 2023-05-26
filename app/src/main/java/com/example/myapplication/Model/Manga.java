package com.example.myapplication.Model;

public class Manga {
    private String id;
    private String name;
    private String image;

    private String author;

    private String status;

    private String description;

    public Manga() {
    }

    public Manga(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Manga(String id, String name, String image, String author, String status, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.author = author;
        this.status = status;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Manga{id='" + id + "', title='" + name + "', author=" + author + "}";
    }
}
