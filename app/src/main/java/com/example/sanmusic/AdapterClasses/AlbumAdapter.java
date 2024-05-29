package com.example.sanmusic.AdapterClasses;

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
import com.example.sanmusic.Activities.AlbumDetailsActivity;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.io.IOException;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.LikeViewHolder> {
    List<MusicFiles> album_list;
    Context context;
    int GridStyle_id;

    public AlbumAdapter(List<MusicFiles> album_list, Context context, int GridStyle) {
        this.album_list = album_list;
        this.context = context;
        this.GridStyle_id = GridStyle;
    }

    @NonNull
    @Override
    public LikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LikeViewHolder(LayoutInflater.from(context).inflate(GridStyle_id, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LikeViewHolder holder, int position) {
        holder.albumName.setText(album_list.get(position).getAlbumName());
        String img = album_list.get(position).getAlbumArt();
        if (img != null){
            Glide.with(context).load(img).into(holder.albumArt);
        } else {
            Glide.with(context).load(R.drawable.musical_notes_04).into(holder.albumArt);
        }
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, AlbumDetailsActivity.class);
            intent.putExtra("albumName",album_list.get(position).getAlbumName());
            context.startActivity(intent);

        });
        if (GridStyle_id == R.layout.list_items){
            holder.more.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return album_list.size();
    }

    public static class LikeViewHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        ImageView albumArt;
        ImageView more;
        public LikeViewHolder(@NonNull View itemView) {
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
