package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    Context context;
    List<Manga> mangaList;


    public MangaAdapter(Context context, List<Manga> mangaList) {
        this.context = context;
        this.mangaList = mangaList;
    }

    @NonNull
    @Override
    public MangaAdapter.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manga_item, parent, false);
        return new MangaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaAdapter.MangaViewHolder holder, int position) {
        Picasso.get().load(mangaList.get(position).getImage()).into(holder.imageViewManga);
        holder.textViewManga.setText(mangaList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewManga;
        TextView textViewManga;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewManga = (ImageView) itemView.findViewById(R.id.image_view_manga_item);
            textViewManga = (TextView) itemView.findViewById(R.id.text_view_manga_item);
        }
    }
}
