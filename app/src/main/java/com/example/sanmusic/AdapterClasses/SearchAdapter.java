package com.example.sanmusic.AdapterClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sanmusic.Activities.SlideUpPanelActivity;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;
import com.example.sanmusic.SongPopupMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.view_holder> {

    Context context;
    public static List<MusicFiles> searchedItems;

    public SearchAdapter(Context context, List<MusicFiles> searchedItems) {
        this.context = context;
        this.searchedItems = searchedItems;
    }

    @NonNull
    @Override
    public view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new view_holder(LayoutInflater.from(context).inflate(R.layout.list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull view_holder holder, int position) {
        holder.title.setText(searchedItems.get(position).getTitle());
        byte[] img = getImg(searchedItems.get(position).getPath());
        if (img != null){
            Glide.with(context).load(img).into(holder.art);
        } else {
            Glide.with(context).load(R.drawable.musical_notes_04).into(holder.art);
        }
        holder.itemView.setOnClickListener(v->{
            Intent Musicintent = new Intent(context, SlideUpPanelActivity.class);
            Musicintent.putExtra("position",position);
            Musicintent.putExtra("song_position",searchedItems.get(position).getIndexSong());
            Musicintent.putExtra("songs","SearchedSongs");
            context.startActivity(Musicintent);
        });
        holder.more.setOnClickListener(v -> {
            new SongPopupMenu(context, v).createSongPopupMenu(position);
            Toast.makeText(context, "the song position " + position, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return searchedItems.size();
    }

    public static class view_holder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView art;
        ImageView more;
        public view_holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name);
            art = itemView.findViewById(R.id.img);
            more = itemView.findViewById(R.id.song_more);
        }
    }

    public byte[] getImg(String url){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url);
        byte[] img_art = retriever.getEmbeddedPicture();
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return img_art;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void upDateList(List<MusicFiles> newList){
        searchedItems = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
