package com.example.myapplication.Common;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Model.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static Manga selected_manga;
    public static String selected_chapter;
    public static int chapter_index = -1;

    public static List<String> chapterList = new ArrayList<>();

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser currentUser = mAuth.getCurrentUser();

}
