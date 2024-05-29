package com.example.sanmusic.AdapterClasses;

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
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {
    Context context;
    int id ;
    public static List<MusicFiles> files;
    public MusicAdapter(Context context, List<MusicFiles> files, int id) {
        this.context = context;
        MusicAdapter.files = files;
        this.id = id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(id, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(files.get(position).getTitle());
        byte[] img = getImg(files.get(position).getPath());
        if (img != null){
            Glide.with(context).load(img).into(holder.art);
        } else {
            Glide.with(context).load(R.drawable.musical_notes_04).into(holder.art);
        }
        holder.itemView.setOnClickListener(v->{
            Intent Musicintent = new Intent(context, SlideUpPanelActivity.class);
            Musicintent.putExtra("position",position);
            Musicintent.putExtra("song_position",files.get(position).getIndexSong());
            Musicintent.putExtra("songs","Songs");
            context.startActivity(Musicintent);
        });
        if (id == R.layout.list_items) {
            holder.more.setOnClickListener(v -> {
                new SongPopupMenu(context, v).createSongPopupMenu(files.get(position).getIndexSong());
                Toast.makeText(context, "the song position " + files.get(position).getIndexSong(), Toast.LENGTH_SHORT).show();
            });
        }

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView art;
        ImageView more;
        public MyViewHolder(@NonNull View itemView) {
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
}
