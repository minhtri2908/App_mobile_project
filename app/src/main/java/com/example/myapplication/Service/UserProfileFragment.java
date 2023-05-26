package com.example.myapplication.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ChapterAdapter;
import com.example.myapplication.Adapter.HistoryAdapter;
import com.example.myapplication.Adapter.MangaAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Database.HistoryDatabase;
import com.example.myapplication.Model.History;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfileFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recycler_history;

    private RecyclerView recycler_watchlist;

    private HistoryAdapter historyAdapter;

    private List<History> historyList = new ArrayList<>();

    private Button buttonLogOut;
    private RelativeLayout user_info;
    private TextView username;


    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    getHistoryList();
                }
            }
    );

    public ActivityResultLauncher<Intent> getLauncher() {
        return launcher;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        buttonLogOut = view.findViewById(R.id.button_log_out);
//        user_info = view.findViewById(R.id.user_profile_view);
//        username = view.findViewById(R.id.username);

        initUI(view);

        // Create adapter
        historyAdapter = new HistoryAdapter(this.getContext(), historyList);

        // Set recycler view
        recycler_history.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycler_history.setLayoutManager(layoutManager);
        recycler_history.setAdapter(historyAdapter);

//        HistoryDatabase.getInstance(this.getContext()).historyDao().deleteAllHistory();

//        buttonLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        getHistoryList();

//        recycler_watchlist = view.findViewById(R.id.recycler_watchlist);
//        recycler_watchlist.setHasFixedSize(true);
//        LinearLayoutManager w_layoutManager = new LinearLayoutManager(this.getContext());
//        w_layoutManager.setOrientation(RecyclerView.HORIZONTAL);
//        recycler_watchlist.setLayoutManager(w_layoutManager);
//        recycler_watchlist.setAdapter(mangaAdapter);

    }



    private void initUI(View view){
        recycler_history = view.findViewById(R.id.recycler_history);
    }

    private void addHistory() {
        CollectionReference colRef = db.collection("manga");
        String mangaId = "95O4G6v5V159TV7NYOKL";
        DocumentReference docRef = colRef.document(mangaId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();

                Log.d("doc", doc.getString("title"));

                String url = doc.getString("img");
                String title = doc.getString("title");
                String id = doc.getId();
                String author = doc.getString("author");
                String status = doc.getString("status");
                String description = doc.getString("description");

                Manga manga = new Manga(id, title, url, author, status, description);

                String chapter = "Ch.001";

                History historyObject = new History(manga, chapter, 0);
                HistoryDatabase.getInstance(this.getContext()).historyDao().insert(historyObject);
                Toast.makeText(this.getContext(), "Insert history succeed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getHistoryList() {
        historyList = HistoryDatabase.getInstance(this.getContext()).historyDao().getAll();
        historyAdapter.setData(historyList);

//        userReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                List<String> histories = (List<String>) documentSnapshot.get("history");
//
//
//                for (String history : histories){
//                    Log.d("ArrayList", history);
//                    DocumentReference docRef = colRef.document(history);
//                    docRef.get().addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot doc = task.getResult();
//                            Log.d("doc", doc.getString("title"));
//                            String url = doc.getString("img");
//                            String title = doc.getString("title");
//                            String id = doc.getId();
//                            String author = doc.getString("author");
//                            String status = doc.getString("status");
//                            String description = doc.getString("description");
//
//                            Manga myManga = new Manga(id, title, url, author, status, description);
//
//                            History historyObject = new History(myManga, "Ch.001", 0);
//                            historyList.add(historyObject);
//
//                        }
//                    });
//                }
//            }
//        });

    }

}