package com.example.sanmusic;

public class MusicFiles {
    private String path;
    private String title;
    private String album;
    private String artist;
    private String duration;
    private String id;
    private String albumName;
    private String albumArtist;
    private String no_songs;
    private String albumArt;
    private String dateAdded;
    private String dateModified;
    private String artistName;
    private String year;
    private String playlistTitle;
    private String fStatus = "0";
    private int FavPosition = 0;
    private int position_fav = 0;
    private int indexSong;

    public MusicFiles(String path, String title, String album, String artist, String duration, String id, int indexSong, String dateAdded, String dateModified, String artistName) {
        this.path = path;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.id = id;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.artistName = artistName;
        this.indexSong = indexSong;
    }
    public MusicFiles(String path, String title, String duration, String artist, int position){
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.artist = artist;
        this.FavPosition = position;
    }
    public MusicFiles(String albumName, String albumArtist, String no_songs, String albumArt, String year){
        this.albumName = albumName;
        this.albumArtist = albumArtist;
        this.no_songs = no_songs;
        this.albumArt = albumArt;
    }
    public MusicFiles(String playlistTitle){
        this.playlistTitle = playlistTitle;
    }

    public MusicFiles(int position_fav){
        this.position_fav = position_fav;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public int getPosition_fav() {
        return position_fav;
    }

    public void setPosition_fav(int position_fav) {
        this.position_fav = position_fav;
    }

    public String getNo_songs() {
        return no_songs;
    }

    public void setNo_songs(String no_songs) {
        this.no_songs = no_songs;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public int getIndexSong() {
        return indexSong;
    }

    public void setIndexSong(int indexSong) {
        this.indexSong = indexSong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getfStatus() {
        return fStatus;
    }

    public void setfStatus(String fStatus) {
        this.fStatus = fStatus;
    }

    public int getFavPosition() {
        return FavPosition;
    }

    public void setFavPosition(int position) {
        this.FavPosition = position;
    }
}
