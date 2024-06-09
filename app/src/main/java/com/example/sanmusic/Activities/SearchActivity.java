package com.example.sanmusic.Activities;

import static com.example.sanmusic.Activities.SlideUpPanelActivity.realSongs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanmusic.AdapterClasses.SearchAdapter;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText search;
    RecyclerView recyclerView;
    TextView songCount;
    SearchAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //For Transparent status View
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        recyclerView = findViewById(R.id.search_recyclerview);
        findViewById(R.id.clear_all).setOnClickListener(v -> {
            search.setText("");
        });
        songCount = findViewById(R.id.file_count);
        songCount.setText(realSongs.size()+" Files");
        search = findViewById(R.id.search_bar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                List<MusicFiles> myFiles = new ArrayList<>();
                for (MusicFiles song : realSongs) {

                    String title = song.getTitle().toLowerCase();
                    String artist = song.getArtist().toLowerCase();
                    String album = song.getAlbum().toLowerCase();
                    String searched = s.toString().toLowerCase();

                    if (title.contains(searched) || artist.contains(searched) || album.contains(searched)) {
                        myFiles.add(song);
                    }
                }
                adapter.upDateList(myFiles);
                songCount.setText(myFiles.size()+" Files");
                Toast.makeText(getApplicationContext(), "myFiles "+myFiles.size(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new SearchAdapter(this, realSongs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}