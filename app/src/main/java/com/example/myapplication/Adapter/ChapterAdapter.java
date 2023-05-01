package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Common.Common;
import com.example.myapplication.Interface.IRecyclerOnClick;
import com.example.myapplication.R;
import com.example.myapplication.Service.ViewMangaActivity;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    Context context;
    List<String> chapterList;

    public ChapterAdapter(Context context, List<String> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }


    @NonNull
    @Override
    public ChapterAdapter.ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new ChapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        holder.textViewChapterName.setText(new StringBuilder(chapterList.get(position)));

        // Add onclick listener
        holder.setiRecyclerOnClick(new IRecyclerOnClick() {
            @Override
            public void onClick(int position, View view) {
                // Start Read Manga Activity
                context.startActivity(new Intent(context, ViewMangaActivity.class));

                Common.selected_chapter = chapterList.get(position);
                Common.chapter_index = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewChapterName;
        IRecyclerOnClick iRecyclerOnClick;

        public void setiRecyclerOnClick(IRecyclerOnClick iRecyclerOnClick) {
            this.iRecyclerOnClick = iRecyclerOnClick;
        }

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewChapterName = itemView.findViewById(R.id.text_view_chapter_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerOnClick.onClick(getAbsoluteAdapterPosition(), view);
        }
    }
}

