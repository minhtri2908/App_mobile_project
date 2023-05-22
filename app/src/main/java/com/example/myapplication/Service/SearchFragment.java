package com.example.myapplication.Service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.Adapter.MangaAdapter;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private MangaAdapter adapter;
    private EditText editTextSearch;
    private List<Manga> mangaList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Tìm kiếm dữ liệu khi nhấn Enter trên bàn phím
        editTextSearch = view.findViewById(R.id.search_bar);
        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchFirestore(v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // Hiển thị kết quả tìm kiếm trong RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MangaAdapter(this.getContext(), mangaList, R.layout.manga_item);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void searchFirestore(String searchQuery) {
        // Tạo truy vấn để tìm kiếm Manga với tiêu đề chứa searchQuery
        db.collection("manga")
                .orderBy("title")
                .startAt(searchQuery)
                .endAt(searchQuery + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Xóa dữ liệu cũ trong adapter và thêm dữ liệu mới từ Firestore vào
                        adapter.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Manga mangaItem = new Manga(document.getId(),
                                    document.getString("title"), document.getString("img"));
                            adapter.add(mangaItem);
                        }
                        // Cập nhật lại RecyclerView
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SearchFragment", "Error searching Firestore: " + e.getMessage());
                    }
                });
    }
}