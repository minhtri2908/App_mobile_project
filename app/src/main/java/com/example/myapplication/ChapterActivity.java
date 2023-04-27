package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ChangedPackages;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.myapplication.Adapter.ChapterAdapter;
import com.example.myapplication.Adapter.MangaAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Model.Chapter;
import com.example.myapplication.Model.Manga;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    private static final String TAG = "ChapterActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String selected_manga_id = Common.selected_manga.getId();

    private CollectionReference chapterReference = db.collection("manga/" + selected_manga_id + "/chapters");

    private RecyclerView recycler_chapter;
    private ChapterAdapter chapterAdapter;

    private List<Chapter> chapterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        // Set manga name for Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chapter);
        toolbar.setTitle(Common.selected_manga.getName());

        // Set go back icon for Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back
                finish();
            }
        });

        // Create adapter
        chapterAdapter = new ChapterAdapter(ChapterActivity.this, chapterList);

        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChapterActivity.this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new MaterialDividerItemDecoration(this, layoutManager.getOrientation()));
        recycler_chapter.setAdapter(chapterAdapter);

        getChapterList();
    }

    private void getChapterList() {
        chapterReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot doc : task.getResult()){
                    String name = doc.getString("name");
                    String url = doc.getString("url");
                    String id = doc.getId();

                    Chapter myChapter = new Chapter(id, name, url);
                    chapterList.add(myChapter);
                }
                chapterAdapter.notifyDataSetChanged();
            }   else{
                Log.d(TAG, "Error getting doc: ", task.getException());
            }
        });
    }
}
