package com.example.sanmusic.Activities;
//
//import static com.example.sanmusic.Activities.MainActivity.favorites;
//import static com.example.sanmusic.Activities.MainActivity.musicFiles;
//import static com.example.sanmusic.Activities.MainActivity.repeat_boolean;
//import static com.example.sanmusic.Activities.MainActivity.shuffle_boolean;
//import static com.example.sanmusic.AdapterClasses.AlbumDetailsAdapter.album_list;
//import static com.example.sanmusic.AdapterClasses.FavoritesAdapter.favorites_list;
//import static com.example.sanmusic.AdapterClasses.MusicAdapter.files;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.GradientDrawable;
//import android.media.MediaMetadataRetriever;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
import androidx.appcompat.app.AppCompatActivity;
//import androidx.palette.graphics.Palette;
//
//import com.bumptech.glide.Glide;
//import com.example.sanmusic.ActionPlay;
//import com.example.sanmusic.MusicFiles;
//import com.example.sanmusic.MusicService;
//import com.example.sanmusic.R;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
public class MusicPlayerActivity extends AppCompatActivity {
//
//    TextView songName, artistName, totalTime, playTime, topSongName;
//    FloatingActionButton play_pause;
//    SeekBar seekBar;
//    int position = -1;
//    public static List<MusicFiles> list_songs;
//    ImageView shuffle,previous,next,repeat, menu,back,img_art,close;
//    public static Uri uri;
//    private final Handler handler = new Handler();
//    public Thread play_pause_thread,next_thread,prev_thread;
//    MusicService musicService;
//    int seekPosition = -1;
//
//    @SuppressLint("ResourceType")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //For Full screen View
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//
//        setContentView(R.layout.activity_music_player);
//        initViews();
//        getIntentMethod();
//
////        menu.setOnClickListener(v->{
////            Intent MenuIntent = new Intent(this,MenuListActivity.class);
////            MenuIntent.putExtra("MenuPosition",position);
////            startActivity(MenuIntent);
////        });
//
//        back.setOnClickListener(v -> {
//            finish();
//        });
//
//        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (musicService != null){
//                    set_progress(musicService.getCurrentPosition() / 1000);
//                }
//                handler.postDelayed(this,1000);
//            }
//        });
//
//
//        //For change the seekbar Position every one sec
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser){
//                    set_seek(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//
//        //For Shuffle Button
//        shuffle.setOnClickListener(v->{
//            if (shuffle_boolean){
//                shuffle_boolean = false;
//                shuffle.setImageResource(R.drawable.shuffle_off);
//            } else {
//                shuffle_boolean = true;
//                shuffle.setImageResource(R.drawable.shuffle_on);
//            }
//        });
//
//        //For Repeat Button
//        repeat.setOnClickListener(v->{
//            if (repeat_boolean){
//                repeat_boolean = false;
//                repeat.setImageResource(R.drawable.repeat_off);
//            } else {
//                repeat_boolean = true;
//                repeat.setImageResource(R.drawable.repeat_on);
//            }
//        });
//    }
//
//    private void set_progress(int currentPos){
//        seekBar.setProgress(currentPos);
//        playTime.setText(formattedTime(currentPos));
//    }
//
//    private void set_seek(int progress){
//        if (musicService != null) {
//            musicService.seek_to(progress * 1000);
//        }
//    }
//
//    //To Store the Passed intent values & Start the MediaPlayer
//    private void getIntentMethod() {
//        position = getIntent().getIntExtra("position",-1);
//        seekPosition = getIntent().getIntExtra("seekPosition", -1);
//
//        Toast.makeText(this,"Position is: "+seekPosition / 1000,Toast.LENGTH_LONG).show();
//
//        String sender = getIntent().getStringExtra("sender");
//        String favorite = getIntent().getStringExtra("favorites");
//
//        if (sender != null && sender.equals("albumDetails")){
//            list_songs = album_list;
//        } else if (favorite != null && favorite.equals("FavList")) {
//            Toast.makeText(this, "Checked "+favorites_list.isEmpty(), Toast.LENGTH_SHORT).show();
//            list_songs = favorites_list;
//        } else {
//            list_songs = files;
//        }
//
//        if (list_songs != null){
//            play_pause.setImageResource(R.drawable.pause);
//            uri = Uri.parse(list_songs.get(position).getPath());
//            Toast.makeText(this, "Checked "+list_songs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//        }
//
//        //For Music Services
//        Intent Serviceintent = new Intent(this,MusicService.class);
//        Serviceintent.putExtra("servicePosition", position);
//        Serviceintent.putExtra("seekPosition", seekPosition / 1000);
//        this.startService(Serviceintent);
//    }
//
//
//    //For all the threads
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent Bindintent = new Intent(this,MusicService.class);
//        bindService(Bindintent,this,Context.BIND_AUTO_CREATE);
//        PlayPauseThread_btn();
//        NextThread_btn();
//        PrevThread_btn();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unbindService(this);
//    }
//
//    //pause & play button
//    private void PlayPauseThread_btn() {
//        play_pause_thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                play_pause.setOnClickListener(v-> PlayPauseThread_btnClicked());
//            }
//        };
//        play_pause_thread.start();
//    }
//    public void PlayPauseThread_btnClicked() {
//        if (musicService.is_Playing()){
//            play_pause.setImageResource(R.drawable.play);
//            musicService.pause();
//            seekBar.setMax(musicService.getDuration() / 1000);
//
//            //For change seekbar position every one sec
//            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (musicService != null){
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        seekBar.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//        } else{
//            play_pause.setImageResource(R.drawable.pause);
//            musicService.start();
//            seekBar.setMax(musicService.getDuration() / 1000);
//
//            //For change seekbar position every one sec
//            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (musicService != null){
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        seekBar.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//        }
//    }
//
//    //previous button
//    private void PrevThread_btn() {
//        prev_thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                previous.setOnClickListener(v -> PrevThread_btnClicked());
//            }
//        };
//        prev_thread.start();
//    }
//    public void PrevThread_btnClicked() {
//        if (musicService.is_Playing()){
//            musicService.stop();
//            musicService.release();
//
//            //Check if user already clicked shuffle or repeat buttons and modify the position
//            if (shuffle_boolean && !repeat_boolean){
//                position = getShuffle(list_songs.size() - 1);
//            } else if (!shuffle_boolean && !repeat_boolean) {
//                position = (position - 1) < 0 ? list_songs.size() : (position - 1);
//            }
//
//            uri = Uri.parse(list_songs.get(position).getPath());
//            musicService.createMediaPlayer(position);
//            metaData(uri);
//            songName.setText(list_songs.get(position).getTitle());
//            artistName.setText(list_songs.get(position).getArtist());
//            topSongName.setText(list_songs.get(position).getTitle());
//            seekBar.setMax(musicService.getDuration() / 1000);
//            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (musicService != null){
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        seekBar.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//            musicService.OnCompleted();
//            play_pause.setBackgroundResource(R.drawable.pause);
//            musicService.start();
//        } else {
//            musicService.stop();
//            musicService.release();
//
//            //Check if user already clicked shuffle or repeat buttons and modify the position
//            if (shuffle_boolean && !repeat_boolean){
//                position = getShuffle(list_songs.size() - 1);
//            } else if (!shuffle_boolean && !repeat_boolean) {
//                position = (position - 1) < 0 ? list_songs.size() : (position - 1);
//            }
//
//            uri = Uri.parse(list_songs.get(position).getPath());
//            musicService.createMediaPlayer(position);
//            metaData(uri);
//            songName.setText(list_songs.get(position).getTitle());
//            artistName.setText(list_songs.get(position).getArtist());
//            topSongName.setText(list_songs.get(position).getTitle());
//            seekBar.setMax(musicService.getDuration() / 1000);
//            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (musicService != null){
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        seekBar.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//            musicService.OnCompleted();
//            play_pause.setBackgroundResource(R.drawable.play);
//        }
//    }
//
//    //next button
//    private void NextThread_btn() {
//        next_thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                next.setOnClickListener(v -> NextThread_btnClicked());
//            }
//        };
//        next_thread.start();
//    }
//    public void NextThread_btnClicked() {
//        if (musicService.is_Playing()){
//            musicService.stop();
//            musicService.release();
//
//            //Check if user already clicked shuffle or repeat buttons and modify the position
//            if (shuffle_boolean && !repeat_boolean){
//                position = getShuffle(list_songs.size() - 1);
//            } else if (!shuffle_boolean && !repeat_boolean) {
//                position = (position + 1) % list_songs.size();
//            }
//
//            uri = Uri.parse(list_songs.get(position).getPath());
//            musicService.createMediaPlayer(position);
//            metaData(uri);
//            songName.setText(list_songs.get(position).getTitle());
//            artistName.setText(list_songs.get(position).getArtist());
//            topSongName.setText(list_songs.get(position).getTitle());
//            seekBar.setMax(musicService.getDuration() / 1000);
//            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (musicService != null){
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        seekBar.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//            musicService.OnCompleted();
//            play_pause.setBackgroundResource(R.drawable.pause);
//            musicService.start();
//        } else {
//            musicService.stop();
//            musicService.release();
//
//            //Check if user already clicked shuffle or repeat buttons and modify the position
//            if (shuffle_boolean && !repeat_boolean){
//                position = getShuffle(list_songs.size() - 1);
//            } else if (!shuffle_boolean && !repeat_boolean) {
//                position = (position + 1) % list_songs.size();
//            }
//
//            uri = Uri.parse(list_songs.get(position).getPath());
//            musicService.createMediaPlayer(position);
//            metaData(uri);
//            songName.setText(list_songs.get(position).getTitle());
//            artistName.setText(list_songs.get(position).getArtist());
//            topSongName.setText(list_songs.get(position).getTitle());
//            seekBar.setMax(musicService.getDuration() / 1000);
//            MusicPlayerActivity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (musicService != null){
//                        int currentPosition = musicService.getCurrentPosition() / 1000;
//                        seekBar.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//            musicService.OnCompleted();
//            play_pause.setBackgroundResource(R.drawable.play);
//        }
//    }
//
//    //For Shuffle the songs or Repeat the song
//    private int getShuffle(int i) {
//        Random ran = new Random();
//        return ran.nextInt(i + 1);
//    }
//
//    private void metaData(Uri uri){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri.toString());
//        byte[] art = retriever.getEmbeddedPicture();
//
//        int TotTime = Integer.parseInt(list_songs.get(position).getDuration()) / 1000;
//        totalTime.setText(formattedTime(TotTime));
//
//        Bitmap bitmap;
//        if (art != null){
//            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
//
//            ImageViewAnimation(this,img_art,bitmap);
//
//            //It's API,User for match the dominate Color into gradient
//            Palette.from(bitmap).generate(palette -> {
//                Palette.Swatch swatch = palette.getDominantSwatch();
//                if(swatch != null){
//                    ImageView gradient = findViewById(R.id.gradient);
////                    RelativeLayout relativeLayout = findViewById(R.id.player_relative);
//                    gradient.setBackgroundResource(R.drawable.main_bg);
////                    relativeLayout.setBackgroundResource(R.drawable.without_gradient_main_bg);
//                    GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),0x00000000});
//                    gradient.setBackground(drawable1);
//                    GradientDrawable drawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
////                    relativeLayout.setBackground(drawable2);
//
//                    songName.setTextColor(swatch.getTitleTextColor());
//                    topSongName.setTextColor(swatch.getTitleTextColor());
//                    artistName.setTextColor(swatch.getBodyTextColor());
//                    totalTime.setTextColor(swatch.getBodyTextColor());
//                    playTime.setTextColor(swatch.getBodyTextColor());
//                } else {
//                    ImageView gradient = findViewById(R.id.gradient);
////                    RelativeLayout relativeLayout = findViewById(R.id.player_relative);
//                    gradient.setBackgroundResource(R.drawable.bottom_gradient_bg);
////                    relativeLayout.setBackgroundResource(R.drawable.without_gradient_main_bg);
//                    GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0x00000000});
//                    gradient.setBackground(drawable1);
//                    GradientDrawable drawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0xff000000});
////                    relativeLayout.setBackground(drawable2);
//
//                    songName.setTextColor(Color.WHITE);
//                    topSongName.setTextColor(Color.WHITE);
//                    artistName.setTextColor(Color.WHITE);
//                    totalTime.setTextColor(Color.WHITE);
//                    playTime.setTextColor(Color.WHITE);
//                }
//            });
//        } else {
//            //If any album art not there put on the default Image
//            Glide.with(getApplicationContext()).asBitmap().load(R.drawable.musical_notes_04).into(img_art);
//
//            ImageView gradient = findViewById(R.id.gradient);
////            RelativeLayout relativeLayout = findViewById(R.id.player_relative);
//            gradient.setBackgroundResource(R.drawable.main_bg);
////            relativeLayout.setBackgroundResource(R.drawable.without_gradient_main_bg);
//
//            songName.setTextColor(Color.WHITE);
//            artistName.setTextColor(Color.WHITE);
//            topSongName.setTextColor(Color.WHITE);
//            totalTime.setTextColor(Color.WHITE);
//            playTime.setTextColor(Color.WHITE);
//        }
//    }
//
//    //For Animate the Images, After click next or prev Buttons
//    private void ImageViewAnimation(Context context, ImageView imageView, Bitmap bitmap){
//        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
//        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
//
//        animOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                Glide.with(context).asBitmap().load(bitmap).into(imageView);
//
//                animIn.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                imageView.startAnimation(animIn);
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        imageView.startAnimation(animOut);
//    }
//
//    //For format the Duration into min & sec
//    private String formattedTime(int currentPosition) {
//        String Totalout = "";
//        String Totalnew = "";
//        String sec = String.valueOf(currentPosition % 60);
//        String min = String.valueOf(currentPosition / 60);
//        Totalout = min + ":" + sec;
//        Totalnew = min + ":" + "0" + sec;
//        if (sec.length() == 1){
//            return Totalnew;
//        } else {
//            return Totalout;
//        }
//    }
//
//    //For Initialize the id's
//    private void initViews() {
//        songName = findViewById(R.id.song_name);
//        artistName = findViewById(R.id.artist_name);
//        totalTime = findViewById(R.id.total_time);
//        playTime = findViewById(R.id.played_time);
//        play_pause = findViewById(R.id.play_pause);
//        seekBar = findViewById(R.id.seekbar);
//        shuffle = findViewById(R.id.shuffule_btn);
//        previous = findViewById(R.id.previous_btn);
//        next = findViewById(R.id.next_btn);
//        repeat = findViewById(R.id.repeat_btn);
//        img_art = findViewById(R.id.img_art);
////        close = findViewById(R.id.close);
//
//        topSongName = findViewById(R.id.now_playing);
//        topSongName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        topSongName.setSelected(true);
//
//        back = findViewById(R.id.back_btn);
//
//        menu = findViewById(R.id.menu_btn);
//    }
//
//
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        MusicService.MyBinder binder = (MusicService.MyBinder) service;
//        musicService = binder.getService();
//
//        if (musicService != null){
//
//            songName.setText(list_songs.get(position).getTitle());
//            artistName.setText(list_songs.get(position).getArtist());
//            topSongName.setText(list_songs.get(position).getTitle());
//
//            musicService.OnCompleted();
//        }
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        musicService = null;
//    }
}