package com.example.myapplication.Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.Adapter.ChapterAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Database.WatchlistDatabase;
import com.example.myapplication.Model.Manga;
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

    private DocumentReference chapterReference = db.collection("manga_search").document(selected_manga_id);

    private RecyclerView recycler_chapter;
    private ChapterAdapter chapterAdapter;

    private ImageButton imageButton;

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
                Intent intentResult = new Intent();
                setResult(RESULT_OK, intentResult);
                finish();
            }
        });

        // Set add to watchlist button for Toolbar
        imageButton = findViewById(R.id.watchlist_button);

        Manga manga = WatchlistDatabase.getInstance(this).mangaDao().findMangaById(Common.selected_manga.getId());
        if (manga != null){
            imageButton.setImageDrawable(getDrawable(R.drawable.baseline_add_box_24_added));
            Common.selected_manga.setAddToWatchlist(true);
        } else{
            imageButton.setImageDrawable(getDrawable(R.drawable.baseline_add_box_24));
            Common.selected_manga.setAddToWatchlist(false);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.selected_manga.isAddToWatchlist()){
                    Common.selected_manga.setAddToWatchlist(false);

                    WatchlistDatabase.getInstance(getBaseContext()).mangaDao().delete(Common.selected_manga.getId());
                    imageButton.setImageDrawable(getDrawable(R.drawable.baseline_add_box_24));
                } else{
                    Common.selected_manga.setAddToWatchlist(true);

                    WatchlistDatabase.getInstance(getBaseContext()).mangaDao().insert(Common.selected_manga);
                    imageButton.setImageDrawable(getDrawable(R.drawable.baseline_add_box_24_added));

                    Toast.makeText(ChapterActivity.this, "Đã thêm vào mục theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set imageview
        ImageView imageView = findViewById(R.id.image_view_manga);
        Picasso.get().load(Common.selected_manga.getImage()).into(imageView);

        // Set info
        TextView name = findViewById(R.id.name);
        TextView status = findViewById(R.id.status);
        TextView author = findViewById(R.id.author);


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

                TextView chapter = findViewById(R.id.chapter);
                String chapterText = "Số chương hoàn thành: " + String.valueOf(chapterList.size());
                chapter.setText(chapterText);

                Log.i("chapterList", chapterList.toString());
                chapterAdapter = new ChapterAdapter(ChapterActivity.this, chapterList);
                recycler_chapter.setAdapter(chapterAdapter);
                chapterAdapter.notifyDataSetChanged();
            }
        });
    }
}