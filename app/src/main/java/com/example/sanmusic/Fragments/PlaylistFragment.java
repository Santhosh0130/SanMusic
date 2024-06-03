package com.example.sanmusic.Fragments;

import static com.example.sanmusic.Activities.SlideUpPanelActivity.playlists;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanmusic.AdapterClasses.AlbumAdapter;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;
import com.google.android.material.textfield.TextInputLayout;

public class PlaylistFragment extends Fragment {

    ImageView settings;
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;

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
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.play_list_popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this::getPopup);
            popupMenu.show();
        });

        albumAdapter = new AlbumAdapter(playlists, requireContext(), R.layout.card_item, true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(albumAdapter);

        return view;
    }

    private boolean getPopup(MenuItem item) {
        if (item.getItemId() == R.id.new_playlist_item){
            Toast.makeText(requireContext(), "Ready to toast", Toast.LENGTH_SHORT).show();
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.new_playlist_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            TextInputLayout textInputLayout = dialog.findViewById(R.id.get_title);
            dialog.findViewById(R.id.ok_button).setOnClickListener(v -> {
                playlists.add(new MusicFiles(textInputLayout.getEditText().getText().toString()));
                dialog.cancel();
            });
            dialog.show();
            return true;
        }
        return false;
    }
}