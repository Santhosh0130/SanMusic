package com.example.sanmusic.Activities;

import static com.example.sanmusic.Activities.SlideUpPanelActivity.realSongs;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sanmusic.DataBase.FavDB;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.io.IOException;

public class MenuListActivity extends AppCompatActivity {

    RelativeLayout like,playlist,artist,delete, infoBack;
    ImageView art,back,like_btn;
    TextView songName, infoName, infoArtist, infoPath, infoAlbum, infoDuration;
    RelativeLayout InfoLayout;
    int position = -1;
    FavDB favDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //For Transparent status View
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        favDB = new FavDB(this);
        initViews();
        getIntentProperties();
        buttonsEvent();
        readAllFavSongs();

        songName.setText(realSongs.get(position).getTitle());
        byte[] img = getImg(realSongs.get(position).getPath());
        if (img != null){
            Glide.with(this).load(img).into(art);
        } else {
            Glide.with(this).load(R.drawable.musical_notes_04).into(art);
        }

    }

    @SuppressLint("Range")
    private void readAllFavSongs() {
        MusicFiles favFiles = realSongs.get(position);
        Cursor cursor = favDB.readAllData(position);
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while(cursor.moveToNext()){
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAV_STATUS));
                favFiles.setfStatus(item_fav_status);
                if (item_fav_status != null && item_fav_status.equals("1")){
                    like_btn.setImageResource(R.drawable.favorite);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    like_btn.setImageResource(R.drawable.favorite_border);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }


    private void buttonsEvent() {
        like.setOnClickListener(v->{
            MusicFiles mFiles = realSongs.get(position);

            if (mFiles.getfStatus().equals("0")) {
                mFiles.setfStatus("1");
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                favDB.insertDataIntoDB(position, "1");
                like_btn.setImageResource(R.drawable.favorite);
            } else {
                mFiles.setfStatus("0");
                Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                favDB.removeFav(position);
                like_btn.setImageResource(R.drawable.favorite_border);
            }
        });

        artist.setOnClickListener(v->{
            InfoLayout.setVisibility(View.VISIBLE);
            infoName.setText(realSongs.get(position).getTitle());
            infoAlbum.setText(realSongs.get(position).getAlbum());
            infoArtist.setText(realSongs.get(position).getArtist());
            int TotTime = Integer.parseInt(realSongs.get(position).getDuration()) / 1000;
            infoDuration.setText(formattedTime(TotTime));
            infoPath.setText(realSongs.get(position).getPath());
        });

        infoBack.setOnClickListener(v-> {
            InfoLayout.setVisibility(View.GONE);
        });

        back.setOnClickListener(v-> {
            finish();
        });
    }

    private void getIntentProperties() {
        position = getIntent().getIntExtra("MenuPosition",-1);
    }

    //For format the Duration into min & sec
    private String formattedTime(int currentPosition) {
        String Totalout = "";
        String Totalnew = "";
        String sec = String.valueOf(currentPosition % 60);
        String min = String.valueOf(currentPosition / 60);
        Totalout = min + ":" + sec;
        Totalnew = min + ":" + "0" + sec;
        if (sec.length() == 1){
            return Totalnew;
        } else {
            return Totalout;
        }
    }

    private void initViews() {
        like = findViewById(R.id.like_btn);
        playlist = findViewById(R.id.playlist_btn);
        artist = findViewById(R.id.artist_btn);
        delete = findViewById(R.id.delete_btn);
        art = findViewById(R.id.imgArt);
        back = findViewById(R.id.back_btn2);
        like_btn = findViewById(R.id.img_like);
        songName = findViewById(R.id.songName);
        InfoLayout = findViewById(R.id.song_info);
        infoName = findViewById(R.id.info_song_name);
        infoArtist = findViewById(R.id.info_song_artist);
        infoAlbum = findViewById(R.id.info_song_album);
        infoPath = findViewById(R.id.info_song_path);
        infoDuration = findViewById(R.id.info_song_duration);
        infoBack = findViewById(R.id.info_song_back);
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