package com.example.sanmusic.Activities;

import static com.example.sanmusic.AdapterClasses.AddFavAdapter.FavFiles;
import static com.example.sanmusic.AdapterClasses.AlbumDetailsAdapter.album_list;
import static com.example.sanmusic.AdapterClasses.MusicAdapter.files;
import static com.example.sanmusic.AdapterClasses.SearchAdapter.searchedItems;
import static com.example.sanmusic.MusicService.is_Created;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.example.sanmusic.ActionPlay;
import com.example.sanmusic.DataBase.FavDB;
import com.example.sanmusic.FetchSongs;
import com.example.sanmusic.Fragments.AlbumFragment;
import com.example.sanmusic.Fragments.FavoritesFragment;
import com.example.sanmusic.Fragments.HomeFragment;
import com.example.sanmusic.Fragments.MusicFragment;
import com.example.sanmusic.Fragments.PlaylistFragment;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.MusicService;
import com.example.sanmusic.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlideUpPanelActivity extends AppCompatActivity implements ActionPlay, ServiceConnection {

    TextView songName, artistName, totalTime, playTime, topSongName, bottomSongName,bottomArtistName;
    FloatingActionButton play_pause, bottomPlay_Pause;
    SeekBar seekBar;
    public int position = -1;
    protected int realPosition;
    public static List<MusicFiles> list_songs;
    public static List<MusicFiles> playlists = new ArrayList<>();
    ImageView shuffle,previous,next,repeat, menu,back,img_art,expand,bottomArt,bottomNext, close;
    public static ImageView main_like;
    public static Uri uri;
    private final Handler handler = new Handler();
    public Thread play_pause_thread, next_thread, prev_thread;
    MusicService musicService;
    public static final int REQUEST_CODE = 101;
    public static boolean shuffle_boolean;
    public static boolean repeat_boolean;
    FetchSongs fetchSongs = new FetchSongs();
    public static List<MusicFiles> musicFiles;
    public static List<MusicFiles> realSongs = new ArrayList<>();
    CardView BottomPlayer;
    RelativeLayout musicPlayerTop;
    ConstraintLayout MusicPlayerContainer;
    SlidingUpPanelLayout PanelLayout;
    LinearProgressIndicator bottomProgress;
    BottomAppBar bottomAppBar;
    EditText search;
    FavDB favDB;
    public static final String MY_SORT_SETTINGS = "SortOrder: ";
    public static final String MY_GRID_SIZE_SETTINGS = "GridSize: ";
    public static final String MY_GRID_STYLE_SETTINGS = "GridStyle: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For Transparent status View
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        favDB = new FavDB(this);

        setContentView(R.layout.activity_slide_up_panel);
        Permission();
        initViews();
        slidingPanel();
        getIntentValues();

//        expand = findViewById(R.id.expand_more);
//        expand.setOnClickListener(v->{
//            PopupMenu popupMenu = new PopupMenu(this,v);
//            popupMenu.setOnMenuItemClickListener(this);
//            popupMenu.inflate(R.menu.popup_menu);
//            popupMenu.show();
//        });

        //For Search a Songs
//        search = findViewById(R.id.search_bar);
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                List<MusicFiles> myFiles = new ArrayList<>();
//                for (MusicFiles song : musicFiles){
//                    if (song.getTitle().toLowerCase().contains(s.toString().toLowerCase())){
//                        myFiles.add(song);
//                    }
//                }
//                MusicFragment.adapter.updateList(myFiles);
//            }
//        });

//        main_like = findViewById(R.id.fav_btn);
//        main_like.setOnClickListener(v -> {
//            Toast.makeText(getApplicationContext(), "Liked", Toast.LENGTH_SHORT).show();
//        });

        menu.setOnClickListener(v->{
            Intent MenuIntent = new Intent(this,MenuListActivity.class);
            MenuIntent.putExtra("MenuPosition", realPosition);
            Toast.makeText(getApplicationContext(), "song position "+realPosition, Toast.LENGTH_SHORT).show();
            startActivity(MenuIntent);
        });

        back.setOnClickListener(v -> {
            PanelLayout.setPanelState(PanelState.COLLAPSED);
        });

        bottomPlay_Pause.setOnClickListener(v -> BottomPLayPause());
        bottomNext.setOnClickListener(v -> BottomNext());

        SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (is_Created && musicService != null){
                    set_progress(musicService.getCurrentPosition() / 1000);
                }
                handler.postDelayed(this,1000);
            }
        });

        //For change the seekbar Position every one sec
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    set_seek(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //For Shuffle Button
        shuffle.setOnClickListener(v->{
            if (shuffle_boolean){
                shuffle_boolean = false;
                shuffle.setImageResource(R.drawable.shuffle_off);
            } else {
                shuffle_boolean = true;
                shuffle.setImageResource(R.drawable.shuffle_on);
            }
        });

        //For Repeat Button
        repeat.setOnClickListener(v->{
            if (repeat_boolean){
                repeat_boolean = false;
                repeat.setImageResource(R.drawable.repeat_off);
            } else {
                repeat_boolean = true;
                repeat.setImageResource(R.drawable.repeat_on);
            }
        });
    }

    private void BottomNext() {
        if (is_Created && musicService != null) {
            musicService.nextBtnClicked();
            musicService.OnCompleted();
        }
    }

    private void BottomPLayPause() {
        if (is_Created && musicService != null) {
            if (musicService.is_Playing()){
                bottomPlay_Pause.setImageResource(R.drawable.play);
            } else {
                bottomPlay_Pause.setImageResource(R.drawable.pause);
            }
            musicService.pause_playBtnClicked();
        }
    }

    //Slide Up Panel
    private void slidingPanel() {
        if (PanelLayout.getPanelState() == PanelState.DRAGGING){
            BottomPlayer.setVisibility(View.VISIBLE);
        }
        PanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                BottomPlayer.setAlpha((int) (1F - slideOffset) * 255);
                bottomAppBar.setAlpha((int) (1F - slideOffset) * 255);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                if (newState != PanelState.EXPANDED){
                    BottomPlayer.setVisibility(View.VISIBLE);
                    bottomAppBar.setVisibility(View.VISIBLE);
                } else {
                    BottomPlayer.setVisibility(View.GONE);
                    bottomAppBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void set_progress(int currentPos){
        seekBar.setProgress(currentPos);
        bottomProgress.setProgress(currentPos);
        playTime.setText(formattedTime(currentPos));
    }

    private void set_seek(int progress){
        if (musicService != null) {
            musicService.seek_to(progress * 1000);
        }
    }
    private void initViews() {
        songName = findViewById(R.id.song_name);
        artistName = findViewById(R.id.artist_name);
        totalTime = findViewById(R.id.total_time);
        playTime = findViewById(R.id.played_time);
        play_pause = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekbar);
        shuffle = findViewById(R.id.shuffule_btn);
        previous = findViewById(R.id.previous_btn);
        next = findViewById(R.id.next_btn);
        repeat = findViewById(R.id.repeat_btn);
        img_art = findViewById(R.id.img_art);
//        close = findViewById(R.id.close);

        topSongName = findViewById(R.id.now_playing);
        topSongName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        topSongName.setSelected(true);

        back = findViewById(R.id.back_btn);

        menu = findViewById(R.id.menu_btn);

        MusicPlayerContainer = findViewById(R.id.music_player_container);

        bottomAppBar = findViewById(R.id.bottom_app_bar);

        PanelLayout = findViewById(R.id.sliding_layout);
        BottomPlayer = findViewById(R.id.bottom_action);
        musicPlayerTop = findViewById(R.id.top_music_player_layout);
        bottomProgress = findViewById(R.id.bottom_progress);
        bottomSongName = findViewById(R.id.bottom_player_song_name);
        bottomArtistName = findViewById(R.id.bottom_player_artist_name);
        bottomArt = findViewById(R.id.bottom_player_img);
        bottomNext = findViewById(R.id.bottom_player_nextbtn);
        bottomPlay_Pause = findViewById(R.id.bottom_player_pause_play_btn);
    }

    private void getIntentValues() {
        position = getIntent().getIntExtra("position",-1);

        String aSender = getIntent().getStringExtra("sender");
        String fSender = getIntent().getStringExtra("favorites");
        String mSender = getIntent().getStringExtra("songs");
        String adSender = getIntent().getStringExtra("albumDetails");
        boolean adSender1 = getIntent().getBooleanExtra("albumDetails", false);

        if (position != -1) {
            PanelLayout.setPanelState(PanelState.EXPANDED);
            MusicPlayerContainer.setVisibility(View.VISIBLE);
        }

        //Albums
        if ((aSender != null && aSender.equals("albumDetails") || (adSender != null && adSender.equals("AlbumDetails"))) || adSender1){
            if (adSender1){
                list_songs = album_list;
                realPosition = getIntent().getIntExtra("album_position", -1);
                shuffle_boolean = true;
            } else {
                list_songs = album_list;
                realPosition = getIntent().getIntExtra("album_position", -1);
            }
        }

        //Favorites
        else if (fSender != null && fSender.equals("Favorites")){
            list_songs = FavFiles;
            realPosition = getIntent().getIntExtra("fav_position", -1);
        }

        //Songs
        else if (mSender != null){
            if (mSender.equals("Songs")) {
                list_songs = files;
                realPosition = getIntent().getIntExtra("song_position", -1);
            } else {
                list_songs = searchedItems;
                realPosition = getIntent().getIntExtra("song_position", -1);
            }
        }

        if (list_songs != null){
            play_pause.setImageResource(R.drawable.pause);
            uri = Uri.parse(list_songs.get(position).getPath());
        }

        //For Music Services
        Intent Serviceintent = new Intent(SlideUpPanelActivity.this ,MusicService.class);
        Serviceintent.putExtra("servicePosition", position);
        this.startService(Serviceintent);
    }

    //For Resume Activity
    @Override
    protected void onResume() {
        super.onResume();
        if (shuffle_boolean){
            shuffle.setImageResource(R.drawable.shuffle_on);
        }
        Intent Bindintent = new Intent(SlideUpPanelActivity.this ,MusicService.class);
        bindService(Bindintent,this, Context.BIND_AUTO_CREATE);
        PlayPauseThread_btn();
        NextThread_btn();
        PrevThread_btn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    //pause & play button
    private void PlayPauseThread_btn() {
        play_pause_thread = new Thread(){
            @Override
            public void run() {
                super.run();
                play_pause.setOnClickListener(v-> PlayPauseThread_btnClicked());
            }
        };
        play_pause_thread.start();
    }

    public void PlayPauseThread_btnClicked() {
        if (musicService.is_Playing()){
            play_pause.setImageResource(R.drawable.play);
            musicService.pause();
            seekBar.setMax(musicService.getDuration() / 1000);
            bottomProgress.setMax(musicService.getDuration() / 1000);

            //For change seekbar position every one sec
            SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        bottomProgress.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        } else{
            play_pause.setImageResource(R.drawable.pause);
            musicService.start();
            seekBar.setMax(musicService.getDuration() / 1000);

            //For change seekbar position every one sec
            SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    //previous button
    private void PrevThread_btn() {
        prev_thread = new Thread(){
            @Override
            public void run() {
                super.run();
                previous.setOnClickListener(v -> PrevThread_btnClicked());
            }
        };
        prev_thread.start();
    }
    public void PrevThread_btnClicked() {
        if (musicService.is_Playing()){
            musicService.stop();
            musicService.release();

            //If user click shuffle button
            if (shuffle_boolean && !repeat_boolean){
                realPosition = position = getShuffle(list_songs.size() - 1);
            }
            //If user not click anything & Also this perform previous song
            else if (!shuffle_boolean && !repeat_boolean) {
                realPosition = position = (position - 1) < 0 ? (list_songs.size() - 1) : (position - 1);
            }

            uri = Uri.parse(list_songs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(list_songs.get(position).getTitle());
            artistName.setText(list_songs.get(position).getArtist());
            topSongName.setText(list_songs.get(position).getTitle());
            bottomSongName.setText(list_songs.get(position).getTitle());
            bottomArtistName.setText(list_songs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            bottomProgress.setMax(musicService.getDuration() / 1000);
            SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        bottomProgress.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.OnCompleted();
            play_pause.setBackgroundResource(R.drawable.pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();

            //If user click shuffle button
            if (shuffle_boolean && !repeat_boolean){
                realPosition = position = getShuffle(list_songs.size() - 1);
            }
            //If user not click anything & Also this perform previous song
             else if (!shuffle_boolean && !repeat_boolean) {
                realPosition = position = (position - 1) < 0 ? (list_songs.size() - 1) : (position - 1);
            }

            uri = Uri.parse(list_songs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(list_songs.get(position).getTitle());
            artistName.setText(list_songs.get(position).getArtist());
            topSongName.setText(list_songs.get(position).getTitle());
            bottomSongName.setText(list_songs.get(position).getTitle());
            bottomArtistName.setText(list_songs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            bottomProgress.setMax(musicService.getDuration() / 1000);
            SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        bottomProgress.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.OnCompleted();
            play_pause.setBackgroundResource(R.drawable.play);
        }
    }

    //next button
    private void NextThread_btn() {
        next_thread = new Thread(){
            @Override
            public void run() {
                super.run();
                next.setOnClickListener(v -> NextThread_btnClicked());
            }
        };
        next_thread.start();
    }
    public void NextThread_btnClicked() {
        if (musicService.is_Playing()){
            musicService.stop();
            musicService.release();

            //If user click shuffle button
            if (shuffle_boolean && !repeat_boolean){
                realPosition = position = getShuffle(list_songs.size() - 1);
            }
            //If user not click anything & Also this perform next song
            else if (!shuffle_boolean && !repeat_boolean) {
                realPosition = position = (position + 1) % list_songs.size();

            }

            uri = Uri.parse(list_songs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(list_songs.get(position).getTitle());
            artistName.setText(list_songs.get(position).getArtist());
            topSongName.setText(list_songs.get(position).getTitle());
            bottomSongName.setText(list_songs.get(position).getTitle());
            bottomArtistName.setText(list_songs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            bottomProgress.setMax(musicService.getDuration() / 1000);
            SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        bottomProgress.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.OnCompleted();
            play_pause.setBackgroundResource(R.drawable.pause);
            musicService.start();
        } else {
            musicService.stop();
            musicService.release();

            //If user click shuffle button
            if (shuffle_boolean && !repeat_boolean){
                realPosition = position = getShuffle(list_songs.size() - 1);
            }
            //If user not click anything & Also this perform next song
            else if (!shuffle_boolean && !repeat_boolean) {
                realPosition = position = (position + 1) % list_songs.size();
            }

            uri = Uri.parse(list_songs.get(position).getPath());
            musicService.createMediaPlayer(position);
            metaData(uri);
            songName.setText(list_songs.get(position).getTitle());
            artistName.setText(list_songs.get(position).getArtist());
            topSongName.setText(list_songs.get(position).getTitle());
            bottomSongName.setText(list_songs.get(position).getTitle());
            bottomArtistName.setText(list_songs.get(position).getArtist());

            seekBar.setMax(musicService.getDuration() / 1000);
            bottomProgress.setMax(musicService.getDuration() / 1000);
            SlideUpPanelActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (musicService != null){
                        int currentPosition = musicService.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPosition);
                        bottomProgress.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            musicService.OnCompleted();
            play_pause.setBackgroundResource(R.drawable.play);
        }
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();

        int TotTime = Integer.parseInt(list_songs.get(position).getDuration()) / 1000;
        totalTime.setText(formattedTime(TotTime));

        Bitmap bitmap;
        if (art != null){
            bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);

            ImageViewAnimation(this,img_art,bottomArt,bitmap);

            //It's API,User for match the dominate Color into gradient
            Palette.from(bitmap).generate(palette -> {
                Palette.Swatch swatch = palette.getDominantSwatch();
                if(swatch != null){
                    ImageView gradient = findViewById(R.id.gradient);
                    RelativeLayout relativeLayout = findViewById(R.id.music_player_relative);
                    gradient.setBackgroundResource(R.drawable.main_bg);
                    relativeLayout.setBackgroundResource(R.drawable.without_gradient_main_bg);
                    GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),0x00000000});
                    gradient.setBackground(drawable1);
                    GradientDrawable drawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
                    relativeLayout.setBackground(drawable2);

                    songName.setTextColor(swatch.getTitleTextColor());
                    topSongName.setTextColor(swatch.getTitleTextColor());
                    artistName.setTextColor(swatch.getBodyTextColor());
                    totalTime.setTextColor(swatch.getBodyTextColor());
                    playTime.setTextColor(swatch.getBodyTextColor());

//                    main_like.setImageTintList(ColorStateList.valueOf(swatch.getTitleTextColor()));
                } else {
                    ImageView gradient = findViewById(R.id.gradient);
                    RelativeLayout relativeLayout = findViewById(R.id.music_player_relative);
                    gradient.setBackgroundResource(R.drawable.bottom_gradient_bg);
                    relativeLayout.setBackgroundResource(R.drawable.without_gradient_main_bg);
                    GradientDrawable drawable1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0x00000000});
                    gradient.setBackground(drawable1);
                    GradientDrawable drawable2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0xff000000});
                    relativeLayout.setBackground(drawable2);

                    songName.setTextColor(Color.WHITE);
                    topSongName.setTextColor(Color.WHITE);
                    artistName.setTextColor(Color.WHITE);
                    totalTime.setTextColor(Color.WHITE);
                    playTime.setTextColor(Color.WHITE);

//                    main_like.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
                }
            });
        } else {
            //If any album art not there put on the default Image
            Glide.with(getApplicationContext()).asBitmap().load(R.drawable.musical_notes_04).into(img_art);
            Glide.with(getApplicationContext()).asBitmap().load(R.drawable.musical_notes_04).into(bottomArt);

            ImageView gradient = findViewById(R.id.gradient);
            RelativeLayout relativeLayout = findViewById(R.id.music_player_relative);
            gradient.setBackgroundResource(R.drawable.main_bg);
            relativeLayout.setBackgroundResource(R.drawable.without_gradient_main_bg);

            songName.setTextColor(Color.WHITE);
            artistName.setTextColor(Color.WHITE);
            topSongName.setTextColor(Color.WHITE);
            totalTime.setTextColor(Color.WHITE);
            playTime.setTextColor(Color.WHITE);

//            main_like.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        }
    }

    private int getShuffle(int i) {
        Random ran = new Random();
        return ran.nextInt(i + 1);
    }

    public void updateList(List<MusicFiles> updatedList){
        list_songs = new ArrayList<>(updatedList);
    }

    //For Animate the Images, After click next or prev Buttons
    private void ImageViewAnimation(Context context, ImageView imageView, ImageView imageView1, Bitmap bitmap){
        Animation animOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        Glide.with(context).load(bitmap).into(imageView1);

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Glide.with(context).asBitmap().load(bitmap).into(imageView);

                animIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animOut);
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        musicService.setCallBack(this);

        if (is_Created && musicService != null){
            seekBar.setMax(musicService.getDuration() / 1000);
            bottomProgress.setMax(musicService.getDuration() / 1000);
            metaData(uri);

            songName.setText(list_songs.get(position).getTitle());
            artistName.setText(list_songs.get(position).getArtist());
            topSongName.setText(list_songs.get(position).getTitle());
            bottomSongName.setText(list_songs.get(position).getTitle());
            bottomArtistName.setText(list_songs.get(position).getArtist());

            musicService.OnCompleted();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
    }

    private void Permission() {
        if (ContextCompat.checkSelfPermission(SlideUpPanelActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SlideUpPanelActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            //Permission Granted
            realSongs = fetchSongs.getSongs(this);
            initViewPager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                realSongs = fetchSongs.getSongs(this);
                initViewPager();
            } else {
                ActivityCompat.requestPermissions(SlideUpPanelActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }
    }

    //Initial the viewpager
    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
//    private void initViewPager() {
//        List<Fragment> fragments = new ArrayList<>();
//        ViewPager2 viewPager = findViewById(R.id.view_pager);
////        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
//
//        fragments.add(new HomeFragment());
//        fragments.add(new MusicFragment());
//        fragments.add(new AlbumFragment());
//        fragments.add(new FavoritesFragment());
//        fragments.add(new PlaylistFragment());
//        SlideUpPanelActivity.ViewPagerAdapter viewPagerAdapter = new SlideUpPanelActivity.ViewPagerAdapter(this, fragments);
//        viewPagerAdapter.notifyDataSetChanged();
//        viewPager.setAdapter(viewPagerAdapter);
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                switch (position){
//                    case 0:
//                        bottomNavigationView.setSelectedItemId(R.id.home);
//                        break;
//                    case 1:
//                        bottomNavigationView.setSelectedItemId(R.id.library);
//                        break;
//                    case 2:
//                        bottomNavigationView.setSelectedItemId(R.id.album);
//                        break;
//                    case 3:
//                        bottomNavigationView.setSelectedItemId(R.id.fav);
//                        break;
//                    case 4:
//                        bottomNavigationView.setSelectedItemId(R.id.playlist);
//                        break;
//                }
//            }
//        });
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            if (itemId == R.id.home)
//                viewPager.setCurrentItem(0);
//            else if (itemId == R.id.library)
//                viewPager.setCurrentItem(1);
//            else if (itemId == R.id.album)
//                viewPager.setCurrentItem(2);
//            else if (itemId == R.id.fav)
//                viewPager.setCurrentItem(3);
//            else if (itemId == R.id.playlist)
//                viewPager.setCurrentItem(4);
//
//            return true;
//        });
//    }

    private void initViewPager(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home)
                replaceFragment(new HomeFragment());
            else if (itemId == R.id.library)
                replaceFragment(new MusicFragment());
            else if (itemId == R.id.album)
                replaceFragment(new AlbumFragment());
            else if (itemId == R.id.fav)
                replaceFragment(new FavoritesFragment());
            else if (itemId == R.id.playlist)
                replaceFragment(new PlaylistFragment());

            return true;
        });
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    //For Sort the Songs List
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        SharedPreferences.Editor editor = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE).edit();
//        if (item.getItemId() == R.id.byName) {
//            editor.putString("sorting","ByName");
//            editor.apply();
//            this.recreate();
//            return true;
//        } else if (item.getItemId() == R.id.byDate) {
//            editor.putString("sorting","ByDate");
//            editor.apply();
//            this.recreate();
//            return true;
//        }  else if (item.getItemId() == R.id.bySize) {
//            editor.putString("sorting","BySize");
//            editor.apply();
//            this.recreate();
//            return true;
//        }
//        return false;
//    }

    //For add a Fragments into ViewPager
//    public static class ViewPagerAdapter extends FragmentStateAdapter {
//
//        private final List<Fragment> fragments;
//
//        public ViewPagerAdapter(@NonNull FragmentActivity fm, List<Fragment> fragments) {
//            super(fm);
//            this.fragments = fragments;
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            return fragments.get(position);
//        }
//
//        @Override
//        public int getItemCount() {
//            return fragments.size();
//        }
//    }

    //Fetch the musics
//    public List<MusicFiles> getFiles(Context context){
//        int index = 0;
//        albums.clear();
//        List<MusicFiles> FetchedFiles = new ArrayList<>();
//        List<String> DuplicateFilesInAlbum = new ArrayList<>();
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        switch (order) {
//            case "asc":
//                SortOrder = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
//                break;
//            case "desc":
//                SortOrder = MediaStore.MediaColumns.DISPLAY_NAME + " DESC";
//                break;
//            case "dateAdded":
//                SortOrder = MediaStore.MediaColumns.DATE_ADDED + " ASC";
//                break;
//            case "dateModified":
//                SortOrder = MediaStore.MediaColumns.DATE_MODIFIED + " ASC";
//                break;
//            case "artist":
//                SortOrder = MediaStore.MediaColumns.ARTIST + " ASC";
//                break;
//        }
//        String[] projection = {
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media._ID
//        };
//        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,SortOrder);
//
//        if (cursor != null){
//            while (cursor.moveToNext()){
//                String album = cursor.getString(0);
//                String path = cursor.getString(1);
//                String artist = cursor.getString(2);
//                String duration = cursor.getString(3);
//                String title = cursor.getString(4);
//                String id = cursor.getString(5);
//
//                MusicFiles musicFiles = new MusicFiles(path, title, album, artist, duration, id, index++);
//                FetchedFiles.add(musicFiles);
//
//                if (!DuplicateFilesInAlbum.contains(album)){
//                    albums.add(musicFiles);
//                    DuplicateFilesInAlbum.add(album);
//                }
//            }
//            cursor.close();
//        }
//        return FetchedFiles;
//    }
}