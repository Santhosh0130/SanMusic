package com.example.sanmusic;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class FetchSongs {
    public List<MusicFiles> getSongs(Context context){
        int index = 0;
        List<MusicFiles> FetchedFiles = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATE_MODIFIED,
                MediaStore.MediaColumns.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null, null);

        if (cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String path = cursor.getString(1);
                String artist = cursor.getString(2);
                String duration = cursor.getString(3);
                String title = cursor.getString(4);
                String id = cursor.getString(5);
                String dateAdded = cursor.getString(6);
                String dateModified = cursor.getString(7);
                String artistName = cursor.getString(8);

                MusicFiles musicFiles = new MusicFiles(path, title, album, artist, duration, id, index++, dateAdded, dateModified, artistName);
                FetchedFiles.add(musicFiles);
            }
            cursor.close();
        }
        return FetchedFiles;
    }

    public List<MusicFiles> getAlbums(Context context) {
        List<MusicFiles> FetchedFiles = new ArrayList<>();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.LAST_YEAR
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String albumName = cursor.getString(0);
                String albumArtist = cursor.getString(1);
                String no_songs = cursor.getString(2);
                String albumArt = cursor.getString(3);
                String year = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(albumName, albumArtist, no_songs, albumArt, year);
                FetchedFiles.add(musicFiles);
            }
            cursor.close();
        }
        return FetchedFiles;
    }
}
