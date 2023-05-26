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
import com.example.myapplication.Database.WatchlistDatabase;
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

    private MangaAdapter watchlistAdapter;

    private List<Manga> watchList = new ArrayList<>();

    private List<History> historyList = new ArrayList<>();


    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    getHistoryList();
                    getWatchList();
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

        initUI(view);

        // Create adapter
        historyAdapter = new HistoryAdapter(this.getContext(), historyList);

        // Set recycler view
        recycler_history.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycler_history.setLayoutManager(layoutManager);
        recycler_history.setAdapter(historyAdapter);

        getHistoryList();

        // Watchlist
        watchlistAdapter = new MangaAdapter(this.getContext(), watchList, R.layout.manga_item_small);

        // Set recycler view
        recycler_watchlist = view.findViewById(R.id.recycler_watchlist);
        recycler_watchlist.setHasFixedSize(true);
        LinearLayoutManager w_layoutManager = new LinearLayoutManager(this.getContext());
        w_layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycler_watchlist.setLayoutManager(w_layoutManager);
        recycler_watchlist.setAdapter(watchlistAdapter);

        getWatchList();
    }


    private void initUI(View view){
        recycler_history = view.findViewById(R.id.recycler_history);
    }


    private void getHistoryList() {
        historyList = HistoryDatabase.getInstance(this.getContext()).historyDao().getAll();
        historyAdapter.setData(historyList);

    }

    private void getWatchList(){
        watchList = WatchlistDatabase.getInstance(this.getContext()).mangaDao().getAll();
        watchlistAdapter.setData(watchList);
    }

}