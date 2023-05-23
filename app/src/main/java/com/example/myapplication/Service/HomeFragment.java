package com.example.myapplication.Service;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaSession2Service;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Adapter.MangaAdapter;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference colRef = db.collection("manga");

    private RecyclerView recycler_manga;

    private MangaAdapter mangaAdapter;

    private List<Manga> mangaList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create adapter
        mangaAdapter = new MangaAdapter(this.getContext(), mangaList, R.layout.manga_item);

        // Set recycler view
        recycler_manga = view.findViewById(R.id.recycler_manga);
        recycler_manga.setHasFixedSize(true);
        recycler_manga.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recycler_manga.setAdapter(mangaAdapter);

        // Get data from firestore
        if (mangaList.isEmpty()){
            getMangaList();
        }
        Log.i("mangaList", String.valueOf(mangaList));
    }

    private void getMangaList() {
        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot doc : task.getResult()){
                    String url = doc.getString("img");
                    String title = doc.getString("title");
                    String id = doc.getId();
                    String author = doc.getString("author");
                    String status = doc.getString("status");
                    String description = doc.getString("description");

                    Manga myManga = new Manga(id, title, url, author, status, description);
                    mangaList.add(myManga);
                    Log.i("mangaList data", myManga.toString());
                }
                mangaAdapter.notifyDataSetChanged();
            }   else{
                Log.d(TAG, "Error getting doc: ", task.getException());
            }
        });
    }
}