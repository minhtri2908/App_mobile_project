package com.example.myapplication.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.MangaAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfileFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference colRef = db.collection("manga");

    private RecyclerView recycler_history;

    private MangaAdapter mangaAdapter;

    private List<Manga> mangaList = new ArrayList<>();

    private Button buttonLogOut;

    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    buttonLogOut.setText("Log out");

                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonLogOut = view.findViewById(R.id.button_log_out);

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                someActivityResultLauncher.launch(intent);
            }
        });

        // Create adapter
        mangaAdapter = new MangaAdapter(this.getContext(), mangaList, R.layout.manga_item_small);

        // Set recycler view
        recycler_history = view.findViewById(R.id.recycler_history);
        recycler_history.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycler_history.setLayoutManager(layoutManager);
        recycler_history.setAdapter(mangaAdapter);

        // Get data from firestore
        if (mangaList.isEmpty()){
            getMangaList();
        }

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