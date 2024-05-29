//package com.example.sanmusic.Fragments;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.SharedPreferences;
//import android.media.MediaMetadataRetriever;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.bumptech.glide.Glide;
//import com.example.sanmusic.MusicService;
//import com.example.sanmusic.R;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.progressindicator.LinearProgressIndicator;
//
//import java.io.IOException;
//
//public class BottomPlayerFragment extends Fragment implements ServiceConnection {
//
//    ImageView BottomNext_btn,BottomArt;
//    TextView BottomSongName,BottomArtistName;
//    FloatingActionButton BottomPlayPause_btn;
//    LinearProgressIndicator BottomSeekbar;
//    RelativeLayout Bottom_Layout;
//    MusicService musicService;
//    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
//    public static final String MUSIC_FILE = "STORED_FILE";
//    public static final String SONG_NAME = "SONG NAME";
//    public static final String ARTIST_NAME = "ARTIST NAME";
//    public boolean is_Played = true;
//
//    public BottomPlayerFragment(){
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_bottom_player, container, false);
////        BottomArt = view.findViewById(R.id.bottom_player_img);
////        BottomArtistName = view.findViewById(R.id.bottom_player_artist_name);
////        BottomNext_btn = view.findViewById(R.id.bottom_player_nextbtn);
////        BottomSongName = view.findViewById(R.id.bottom_player_song_name);
////        BottomPlayPause_btn = view.findViewById(R.id.bottom_player_pause_play_btn);
////        Bottom_Layout = view.findViewById(R.id.bottom_action);
//
//        //For Bottom Button Actions
//        BottomNext_btn.setOnClickListener(v->{
//            if (musicService != null) {
//                musicService.nextBtnClicked();
//                musicService.OnCompleted();
//                BottomSeekbar.setMax(musicService.getDuration() / 1000);
//                Handler handler = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        BottomSeekbar.setProgress(currentPosition);
//                        handler.postDelayed(this, 1000);
//                    }
//                });
//                if (getActivity() != null) {
//                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit();
//                    editor.putString(MUSIC_FILE, musicService.musicFilesList.get(musicService.position).getPath());
//                    editor.putString(ARTIST_NAME, musicService.musicFilesList.get(musicService.position).getArtist());
//                    editor.putString(SONG_NAME, musicService.musicFilesList.get(musicService.position).getTitle());
//                    editor.apply();
//
//                    SharedPreferences preferences = getActivity().getSharedPreferences(MUSIC_LAST_PLAYED,MODE_PRIVATE);
//                    String pathForBottom = preferences.getString(MUSIC_FILE,null);
//                    String songForBottom = preferences.getString(SONG_NAME,null);
//                    String artistForBottom = preferences.getString(ARTIST_NAME,null);
//                    if (pathForBottom != null){
//                        SHOW_MINI_PLAYER = true;
//                        PATH_FOR_BOTTOM = pathForBottom;
//                        ARTIST_FOR_BOTTOM = artistForBottom;
//                        SONG_FOR_BOTTOM = songForBottom;
//                    } else {
//                        SHOW_MINI_PLAYER = false;
//                        PATH_FOR_BOTTOM = null;
//                        ARTIST_FOR_BOTTOM = null;
//                        SONG_FOR_BOTTOM = null;
//                    }
//
//                    if (SHOW_MINI_PLAYER){
//                        BottomSongName.setText(SONG_FOR_BOTTOM);
//                        BottomArtistName.setText(ARTIST_FOR_BOTTOM);
//                        byte[] imgArt = getImg(PATH_FOR_BOTTOM);
//                        if (imgArt != null){
//                            Glide.with(requireContext()).load(imgArt).into(BottomArt);
//                        } else {
//                            Glide.with(requireContext()).load(R.drawable.musical_notes_04).into(BottomArt);
//                        }
//                    }
//                }
//            }
//        });
//        BottomPlayPause_btn.setOnClickListener(v->{
//            if (musicService != null) {
//                BottomSeekbar.setMax(musicService.getDuration() / 1000);
//                Handler handler = new Handler();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        BottomSeekbar.setProgress(currentPosition);
//                        handler.postDelayed(this, 1000);
//                    }
//                });
//                if (musicService.is_Playing()){
//                    BottomPlayPause_btn.setImageResource(R.drawable.play);
//                } else {
//                    BottomPlayPause_btn.setImageResource(R.drawable.pause);
//                }
//                musicService.pause_playBtnClicked();
//            }
//        });
//
//        return view;
//    }
//
//    void BottomView(){
//        Bottom_Layout.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
//            intent.putExtra("seekPosition", musicService.getCurrentPosition());
//            intent.putExtra("position", musicService.position);
//            startActivity(intent);
//        });
//    }
//    //Show the Mini Player
//    @Override
//    public void onResume() {
//        super.onResume();
//        BottomView();
//        if (SHOW_MINI_PLAYER){
//            if (PATH_FOR_BOTTOM != null){
//                BottomSongName.setText(SONG_FOR_BOTTOM);
//                BottomArtistName.setText(ARTIST_FOR_BOTTOM);
//                byte[] imgArt = getImg(PATH_FOR_BOTTOM);
//                if (imgArt != null){
//                    Glide.with(requireContext()).load(imgArt).into(BottomArt);
//                } else {
//                    Glide.with(requireContext()).load(R.drawable.musical_notes_04).into(BottomArt);
//                }
//                Intent Bottomintent = new Intent(getContext(),MusicService.class);
//                if (getContext() != null){
//                    getContext().bindService(Bottomintent,this, Context.BIND_AUTO_CREATE);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (getContext() != null){
//            getContext().unbindService(this);
//        }
//    }
//
//    public void setSeekbar(){
//        BottomSeekbar.setMax(musicService.getDuration() / 1000);
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                int currentPosition = musicService.getCurrentPosition() / 1000;
//                BottomSeekbar.setProgress(currentPosition);
//                handler.postDelayed(this, 1000);
//            }
//        });
//    }
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        MusicService.MyBinder binder = (MusicService.MyBinder) service;
//        musicService = binder.getService();
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        musicService = null;
//    }
//
//    public byte[] getImg(String url){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(url);
//        byte[] img_art = retriever.getEmbeddedPicture();
//        try {
//            retriever.release();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return img_art;
//    }
//}