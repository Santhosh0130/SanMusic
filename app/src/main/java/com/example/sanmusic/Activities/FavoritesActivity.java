package com.example.sanmusic.Activities;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.model.ModelLoader;
import com.example.sanmusic.AdapterClasses.AddFavAdapter;
import com.example.sanmusic.DataBase.FavDB;
import com.example.sanmusic.MusicFiles;
import com.example.sanmusic.R;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView favActBack;
    AddFavAdapter favAdapter;
    public List<MusicFiles> songFilesList = new ArrayList<>();
    FavDB favDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


//        i am not used this activity, coz i am changed it to fragment

        //For Transparent status View
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favDB = new FavDB(this);

//        recyclerView = findViewById(R.id.fav_recyclerView);
//        favActBack = findViewById(R.id.fav_back);

        favActBack.setOnClickListener(v -> this.finish());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    @SuppressLint("Range")
    private void loadData() {
        if (songFilesList != null){
            songFilesList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.selectAllInList();
        try {
            while (cursor.moveToNext()){

                int position = cursor.getInt(cursor.getColumnIndex(FavDB.SONG_POSITION));

//                MusicFiles songFiles = new MusicFiles(position);
////                songFiles.setChkPosition(position);
//                songFilesList.add(songFiles);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

//        favAdapter = new AddFavAdapter(this, songFilesList);
//        recyclerView.setAdapter(favAdapter);
    }
}