package com.example.myapplication.Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toolbar;

import com.example.myapplication.Adapter.ChapterAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    private static final String TAG = "ChapterActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String selected_manga_id = Common.selected_manga.getId();

    private DocumentReference chapterReference = db.collection("manga").document(selected_manga_id);

    private RecyclerView recycler_chapter;
    private ChapterAdapter chapterAdapter;

    private List<String> chapterList = new ArrayList<>();

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

        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChapterActivity.this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new MaterialDividerItemDecoration(this, layoutManager.getOrientation()));

        getChapterList();

    }

    private void getChapterList() {
        chapterReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                chapterList = (List<String>) documentSnapshot.get("chapter");

                Log.i("chapterList", chapterList.toString());
                chapterAdapter = new ChapterAdapter(ChapterActivity.this, chapterList);
                recycler_chapter.setAdapter(chapterAdapter);
                chapterAdapter.notifyDataSetChanged();
                Common.chapterList = chapterList;
            }
        });
    }
}