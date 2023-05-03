package com.example.myapplication.Service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Adapter.MangaPagesAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.storage.FirebaseStorage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_view_manga);

        utilityBar = findViewById(R.id.utility_bar);
        utilityBar.setVisibility(View.GONE);

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



        toolbar = findViewById(R.id.toolbar_view_manga);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    getMangaPages(Common.chapterList.get(Common.chapter_index));
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
                    getMangaPages(Common.chapterList.get(Common.chapter_index));
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
        mangaPageList.clear();
        mangaPagesAdapter.notifyDataSetChanged();
        Common.selected_chapter = chapter;
        toolbar.setTitle(Common.selected_chapter);

        try{
            StorageReference pdfRef = storageRef
                    .child("chapter/" + Common.selected_manga.getName() + "/" + chapter + ".pdf");
            File file = File.createTempFile("pdf", "pdf");
            pdfRef.getFile(file)
                    .addOnCompleteListener(task -> {
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

                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        File file = File.createTempFile("cbz", "cbz");
//        pdfRef.getFile(file)
//                .addOnCompleteListener(task -> {
//                    ZipFile zipFile = null;
//                    try {
//                        zipFile = new ZipFile(file);
//                        Enumeration<? extends ZipEntry> entries = zipFile.entries();
//
//                        while (entries.hasMoreElements()) {
//                            ZipEntry entry = entries.nextElement();
//
//                            if (!entry.isDirectory()) {
//                                String name = entry.getName();
//                                if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
//                                    InputStream inputStream = zipFile.getInputStream(entry);
//                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                                    // Display the bitmap
//                                    mangaPageList.add(bitmap);
//
//                                    inputStream.close();
//                                }
//                            }
//                        }
//                        //Collections.sort(mangaPageList);
//                        mangaPagesAdapter.notifyDataSetChanged();
//                        zipFile.close();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
    }
}