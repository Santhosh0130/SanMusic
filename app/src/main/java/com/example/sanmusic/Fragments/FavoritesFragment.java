package com.example.sanmusic.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sanmusic.Activities.SlideUpPanelActivity.MY_SORT_SETTINGS;
import static com.example.sanmusic.Activities.SlideUpPanelActivity.realSongs;
import static com.example.sanmusic.SongPopupMenu.MY_GRID_SIZE_SETTINGS;
import static com.example.sanmusic.SongPopupMenu.MY_GRID_STYLE_SETTINGS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.sanmusic.AdapterClasses.AddFavAdapter;
import com.example.sanmusic.DataBase.FavDB;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;
import com.example.sanmusic.SongPopupMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesFragment extends Fragment {

    RecyclerView recyclerView;
    public static AddFavAdapter favAdapter;
    public static List<MusicFiles> songFilesList = new ArrayList<>();
    public static List<MusicFiles> positionFilesList = new ArrayList<>();
    FavDB favDB;
    ImageView settings;
    String order;
    String Style;
    String Size;
    SongPopupMenu songPopupMenu;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        favDB = new FavDB(getContext());

        fetchData();
//        loadData();

        recyclerView = view.findViewById(R.id.fav_recyclerView);
        settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.songs_more_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getPopup);
            popupMenu.show();
        });

        order = requireContext().getSharedPreferences(MY_SORT_SETTINGS, MODE_PRIVATE).getString("favSorting", "asc");

        Style = requireActivity().getSharedPreferences(MY_GRID_STYLE_SETTINGS, MODE_PRIVATE).getString("fav_grid_style", "list");

        Size = requireActivity().getSharedPreferences(MY_GRID_SIZE_SETTINGS, MODE_PRIVATE).getString("fav_grid_size", "1");

        Collections.sort(songFilesList, this::getSort);

        recyclerView.setHasFixedSize(true);
        if (Style.equals("list")){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            favAdapter = new AddFavAdapter(getContext(), songFilesList, positionFilesList, R.layout.list_items);
        }else {
            switch (Size) {
                case "1":
                    favAdapter = new AddFavAdapter(getContext(), songFilesList, positionFilesList, R.layout.list_items);
                    recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                    break;
                case "2":
                    favAdapter = new AddFavAdapter(getContext(), songFilesList, positionFilesList, R.layout.card_item);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
                    break;
                case "3":
                    favAdapter = new AddFavAdapter(getContext(), songFilesList, positionFilesList, R.layout.card_item_3);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
                    break;
                case "4":
                    favAdapter = new AddFavAdapter(getContext(), songFilesList, positionFilesList, R.layout.card_items_4);
                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 4));
                    break;
                default:
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    favAdapter = new AddFavAdapter(getContext(), songFilesList, positionFilesList, R.layout.list_items);
                    break;
            }
        }

        recyclerView.setAdapter(favAdapter);

        return view;
    }
    private void checkMenu(Menu menu) {
        switch (order){
            case "asc":
                menu.findItem(R.id.ascending_item3).setChecked(true);
                break;
            case "desc":
                menu.findItem(R.id.descending_item3).setChecked(true);
                break;
            default:
                menu.findItem(R.id.ascending_item3).setChecked(true);
                break;
        }

        if (Style.equals("list")){
            menu.findItem(R.id.list_item3).setChecked(true);
        } else {
            menu.findItem(R.id.card_item3).setChecked(true);
        }

        switch (Size) {
            case "1":
                menu.findItem(R.id.one_item3).setChecked(true);
                break;
            case "2":
                menu.findItem(R.id.two_item3).setChecked(true);
                break;
            case "3":
                menu.findItem(R.id.three_item3).setChecked(true);
                break;
            case "4":
                menu.findItem(R.id.four_item3).setChecked(true);
                break;
            default:
                menu.findItem(R.id.two_item3).setChecked(true);
                break;
        }
    }
    private void sortList(List<MusicFiles> list){
        Collections.sort(list, this::getSort);
    }
    private int getSort(MusicFiles o1, MusicFiles o2){
        switch (order) {
            case "asc":
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            case "desc":
                return o2.getTitle().compareToIgnoreCase(o1.getTitle());
        }

        return 0;}
    private void refreshFragment(){
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MusicFragment()).commit();
    }
    private boolean sort(MenuItem item, String type){
        SharedPreferences.Editor FavSortEditor = requireActivity().getSharedPreferences(SongPopupMenu.MY_SORT_SETTINGS, MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        FavSortEditor.putString("favSorting", type).apply();
        item.setChecked(true);
        sortList(songFilesList);
        refreshFragment();
        return true;
    }

    private boolean style(MenuItem item, String type){
        SharedPreferences.Editor favGridStyleEditor = requireActivity().getSharedPreferences(MY_GRID_STYLE_SETTINGS, Context.MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        favGridStyleEditor.putString("fav_grid_style", type).apply();
        item.setChecked(true);
        refreshFragment();
        return true;
    }

    private boolean size(MenuItem item, String type){
        SharedPreferences.Editor favGridSizeEditor = requireActivity().getSharedPreferences(MY_GRID_SIZE_SETTINGS, Context.MODE_PRIVATE).edit();
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        favGridSizeEditor.putString("fav_grid_size", type).apply();
        item.setChecked(true);
        refreshFragment();
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    private boolean getPopup(MenuItem item) {
        //Favorites Sort
        if (item.getItemId() == R.id.ascending_item3){
            return sort(item, "asc");
        }
        else if (item.getItemId() == R.id.descending_item3){
            return sort(item, "desc");
        }
        //Favorites Grid Style
        if (item.getItemId() == R.id.list_item3) {
            return style(item, "list");
        } else if (item.getItemId() == R.id.card_item3) {
            return style(item, "card");
        }
        //Favorites Grid Size
        if (item.getItemId() == R.id.one_item3) {
            return size(item, "1");
        } else if (item.getItemId() == R.id.two_item3) {
            return size(item, "2");
        } else if (item.getItemId() == R.id.three_item3) {
            return size(item, "3");
        } else if (item.getItemId() == R.id.four_item3) {
            return size(item, "4");
        }
        //Settings
        if (item.getItemId() == R.id.settings_item) {
            Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void loadData() {
        int j = 0;
        for (int i = 0; i < realSongs.size(); i++) {
            if (positionFilesList.size() == j) {
                break;
            }
            if (realSongs.get(i).getIndexSong() == positionFilesList.get(j).getPosition_fav()) {
                songFilesList.add(j, realSongs.get(i));
                j++;
            }
        }
    }

    @SuppressLint("Range")
    private void fetchData() {
        if (songFilesList != null){
            songFilesList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.selectAllInList();
        try {
            while (cursor.moveToNext()){
                int position = cursor.getInt(cursor.getColumnIndex(FavDB.SONG_POSITION));
                MusicFiles positionsList = new MusicFiles(position);
                positionFilesList.add(positionsList);
                int j = 0;
                for (int i = 0; i < realSongs.size(); i++) {
                    if (realSongs.get(i).getIndexSong() == position) {
                        songFilesList.add(j, realSongs.get(i));
                        j++;
                    }
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }
    }
}