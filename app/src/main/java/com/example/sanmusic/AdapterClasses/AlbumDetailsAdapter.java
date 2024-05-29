package com.example.sanmusic.AdapterClasses;

import static com.example.sanmusic.Activities.SlideUpPanelActivity.realSongs;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sanmusic.Activities.SlideUpPanelActivity;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;
import com.example.sanmusic.SongPopupMenu;

import java.io.IOException;
import java.util.List;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.viewHolder> {
    public static List<MusicFiles> album_list;
    Context context;
    SongPopupMenu menu;

    public AlbumDetailsAdapter(List<MusicFiles> album_list, Context context) {
        AlbumDetailsAdapter.album_list = album_list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.list_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.albumName.setText(album_list.get(position).getTitle());
        byte[] img = getImg(album_list.get(position).getPath());
        if (img != null){
            Glide.with(context).load(img).into(holder.albumArt);
        } else {
            Glide.with(context).load(R.drawable.musical_notes_04).into(holder.albumArt);
        }
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, SlideUpPanelActivity.class);
            intent.putExtra("sender","albumDetails");
            intent.putExtra("position",position);
            intent.putExtra("album_position",get_position(position));
            context.startActivity(intent);
        });
        holder.more.setOnClickListener(v -> {
            menu = new SongPopupMenu(context, v);
            menu.createSongPopupMenu(get_position(position));
        });
    }

    private int get_position(int position){
        for (int i=0 ; i < realSongs.size() ; i++){
            if (realSongs.get(i).getPath().equals(album_list.get(position).getPath())){
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return album_list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        ImageView albumArt;
        ImageView more;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.name);
            albumArt = itemView.findViewById(R.id.img);
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
