package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Service.ChapterActivity;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Interface.IRecyclerOnClick;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.example.myapplication.Service.UserProfileFragment;
import com.example.myapplication.Service.ViewMangaActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    Context context;
    List<Manga> mangaList;

    int item;

    public void add(Manga mangaItem) {
        mangaList.add(mangaItem);
    }

    public void clear() {
        mangaList.clear();
    }

    public MangaAdapter(Context context, List<Manga> mangaList, int item) {
        this.context = context;
        this.mangaList = mangaList;
        this.item = item;
    }

    @NonNull
    @Override
    public MangaAdapter.MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(this.item, parent, false);
        return new MangaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Picasso.get().load(mangaList.get(position).getImage()).into(holder.imageViewManga);
        holder.textViewManga.setText(mangaList.get(position).getName());

        // Add onclick listener
        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(int position, View view) {
                // Start Chapter Activity
                Common.selected_manga = mangaList.get(position);
                Intent intent = new Intent(context, ChapterActivity.class);

                if (context instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    UserProfileFragment fragment = (UserProfileFragment) fragmentManager.findFragmentByTag("android:switcher:2131230863:2");
                    fragment.getLauncher().launch(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public void setData(List<Manga> watchList) {
        this.mangaList = watchList;
        notifyDataSetChanged();
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewManga;
        TextView textViewManga;
        IRecyclerOnClick iRecyclerOnClick;

        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewManga = (ImageView) itemView.findViewById(R.id.image_view_manga_item);
            textViewManga = (TextView) itemView.findViewById(R.id.text_view_manga_item);

            imageViewManga.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerOnClick.onClick(getAbsoluteAdapterPosition(), view);
        }
    }
}
