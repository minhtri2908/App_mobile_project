package com.example.myapplication.Service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.Adapter.MangaPagesAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Database.HistoryDao;
import com.example.myapplication.Database.HistoryDatabase;
import com.example.myapplication.Model.History;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewMangaActivity extends AppCompatActivity {

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private List<Bitmap> mangaPageList = new ArrayList<>();
    private MangaPagesAdapter mangaPagesAdapter;

    private RecyclerView recyclerMangaPages;

    private MaterialToolbar toolbar;

    private GestureDetectorCompat gestureDetector;

    private View decorView;

    private LinearLayoutCompat utilityBar;

    private AppBarLayout appBarLayout;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private boolean isExpanded;

    private ProgressBar progressBar;

    private History mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            mHistory = (History) getIntent().getExtras().get("object_history");
        }

        DocumentReference mangaRef = FirebaseFirestore.getInstance().collection("manga_search").document(Common.selected_manga.getId());
        mangaRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Common.chapterList = (List<String>) documentSnapshot.get("chapter");
            }
        });

        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_view_manga);

        utilityBar = findViewById(R.id.utility_bar);
        utilityBar.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);

        appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.setExpanded(true);
        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    isExpanded = true;
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    isExpanded = false;
                } else {
                    isExpanded = false;
                }
            }
        });


        toolbar = findViewById(R.id.toolbar_view_manga);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentResult = new Intent();
                setResult(RESULT_OK, intentResult);
                finish();
            }
        });

        Button buttonPrevious = findViewById(R.id.button_previous);
        Button buttonNext = findViewById(R.id.button_next);

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.chapter_index == 0){
                    Toast.makeText(ViewMangaActivity.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                }   else{
                    Common.chapter_index--;
                    Common.selected_chapter = Common.chapterList.get(Common.chapter_index);
                    getMangaPages(Common.selected_chapter);
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.chapter_index == Common.chapterList.size() - 1){
                    Toast.makeText(ViewMangaActivity.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                }   else{
                    Common.chapter_index++;
                    Common.selected_chapter = Common.chapterList.get(Common.chapter_index);
                    getMangaPages(Common.selected_chapter);
                }
            }
        });

        mangaPagesAdapter = new MangaPagesAdapter(this.getBaseContext(), mangaPageList);

        recyclerMangaPages = findViewById(R.id.recycler_manga_pages);
        recyclerMangaPages.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getBaseContext());
        recyclerMangaPages.setLayoutManager(layoutManager);

        recyclerMangaPages.setAdapter(mangaPagesAdapter);

        getMangaPages(Common.selected_chapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void getMangaPages(String chapter) {
        addHistory();
        mangaPageList.clear();
        mangaPagesAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        Common.selected_chapter = chapter;
        toolbar.setTitle(Common.selected_chapter);

        // Reset Gesture Detector
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener());

        try{
            StorageReference pdfRef = storageRef
                    .child("chapter/" + Common.selected_manga.getName() + "/" + chapter + ".pdf");
            File file = File.createTempFile("pdf", "pdf");

            FileDownloadTask downloadTask = pdfRef.getFile(file);

            downloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);

                    // Render manga content
                    ParcelFileDescriptor fileDescriptor = null;
                    try {
                        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                        PdfRenderer renderer = new PdfRenderer(fileDescriptor);

                        // Render the first page of the PDF into a Bitmap object
                        int pageCount = renderer.getPageCount();
                        for (int i = 0; i < pageCount; i++){
                            PdfRenderer.Page page = renderer.openPage(i);
                            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                            mangaPageList.add(bitmap);
                            mangaPagesAdapter.notifyDataSetChanged();

                            page.close();
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Add touch listener to show/hide utility bar and toolbar
                    addTouchListener();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                    // Update the progress of the download
                    long totalBytes = snapshot.getTotalByteCount();
                    long bytesDownloaded = snapshot.getBytesTransferred();
                    double progress = (100.0 * bytesDownloaded) / totalBytes;

                    progressBar.setProgress((int)progress);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHistory() {
        HistoryDao instance = HistoryDatabase.getInstance(this).historyDao();
        String mangaId = Common.selected_manga.getId();

        if (mHistory == null){
            mHistory = HistoryDatabase.getInstance(this).historyDao().findHistoryById(mangaId);
        }

        if (mHistory != null){
            instance.delete(mHistory.getId());
            if (mHistory.getChapterIndex() != Common.chapter_index){
                mHistory.setChapter(Common.selected_chapter);
                mHistory.setChapterIndex(Common.chapter_index);
            }
        } else {
            mHistory = new History(Common.selected_manga, Common.selected_chapter, Common.chapter_index);
        }

        List<History> historyList = HistoryDatabase.getInstance(this).historyDao().getAll();

        instance.deleteAllHistory();
        historyList.add(0, mHistory);


        for (History history : historyList){
            List<History> histories = instance.getAll();
            for (History history1 : histories){
                Log.d("histories item", history1.toString());
            }

            instance.insert(history);
        }
    }

    private void addTouchListener(){
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener(){

//            @Override
//            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
//                boolean visible = ((decorView.getSystemUiVisibility() & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0)
//                        && (utilityBar.getVisibility() == View.VISIBLE);
//                if (visible){
//                    hideSystemUI();
//                    utilityBar.setVisibility(View.GONE);
//                }   else{
//                    showSystemUI();
//                    utilityBar.setVisibility(View.VISIBLE);
//                }
//                return true;
//            }


            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                boolean visible = utilityBar.getVisibility() == View.VISIBLE;
                if (visible){
                    utilityBar.setVisibility(View.GONE);
                    if (isExpanded)
                        appBarLayout.setExpanded(false);
                }   else{
                    utilityBar.setVisibility(View.VISIBLE);
                    if (!isExpanded)
                        appBarLayout.setExpanded(true);
                }
                return true;
            }
        });
    }

}

