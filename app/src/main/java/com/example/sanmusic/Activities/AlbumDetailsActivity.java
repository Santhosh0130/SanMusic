package com.example.sanmusic.Activities;

import static com.example.sanmusic.Activities.SlideUpPanelActivity.realSongs;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sanmusic.AdapterClasses.AlbumDetailsAdapter;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsActivity extends AppCompatActivity {

    ImageView albumBanner;
    TextView albumName;
    RecyclerView recyclerView;
    String albumNameStr;
    List<MusicFiles> albumSongs = new ArrayList<>();
    AlbumDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        Intent intent = new Intent(this, SlideUpPanelActivity.class);

        albumBanner = findViewById(R.id.albumBanner);
        recyclerView = findViewById(R.id.recyclerviewforalbumdetails);
        albumName = findViewById(R.id.name);

        findViewById(R.id.album_back_btn).setOnClickListener(v -> finish());

        albumNameStr = getIntent().getStringExtra("albumName");
        albumName.setText(albumNameStr);

        //To add an Album
        int j = 0;
        for (int i = 0 ; i < realSongs.size() ; i++){
            if (albumNameStr != null && albumNameStr.equals(realSongs.get(i).getAlbum())){
                albumSongs.add(j,realSongs.get(i));
                j++;

            }
        }


        findViewById(R.id.play_all_btn).setOnClickListener(v -> {
            intent
                    .putExtra("albumDetails", "AlbumDetails")
                    .putExtra("position", 0)
                    .putExtra("albumPosition", albumSongs.get(0).getIndexSong());
            startActivity(intent);
        });

        findViewById(R.id.shuffule_btn1).setOnClickListener(v -> {
            intent
                    .putExtra("albumDetails", true)
                    .putExtra("position", 0)
                    .putExtra("albumPosition", albumSongs.get(0).getIndexSong());
            startActivity(intent);
        });

        if (albumSongs != null) {
            byte[] albumArt = getImg(albumSongs.get(0).getPath());
            if (albumArt != null) {
                Glide.with(this).load(albumArt).into(albumBanner);
            } else {
                Glide.with(this).load(R.drawable.musical_notes_04).into(albumBanner);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(albumSongs.isEmpty())){
            adapter = new AlbumDetailsAdapter(albumSongs,AlbumDetailsActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
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