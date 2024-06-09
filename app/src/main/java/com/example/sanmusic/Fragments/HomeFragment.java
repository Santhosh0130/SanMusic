package com.example.sanmusic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.sanmusic.Activities.SearchActivity;
import com.example.sanmusic.R;
import com.example.sanmusic.SongPopupMenu;

public class HomeFragment extends Fragment {

    ImageView settings;
    SongPopupMenu songPopupMenu;

    ImageView search;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        settings = view.findViewById(R.id.settings);
        view.findViewById(R.id.search_btn).setOnClickListener(v ->{
            startActivity(new Intent(requireActivity(), SearchActivity.class));
        });
        settings.setOnClickListener(v -> {
            songPopupMenu = new SongPopupMenu(getContext(), v);
        });
        return view;
    }
}