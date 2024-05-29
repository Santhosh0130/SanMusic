package com.example.sanmusic.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sanmusic.SongPopupMenu.MY_GRID_SIZE_SETTINGS;
import static com.example.sanmusic.SongPopupMenu.MY_GRID_STYLE_SETTINGS;
import static com.example.sanmusic.SongPopupMenu.MY_SORT_SETTINGS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanmusic.AdapterClasses.AlbumAdapter;
import com.example.sanmusic.FetchSongs;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.util.Collections;
import java.util.List;

public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    AlbumAdapter adapter;
    ImageView settings;
    String order = "asc";
    String Style = "card";
    String Size = "2";
    List<MusicFiles> sortedList;

    public AlbumFragment(){
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.albums_recyclerview);
        recyclerView.setHasFixedSize(true);
        settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.album_more_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getPopup);
            checkMenu(popupMenu.getMenu());
            popupMenu.show();
        });

        order = requireActivity().getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).getString("albumSorting","asc");

        Style = requireActivity().getSharedPreferences(MY_GRID_STYLE_SETTINGS, MODE_PRIVATE).getString("album_grid_style", "card");

        Size = requireActivity().getSharedPreferences(MY_GRID_SIZE_SETTINGS, MODE_PRIVATE).getString("album_grid_size", "2");

        sortedList = new FetchSongs().getAlbums(requireContext());

        Collections.sort(sortedList, (o1, o2) -> getSort(order, o1, o2));

        //For show the Albums
        if (Style.equals("list")) {
            adapter = new AlbumAdapter(sortedList, requireContext(), R.layout.list_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        } else {
            switch (Size) {
                case "1":
                    adapter = new AlbumAdapter(sortedList, getContext(), R.layout.list_items);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                    break;
                case "2":
                    adapter = new AlbumAdapter(sortedList, getContext(), R.layout.card_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                    break;
                case "3":
                    adapter = new AlbumAdapter(sortedList, getContext(), R.layout.card_item_3);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
                    break;
                case "4":
                    adapter = new AlbumAdapter(sortedList, getContext(), R.layout.card_items_4);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 4));
                    break;
                default:
                    adapter = new AlbumAdapter(sortedList, getContext(), R.layout.card_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                    break;
            }
        }
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void checkMenu(Menu menu) {
        switch (order){
            case "asc":
                menu.findItem(R.id.ascending_item2).setChecked(true);
                break;
            case "desc":
                menu.findItem(R.id.descending_item2).setChecked(true);
                break;
            case "artist":
                menu.findItem(R.id.artist_item2).setChecked(true);
                break;
            case "no_songs":
                menu.findItem(R.id.song_count_item2).setChecked(true);
                break;
            case "year":
                menu.findItem(R.id.year_item).setChecked(true);
                break;
            default:
                menu.findItem(R.id.ascending_item2).setChecked(true);
                break;
        }

        if (Style.equals("list")){
            menu.findItem(R.id.list_item2).setChecked(true);
        } else {
            menu.findItem(R.id.card_item2).setChecked(true);
        }

        switch (Size) {
            case "1":
                menu.findItem(R.id.one_item2).setChecked(true);
                break;
            case "2":
                menu.findItem(R.id.two_item2).setChecked(true);
                break;
            case "3":
                menu.findItem(R.id.three_item2).setChecked(true);
                break;
            case "4":
                menu.findItem(R.id.four_item2).setChecked(true);
                break;
            default:
                menu.findItem(R.id.two_item2).setChecked(true);
                break;
        }
    }

    private boolean sort(MenuItem item, String type){
        SharedPreferences.Editor AlbumSortEditor = requireActivity().getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        AlbumSortEditor.putString("albumSorting", type).apply();
        item.setChecked(true);
        sortList(sortedList);
        refreshFragment();
        return true;
    }
    private boolean style(MenuItem item, String type){
        SharedPreferences.Editor albumGridStyleEditor = requireActivity().getSharedPreferences(MY_GRID_STYLE_SETTINGS, Context.MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        albumGridStyleEditor.putString("album_grid_style", type).apply();
        item.setChecked(true);
        refreshFragment();
        return true;
    }
    private boolean size(MenuItem item, String type){
        SharedPreferences.Editor albumGridSizeEditor = requireActivity().getSharedPreferences(MY_GRID_SIZE_SETTINGS, Context.MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        albumGridSizeEditor.putString("album_grid_size", type).apply();
        item.setChecked(true);
        refreshFragment();
        return true;
    }

    private int getSort(String order, MusicFiles o1, MusicFiles o2){
        switch (order){
            case "asc":
                return o1.getAlbumName().compareToIgnoreCase(o2.getAlbumName());
            case "desc":
                return o2.getAlbumName().compareToIgnoreCase(o1.getAlbumName());
            case "artist":
                return o1.getArtistName().compareToIgnoreCase(o2.getArtistName());
            case "no_songs":
                return o1.getNo_songs().compareToIgnoreCase(o2.getNo_songs());
            case "year":
                return o1.getNo_songs().compareToIgnoreCase(o2.getNo_songs());
        }
        return 0;
    }

    private void refreshFragment(){
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new AlbumFragment()).commit();
    }
    private void sortList(List<MusicFiles> list){
        Collections.sort(list, (o1, o2) -> getSort(order, o1, o2));
    }

    private boolean getPopup(MenuItem item) {
        //Album Sorting
        if (item.getItemId() == R.id.ascending_item2) {
            return sort(item, "asc");
        }
        else if (item.getItemId() == R.id.descending_item2) {
            return sort(item, "desc");
        }
        else if (item.getItemId() == R.id.artist_item2) {
            return sort(item, "artist");
        }
        else if (item.getItemId() == R.id.song_count_item2) {
            return sort(item, "no_songs");
        }
        else if (item.getItemId() == R.id.year_item) {
            return sort(item, "year");
        }
        //Album Grid Style
        if (item.getItemId() == R.id.list_item2) {
            return style(item, "list");
        } else if (item.getItemId() == R.id.card_item2) {
            return style(item, "card");
        }
        //Album Grid Size
        if (item.getItemId() == R.id.one_item2) {
            return size(item, "one");
        } else if (item.getItemId() == R.id.two_item2) {
            return size(item, "two");
        } else if (item.getItemId() == R.id.three_item2) {
            return size(item, "three");
        } else if (item.getItemId() == R.id.four_item2) {
            return size(item, "four");
        }
        //Settings
        if (item.getItemId() == R.id.settings_item) {
            Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}