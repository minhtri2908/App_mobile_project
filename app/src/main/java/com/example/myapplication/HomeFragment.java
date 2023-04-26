package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Create adapter
        mangaAdapter = new MangaAdapter(this.getContext(), mangaList);

        // Set recycler view
        recycler_manga = rootView.findViewById(R.id.recycler_manga);
        recycler_manga.setHasFixedSize(true);
        recycler_manga.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recycler_manga.setAdapter(mangaAdapter);

        // Get data from firestore
        getMangaList();
        Log.i("mangaList", String.valueOf(mangaList));

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getMangaList() {
        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot doc : task.getResult()){
                    String url = doc.getString("img");
                    String title = doc.getString("title");
                    String id = doc.getId();

                    Manga myManga = new Manga(id, title, url);
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