package com.example.myapplication.Adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class MangaPagesAdapter extends RecyclerView.Adapter<MangaPagesAdapter.MangaPagesViewHolder> {
    Context context;
    List<Bitmap> mangaPageList;


    public MangaPagesAdapter(Context context, List<Bitmap> mangaPageList) {
        this.context = context;
        this.mangaPageList = mangaPageList;
    }

    @NonNull
    @Override
    public MangaPagesAdapter.MangaPagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manga_page, parent, false);
        return new MangaPagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaPagesViewHolder holder, int position) {
        holder.photoViewMangaScreen.setImageBitmap(mangaPageList.get(position));
    }

    @Override
    public int getItemCount() {
        return mangaPageList.size();
    }

    public class MangaPagesViewHolder extends RecyclerView.ViewHolder {
        ImageView photoViewMangaScreen;

        public MangaPagesViewHolder(@NonNull View itemView) {
            super(itemView);

            photoViewMangaScreen = itemView.findViewById(R.id.photo_view_manga_page);

        }
    }
}
