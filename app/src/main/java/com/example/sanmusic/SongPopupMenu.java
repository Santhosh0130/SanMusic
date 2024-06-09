package com.example.sanmusic;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanmusic.Activities.SlideUpPanelActivity;
import com.example.sanmusic.AdapterClasses.AddFavAdapter;
import com.example.sanmusic.AdapterClasses.AlbumAdapter;
import com.example.sanmusic.AdapterClasses.MusicAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongPopupMenu extends SlideUpPanelActivity {
    Context context;
    View view;
    AlbumAdapter albumAdapter;
    MusicAdapter musicAdapter;
    AddFavAdapter favAdapter;
    List<MusicFiles> sortedList;
    public static final String MY_SORT_SETTINGS = "SortOrder: ";
    public static final String MY_GRID_SIZE_SETTINGS = "GridSize: ";
    public static final String MY_GRID_STYLE_SETTINGS = "GridStyle: ";
    TextView name, album, artist, duration, path;
    RecyclerView addRecyclerview;
    int songPosition = -1;

    public SongPopupMenu(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void createSongPopupMenu(int songPosition){
        this.songPosition = songPosition;
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.song_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::getSongPopupMenu);
        popupMenu.show();
    }

    private boolean getSongPopupMenu(MenuItem item) {
        if (item.getItemId() == R.id.play_next_item) {
            Toast.makeText(context, "Play next "+songPosition, Toast.LENGTH_SHORT).show();
            if (list_songs != null){
//                List<MusicFiles> newList = new ArrayList<>(list_songs);
                list_songs.add(songPosition , list_songs.get(songPosition));
//                updateList(newList);
                return true;
            }
        } else if (item.getItemId() == R.id.add_to_playlist_item) {
            Toast.makeText(context, "Added to playlist", Toast.LENGTH_SHORT).show();
            Dialog addPlaylist = new Dialog(context);
            addPlaylist.setContentView(R.layout.add_playlist_dialog);
            addPlaylist.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addPlaylist.setCanceledOnTouchOutside(true);
            addRecyclerview = addPlaylist.findViewById(R.id.add_recycleview);
            addRecyclerview.setHasFixedSize(true);
            addRecyclerview.setLayoutManager(new LinearLayoutManager(context));
            addRecyclerview.setAdapter(new AddPlaylist(context, playlists));
            addPlaylist.show();
        }
        else if (item.getItemId() == R.id.add_to_queue_item) {
            Toast.makeText(context, "Add to playing queue", Toast.LENGTH_SHORT).show();
            if (list_songs != null){
                list_songs.add(list_songs.get(songPosition));
            }
            return true;
        }
        else if (item.getItemId() == R.id.details_item) {
            Toast.makeText(context, "Details", Toast.LENGTH_SHORT).show();
            Dialog detailsDialog = new Dialog(context);
            detailsDialog.setContentView(R.layout.details_dialog);
            detailsDialog.setCanceledOnTouchOutside(true);
            detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            name = detailsDialog.findViewById(R.id.det_song_name);
            name.setText(realSongs.get(songPosition).getTitle());
            album = detailsDialog.findViewById(R.id.det_album_name);
            album.setText(realSongs.get(songPosition).getAlbum());
            artist = detailsDialog.findViewById(R.id.det_artist_name);
            artist.setText(realSongs.get(songPosition).getArtist());
            duration = detailsDialog.findViewById(R.id.det_duration);
            duration.setText(formattedTime(Integer.parseInt(realSongs.get(songPosition).getDuration()) / 1000));
            path = detailsDialog.findViewById(R.id.det_path);
            path.setText(realSongs.get(songPosition).getPath());
            detailsDialog.findViewById(R.id.det_ok_btn).setOnClickListener(v -> detailsDialog.dismiss());
            detailsDialog.show();
            return true;
        }
        else if (item.getItemId() == R.id.delete_item) {
            Toast.makeText(context, "Delete from device", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
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


    public void createSortPopupMenu(int val, List<MusicFiles> musicFilesList, MusicAdapter mAdapter, AlbumAdapter aAdapter, AddFavAdapter fAdapter){
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        if (val == 1 && mAdapter != null) {
            sortedList = musicFilesList;
            musicAdapter = mAdapter;
            menuInflater.inflate(R.menu.songs_more_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getSongSortPopupMenu);
        } else if (val == 2 && aAdapter != null){
            sortedList = musicFilesList;
            albumAdapter = aAdapter;
            menuInflater.inflate(R.menu.album_more_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getAlbumSortPopupMenu);
        } else if (val == 3 && fAdapter != null) {
            sortedList = musicFilesList;
            favAdapter = fAdapter;
            menuInflater.inflate(R.menu.fav_more_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getFavSortPopupMenu);
        }
        popupMenu.show();
    }

    private boolean getSongSortPopupMenu(MenuItem item) {
        if (getSort(item, 1)) {
            return getSort(item, 1);
        } else if (getGridSize(item, 1)) {
            return getGridSize(item, 1);
        } else if (getGridStyle(item, 1)) {
            return getGridStyle(item, 1);
        } else if (getSettings(item)) {
            getSettings(item);
        }

        return false;
    }

    private boolean getAlbumSortPopupMenu(MenuItem item) {
        if (getSort(item, 2)) {
            return getSort(item, 2);
        } else if (getGridSize(item, 2)) {
            return getGridSize(item, 2);
        } else if (getGridStyle(item, 2)) {
            return getGridStyle(item, 2);
        } else if (getSettings(item)) {
            getSettings(item);
        }

        return false;
    }

    private boolean getFavSortPopupMenu(MenuItem item) {
        if (getSort(item, 3)) {
            return getSort(item, 3);
        } else if (getGridSize(item, 3)) {
            return getGridSize(item, 3);
        } else if (getGridStyle(item, 3)) {
            return getGridStyle(item, 3);
        } else if (getSettings(item)) {
            getSettings(item);
        }

        return false;
    }

    private void sortList(List<MusicFiles> list, String name){
        Collections.sort(list, new Comparator<MusicFiles>() {
            @Override
            public int compare(MusicFiles o1, MusicFiles o2) {
                switch (name) {
                    case "TitleASC":
                        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    case "TitleDESC":
                        return o2.getTitle().compareToIgnoreCase(o1.getTitle());
                    case "Artist":
                        return o1.getArtistName().compareToIgnoreCase(o2.getArtistName());
                    case "DateAdd":
                        return o1.getDateAdded().compareToIgnoreCase(o2.getDateAdded());
                    case "DateMod":
                        return o1.getDateModified().compareToIgnoreCase(o2.getDateModified());
                    case "AlbumASC":
                        return o1.getAlbumName().compareToIgnoreCase(o2.getAlbumName());
                    case "AlbumDESC":
                        return o2.getAlbumName().compareToIgnoreCase(o1.getAlbumName());
                    case "AlbumArtist":
                        return o1.getAlbumArtist().compareToIgnoreCase(o2.getAlbumArtist());
                    case "SongCount":
                        return o1.getNo_songs().compareToIgnoreCase(o2.getNo_songs());
                    case "Year":
                        return o1.getYear().compareToIgnoreCase(o2.getYear());
                }

                return 0;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private boolean getSort(MenuItem item, int val) {
        if (val == 1){
            //Song Sorting
            SharedPreferences.Editor songSortEditor = context.getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.ascending_item) {
                Toast.makeText(context, "Ascending", Toast.LENGTH_SHORT).show();
                songSortEditor.putString("songSorting", "asc").apply();
                sortList(sortedList, "TitleASC");
                musicAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.descending_item) {
                Toast.makeText(context, "Descending", Toast.LENGTH_SHORT).show();
                songSortEditor.putString("songSorting", "desc").apply();
                sortList(sortedList, "TitleDESC");
                musicAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.artist_item) {
                Toast.makeText(context, "Artist", Toast.LENGTH_SHORT).show();
                songSortEditor.putString("songSorting", "artist").apply();
                sortList(sortedList, "Artist");
                musicAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.date_added_item) {
                Toast.makeText(context, "Date Added", Toast.LENGTH_SHORT).show();
                songSortEditor.putString("songSorting", "dateAdded").apply();
                sortList(sortedList, "DateAdd");
                musicAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.date_modified_item) {
                Toast.makeText(context, "Date Modified", Toast.LENGTH_SHORT).show();
                songSortEditor.putString("songSorting", "dateModified").apply();
                sortList(sortedList, "DateMod");
                musicAdapter.notifyDataSetChanged();
                return true;
            }
        } else if (val == 2){
            //Album Sorting
            SharedPreferences.Editor AlbumSortEditor = context.getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.ascending_item2) {
                Toast.makeText(context, "Ascending", Toast.LENGTH_SHORT).show();
                AlbumSortEditor.putString("albumSorting", "asc").apply();
                sortList(sortedList, "AlbumASC");
                albumAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.descending_item2) {
                Toast.makeText(context, "Descending", Toast.LENGTH_SHORT).show();
                AlbumSortEditor.putString("albumSorting", "desc").apply();
                sortList(sortedList, "AlbumDESC");
                albumAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.artist_item2) {
                Toast.makeText(context, "Album Artist", Toast.LENGTH_SHORT).show();
                AlbumSortEditor.putString("albumSorting", "artist").apply();
                sortList(sortedList, "AlbumArtist");
                albumAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.song_count_item2) {
                Toast.makeText(context, "Song Count", Toast.LENGTH_SHORT).show();
                AlbumSortEditor.putString("albumSorting", "no_songs").apply();
                sortList(sortedList, "SongCount");
                albumAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.year_item) {
                Toast.makeText(context, "Year", Toast.LENGTH_SHORT).show();
                AlbumSortEditor.putString("albumSorting", "year").apply();
                sortList(sortedList, "year");
                albumAdapter.notifyDataSetChanged();
                return true;
            }
        } else if (val == 3) {
            //Favorites Sort
            SharedPreferences.Editor FavSortEditor = context.getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.ascending_item3){
                FavSortEditor.putString("favSorting", "asc").apply();
                sortList(sortedList, "FavASC");
                favAdapter.notifyDataSetChanged();
                return true;
            }
            else if (item.getItemId() == R.id.descending_item3){
                FavSortEditor.putString("favSorting", "desc").apply();
                sortList(sortedList, "FavDESC");
                favAdapter.notifyDataSetChanged();
                return true;
            }
        }

        return false;
    }

    private boolean getSettings(MenuItem item) {
        //Settings
        if (item.getItemId() == R.id.settings_item) {
            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    private boolean getGridStyle(MenuItem item, int val) {
        if (val == 1) {
            //Song Grid Style
            SharedPreferences.Editor songGridStyleEditor = context.getSharedPreferences(MY_GRID_STYLE_SETTINGS, Context.MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.list_item) {
                Toast.makeText(context, "List", Toast.LENGTH_SHORT).show();
                songGridStyleEditor.putString("grid_style", "list").apply();
                musicAdapter.notifyDataSetChanged();
                return true;
            } else if (item.getItemId() == R.id.card_item) {
                Toast.makeText(context, "Card", Toast.LENGTH_SHORT).show();
                songGridStyleEditor.putString("grid_style", "card").apply();
                musicAdapter.notifyDataSetChanged();
                return true;
            }
        } else if (val == 2){
            //Album Grid Style
            SharedPreferences.Editor albumGridStyleEditor = context.getSharedPreferences(MY_GRID_STYLE_SETTINGS, Context.MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.list_item2) {
                Toast.makeText(context, "List", Toast.LENGTH_SHORT).show();
                albumGridStyleEditor.putString("grid_style", "list").apply();
                albumAdapter.notifyDataSetChanged();
                return true;
            } else if (item.getItemId() == R.id.card_item2) {
                Toast.makeText(context, "Card", Toast.LENGTH_SHORT).show();
                albumGridStyleEditor.putString("grid_style", "card").apply();
                albumAdapter.notifyDataSetChanged();
                return true;
            }
        } else if (val == 3) {
            //Favorites Grid Style
            SharedPreferences.Editor favGridStyleEditor = context.getSharedPreferences(MY_GRID_STYLE_SETTINGS, Context.MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.list_item3) {
                Toast.makeText(context, "List", Toast.LENGTH_SHORT).show();
                favGridStyleEditor.putString("grid_style", "list").apply();
            } else if (item.getItemId() == R.id.card_item3) {
                Toast.makeText(context, "Card", Toast.LENGTH_SHORT).show();
                favGridStyleEditor.putString("grid_style", "card").apply();
                return true;
            }
        }

        return false;
    }

    private boolean getGridSize(MenuItem item, int val) {
        if (val == 1) {
            //Song Grid Size
            SharedPreferences.Editor songGridSizeEditor = context.getSharedPreferences(MY_GRID_SIZE_SETTINGS, Context.MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.one_item) {
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                songGridSizeEditor.putString("grid_size", "one");
                songGridSizeEditor.apply();
                finish();
                return true;
            } else if (item.getItemId() == R.id.two_item) {
                Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                songGridSizeEditor.putString("grid_size", "two");
                songGridSizeEditor.apply();
                finish();
                return true;
            } else if (item.getItemId() == R.id.three_item) {
                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                songGridSizeEditor.putString("grid_size", "three");
                songGridSizeEditor.apply();
                finish();
                return true;
            } else if (item.getItemId() == R.id.four_item) {
                Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                songGridSizeEditor.putString("grid_size", "four");
                songGridSizeEditor.apply();
                finish();
                return true;
            }
        }else if (val == 2){
                //Album Grid Size
                SharedPreferences.Editor albumGridSizeEditor = context.getSharedPreferences(MY_GRID_SIZE_SETTINGS, Context.MODE_PRIVATE).edit();
                if (item.getItemId() == R.id.one_item2) {
                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                    albumGridSizeEditor.putString("grid_size", "one");
                    albumGridSizeEditor.apply();
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.two_item2) {
                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                    albumGridSizeEditor.putString("grid_size", "two");
                    albumGridSizeEditor.apply();
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.three_item2) {
                    Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    albumGridSizeEditor.putString("grid_size", "three");
                    albumGridSizeEditor.apply();
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.four_item2) {
                    Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                    albumGridSizeEditor.putString("grid_size", "four");
                    albumGridSizeEditor.apply();
                    finish();
                    return true;
                }
        } else if (val == 3) {
            //Favorites Grid Size
            SharedPreferences.Editor favGridSizeEditor = context.getSharedPreferences(MY_GRID_SIZE_SETTINGS, Context.MODE_PRIVATE).edit();
            if (item.getItemId() == R.id.one_item3) {
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
                favGridSizeEditor.putString("grid_size", "one");
                favGridSizeEditor.apply();
                finish();
                return true;
            } else if (item.getItemId() == R.id.two_item3) {
                Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
                favGridSizeEditor.putString("grid_size", "two");
                favGridSizeEditor.apply();
                finish();
                return true;
            } else if (item.getItemId() == R.id.three_item3) {
                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                favGridSizeEditor.putString("grid_size", "three");
                favGridSizeEditor.apply();
                finish();
                return true;
            } else if (item.getItemId() == R.id.four_item3) {
                Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                favGridSizeEditor.putString("grid_size", "four");
                favGridSizeEditor.apply();
                finish();
                return true;
            }
        }

        return false;
    }

    private static class AddPlaylist extends RecyclerView.Adapter<AddPlaylist.view_holder>{

        Context context;
        List<MusicFiles> playlistLists;

        public AddPlaylist(Context context, List<MusicFiles> playlistLists) {
            this.context = context;
            this.playlistLists = playlistLists;
        }

        @NonNull
        @Override
        public AddPlaylist.view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new view_holder(LayoutInflater.from(context).inflate(R.layout.add_playlist_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AddPlaylist.view_holder holder, int position) {
            holder.title.setText(playlistLists.get(position).getPlaylistTitle());
            holder.itemView.setOnClickListener(v ->{
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return playlistLists.size();
        }

        public static class view_holder extends RecyclerView.ViewHolder {
            TextView title;
            public view_holder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.add_playlist_title);
            }
        }
    }
}
