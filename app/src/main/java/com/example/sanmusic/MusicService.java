package com.example.sanmusic;

import static com.example.sanmusic.Activities.SlideUpPanelActivity.list_songs;
import static com.example.sanmusic.AdapterClasses.MusicAdapter.files;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    IBinder myBinder = new MyBinder();
    MediaPlayer mediaPlayer;
    public List<MusicFiles> musicFilesList = new ArrayList<>();
    public int position = -1, real_position = -1;
    ActionPlay actionPlay;
    public static boolean is_Created = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class MyBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition = intent.getIntExtra("servicePosition", -1);
        if (myPosition != -1) {
            playMedia(myPosition);
        }
        return START_STICKY;
    }

    private void playMedia(int StartPosition) {

        musicFilesList = list_songs;

        position = StartPosition;

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            if (musicFilesList != null){
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        } else {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }
    public void createMediaPlayer(int positionInner){
        position = positionInner;
        Uri uriPath = Uri.parse(musicFilesList.get(position).getPath());
        mediaPlayer = MediaPlayer.create(getBaseContext(),uriPath);
        is_Created = true;
    }
    public void OnCompleted(){
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (actionPlay != null){
            actionPlay.NextThread_btnClicked();
        }
        createMediaPlayer(position);
        mediaPlayer.start();
        OnCompleted();
    }

    //For control the Media Player in their properties
    public void start(){
        mediaPlayer.start();
    }
    public void stop(){
        mediaPlayer.stop();
    }
    public boolean is_Playing(){
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void release(){
        mediaPlayer.release();
    }
    public int getDuration(){
        return mediaPlayer.getDuration();
    }
    public void seek_to(int position){
        mediaPlayer.seekTo(position);
    }
    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void nextBtnClicked(){
        if (actionPlay != null){
            actionPlay.NextThread_btnClicked();}
    }
    public void pause_playBtnClicked(){
        if (actionPlay != null){
            actionPlay.PlayPauseThread_btnClicked();}
    }
    public void setCallBack(ActionPlay actionPlay){
        this.actionPlay = actionPlay;
    }

}
