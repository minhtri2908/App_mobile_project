package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Common.Common;
import com.example.myapplication.Interface.IRecyclerOnClick;
import com.example.myapplication.Model.History;
import com.example.myapplication.Model.Manga;
import com.example.myapplication.R;
import com.example.myapplication.Service.ChapterActivity;
import com.example.myapplication.Service.UserProfileFragment;
import com.example.myapplication.Service.ViewMangaActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    Context context;
    List<History> historyList;


    public void setData(List<History> historyList){
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Picasso.get().load(historyList.get(position).getMangaImage()).into(holder.imageViewHistory);
        holder.mangaName.setText(historyList.get(position).getMangaName());
        holder.mangaChapter.setText(historyList.get(position).getChapter());

        // Add onclick listener
        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(int position, View view) {
                Common.selected_manga = historyList.get(position).getManga();
                Common.selected_chapter = historyList.get(position).getChapter();
                Common.chapter_index = historyList.get(position).getChapterIndex();

                Intent intent = new Intent(context, ViewMangaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_history", historyList.get(position));
                intent.putExtras(bundle);

                // Start Chapter Activity
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
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewHistory;
        TextView mangaName;
        TextView mangaChapter;
        IRecyclerOnClick iRecyclerOnClick;

        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewHistory = (ImageView) itemView.findViewById(R.id.image_view_history_item);
            mangaName = (TextView) itemView.findViewById(R.id.manga_name);
            mangaChapter = (TextView) itemView.findViewById(R.id.manga_chapter);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerOnClick.onClick(getAbsoluteAdapterPosition(), view);
        }


    }
}
