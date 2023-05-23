package com.example.myapplication.Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.myapplication.Adapter.ChapterAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    private TextView chapter;

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

        // Set imageview
        ImageView imageView = findViewById(R.id.image_view_manga);
        Picasso.get().load(Common.selected_manga.getImage()).into(imageView);

        // Set info
        TextView name = findViewById(R.id.name);
        TextView status = findViewById(R.id.status);
        TextView author = findViewById(R.id.author);
        chapter = findViewById(R.id.chapter);

        name.setText(Common.selected_manga.getName());
        status.setText(Common.selected_manga.getStatus());
        author.setText(Common.selected_manga.getAuthor());

        // Set detail
        TextView detail = findViewById(R.id.detail);
        detail.setText(Common.selected_manga.getDescription());

        // Get chapter
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
//                chapter.setText(chapterList.size());

                Log.i("chapterList", chapterList.toString());
                chapterAdapter = new ChapterAdapter(ChapterActivity.this, chapterList);
                recycler_chapter.setAdapter(chapterAdapter);
                chapterAdapter.notifyDataSetChanged();
                Common.chapterList = chapterList;
            }
        });
    }
}