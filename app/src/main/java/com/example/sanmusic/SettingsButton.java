package com.example.sanmusic;

import android.content.Context;

import com.example.sanmusic.Fragments.MusicFragment;

import java.util.List;

public class SettingsButton extends MusicFragment {
    Context context;
    public List<MusicFiles> editedList;
    FetchSongs fetchSongs = new FetchSongs();
    public SettingsButton(Context context) {
        this.context = context;
        editedList = fetchSongs.getSongs(context);
    }

    public List<MusicFiles> getSongsList(){

        return editedList;
    }

}
