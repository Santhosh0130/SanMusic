package com.example.sanmusic.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sanmusic.SongPopupMenu.MY_GRID_SIZE_SETTINGS;
import static com.example.sanmusic.SongPopupMenu.MY_GRID_STYLE_SETTINGS;
import static com.example.sanmusic.SongPopupMenu.MY_SORT_SETTINGS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.example.sanmusic.Activities.SearchActivity;
import com.example.sanmusic.AdapterClasses.MusicAdapter;
import com.example.sanmusic.FetchSongs;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.util.Collections;
import java.util.List;

public class MusicFragment extends Fragment {
    RecyclerView recyclerView;
    public static MusicAdapter adapter;
    String Order = "asc";
    String Style = "list";
    String Size = "1";
    public List<MusicFiles> sortedList;
    ImageView settings;
    public MusicFragment() {
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.songs_more_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getPopup);
            checkMenu(popupMenu.getMenu());
            popupMenu.show();
        });
        view.findViewById(R.id.search_btn).setOnClickListener(v ->{
            startActivity(new Intent(requireActivity(), SearchActivity.class));
        });

        Order = requireActivity().getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).getString("songSorting","asc");

        Style = requireActivity().getSharedPreferences(MY_GRID_STYLE_SETTINGS, MODE_PRIVATE).getString("song_grid_style", "card");

        Size = requireActivity().getSharedPreferences(MY_GRID_SIZE_SETTINGS, MODE_PRIVATE).getString("song_grid_size", "2");
        sortedList = new FetchSongs().getSongs(requireContext());

        Collections.sort(sortedList, this::getSort);

        recyclerView.setHasFixedSize(true);
        //For Show the Songs
        if (Style.equals("list") ) {
            adapter = new MusicAdapter(getContext(), sortedList, R.layout.list_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
        } else {
            switch (Size) {
                case "1":
                    adapter = new MusicAdapter(getContext(), sortedList, R.layout.list_items);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                    break;
                case "2":
                    adapter = new MusicAdapter(getContext(), sortedList, R.layout.card_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                    break;
                case "3":
                    adapter = new MusicAdapter(getContext(), sortedList, R.layout.card_item_3);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
                    break;
                case "4":
                    adapter = new MusicAdapter(getContext(), sortedList, R.layout.card_items_4);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 4));
                    break;
            }
        }
        recyclerView.setAdapter(adapter);
        return view;
    }
    private void checkMenu(Menu menu) {
        switch (Order){
            case "asc":
                menu.findItem(R.id.ascending_item).setChecked(true);
                break;
            case "desc":
                menu.findItem(R.id.descending_item).setChecked(true);
                break;
            case "artist":
                menu.findItem(R.id.artist_item).setChecked(true);
                break;
            case "dateAdd":
                menu.findItem(R.id.date_added_item).setChecked(true);
                break;
            case "dateMod":
                menu.findItem(R.id.date_modified_item).setChecked(true);
                break;
            default:
                menu.findItem(R.id.ascending_item).setChecked(true);
                break;
        }

        if (Style.equals("list")){
            menu.findItem(R.id.list_item).setChecked(true);
        } else {
            menu.findItem(R.id.card_item).setChecked(true);
        }

        switch (Size) {
            case "1":
                menu.findItem(R.id.one_item).setChecked(true);
                break;
            case "2":
                menu.findItem(R.id.two_item).setChecked(true);
                break;
            case "3":
                menu.findItem(R.id.three_item).setChecked(true);
                break;
            case "4":
                menu.findItem(R.id.four_item).setChecked(true);
                break;
            default:
                menu.findItem(R.id.two_item).setChecked(true);
                break;
        }
    }
    private int getSort(MusicFiles o1, MusicFiles o2){
        switch (Order) {
            case "asc":
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            case "desc":
                return o2.getTitle().compareToIgnoreCase(o1.getTitle());
            case "artist":
                return o1.getArtistName().compareToIgnoreCase(o2.getArtistName());
            case "dateAdd":
                return o1.getDateAdded().compareToIgnoreCase(o2.getDateAdded());
            case "dateMod":
                return o1.getDateModified().compareToIgnoreCase(o2.getDateModified());
        }

        return 0;
    }
    private void sortList(List<MusicFiles> list){
        Collections.sort(list, this::getSort);
    }
    private void refreshFragment(){
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MusicFragment()).commit();
    }

    private boolean sort(MenuItem item, String type){
        SharedPreferences.Editor songSortEditor = requireActivity().getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        songSortEditor.putString("songSorting", type).apply();
        item.setChecked(true);
        sortList(sortedList);
        refreshFragment();
        return true;
    }

    private boolean style(MenuItem item, String type){
        SharedPreferences.Editor songGridStyleEditor = requireActivity().getSharedPreferences(MY_GRID_STYLE_SETTINGS, Context.MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        songGridStyleEditor.putString("song_grid_style", type).apply();
        item.setChecked(true);
        refreshFragment();
        return true;
    }

    private boolean size(MenuItem item, String type){
        SharedPreferences.Editor songGridSizeEditor = requireActivity().getSharedPreferences(MY_GRID_SIZE_SETTINGS, Context.MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        songGridSizeEditor.putString("song_grid_size", type).apply();
        item.setChecked(true);
        refreshFragment();
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    private boolean getPopup(MenuItem item) {
        //Song Sorting
        if (item.getItemId() == R.id.ascending_item) {
            return sort(item, "asc");
        }
        else if (item.getItemId() == R.id.descending_item) {
            return sort(item, "desc");
        }
        else if (item.getItemId() == R.id.artist_item) {
            return  sort(item, "artist");
        }
        else if (item.getItemId() == R.id.date_added_item) {
            return sort(item, "dateAdd");
        }
        else if (item.getItemId() == R.id.date_modified_item) {
            return sort(item, "dateMod");
        }
        //Song Grid Style
        if (item.getItemId() == R.id.list_item) {
            return style(item, "list");
        } else if (item.getItemId() == R.id.card_item) {
            return style(item, "card");
        }
        //Song Grid Size
        if (item.getItemId() == R.id.one_item) {
            return size(item, "1");
        } else if (item.getItemId() == R.id.two_item) {
            return size(item, "2");
        } else if (item.getItemId() == R.id.three_item) {
            return size(item, "3");
        } else if (item.getItemId() == R.id.four_item) {
            return size(item, "4");
        }
        //Settings
        if (item.getItemId() == R.id.settings_item) {
            Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}