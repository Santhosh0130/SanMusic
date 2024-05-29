package com.example.sanmusic.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanmusic.AdapterClasses.AlbumAdapter;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;
import com.example.sanmusic.SongPopupMenu;

import java.util.List;

public class PlaylistFragment extends Fragment {

    ImageView settings;
    SongPopupMenu songPopupMenu;
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    List<MusicFiles> playlists;

    public PlaylistFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        settings = view.findViewById(R.id.settings);
        recyclerView = view.findViewById(R.id.playlist_recyclerView);
        recyclerView.setHasFixedSize(true);
        

        settings.setOnClickListener(v -> {
//            songPopupMenu = new SongPopupMenu(getContext(), v);
//            songPopupMenu.createSortPopupMenu(false, );
        });

        albumAdapter = new AlbumAdapter(playlists, requireContext(), R.layout.card_item);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(albumAdapter);

        return view;
    }
}