package com.example.sanmusic.Activities;
//
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.MenuItem;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.viewpager.widget.ViewPager;
//
//import com.example.sanmusic.Fragments.MusicFragment;
//import com.example.sanmusic.MusicFiles;
//import com.example.sanmusic.R;
//import com.google.android.material.tabs.TabLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
public class MainActivity extends AppCompatActivity{


//
//    public static final int REQUEST_CODE = 101;
//    public static boolean shuffle_boolean;
//    public static boolean repeat_boolean;
//    private static String MY_SORT_PREF = "SortOrder: ";
//    public static List<MusicFiles> musicFiles;
//    public static List<MusicFiles> albums = new ArrayList<>();
//    public static List<MusicFiles> favorites = new ArrayList<>();
//    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
//    public static final String MUSIC_FILE = "STORED_FILE";
//    public static final String SONG_NAME = "SONG NAME";
//    public static final String ARTIST_NAME = "ARTIST NAME";
//    public static boolean SHOW_MINI_PLAYER = false;
//    public static String PATH_FOR_BOTTOM = null;
//    public static String SONG_FOR_BOTTOM = null;
//    public static String ARTIST_FOR_BOTTOM = null;
//    EditText search;
//    ImageView expand;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //For Transparent status View
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//        setContentView(R.layout.activity_main);
////        Permission();
//
//        expand = findViewById(R.id.expand_more);
//        expand.setOnClickListener(v->{
//            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
//            PopupMenu popupMenu = new PopupMenu(this,v);
//            popupMenu.setOnMenuItemClickListener(this);
//            popupMenu.inflate(R.menu.popup_menu);
//            popupMenu.show();
//        });
//
//        //For Search a Songs
//        search = findViewById(R.id.search_bar);
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                List<MusicFiles> myFiles = new ArrayList<>();
//                for (MusicFiles song : musicFiles){
//                    if (song.getTitle().toLowerCase().contains(s.toString().toLowerCase())){
//                        myFiles.add(song);
//                    }
//                }
//                MusicFragment.adapter.updateList(myFiles);
//            }
//        });
//    }
//
//    private void Permission() {
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
//        } else {
//            Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
//            musicFiles = getFiles(this);
//            initViewPager();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                musicFiles = getFiles(this);
//                initViewPager();
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
//            }
//        }
//    }
//
//    //Initial the viewpager
//    private void initViewPager() {
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//
////        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
////        viewPagerAdapter.adaFragments(new MusicFragment(),"Songs");
////        viewPagerAdapter.adaFragments(new AlbumFragment(),"Albums");
////        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//
//    //For Sort the Songs List
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        SharedPreferences.Editor editor = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE).edit();
//        if (item.getItemId() == R.id.byName) {
//            editor.putString("sorting","ByName");
//            editor.apply();
//            this.recreate();
//            return true;
//        } else if (item.getItemId() == R.id.byDate) {
//            editor.putString("sorting","ByDate");
//            editor.apply();
//            this.recreate();
//            return true;
//        }  else if (item.getItemId() == R.id.bySize) {
//            editor.putString("sorting","BySize");
//            editor.apply();
//            this.recreate();
//            return true;
//        }
//        return false;
//    }
//
//    //For add a Fragments into ViewPager
////    public static class ViewPagerAdapter extends FragmentPagerAdapter{
////
////        private final List<Fragment> fragments;
////        private final List<String> titles;
////
////        public ViewPagerAdapter(@NonNull FragmentManager fm) {
////            super(fm);
////            this.fragments = new ArrayList<>();
////            this.titles = new ArrayList<>();
////        }
////        public void adaFragments(Fragment fragment, String title){
////            fragments.add(fragment);
////            titles.add(title);
////        }
////
////        @NonNull
////        @Override
////        public Fragment getItem(int position) {
////            return fragments.get(position);
////        }
////
////        @Override
////        public int getCount() {
////            return fragments.size();
////        }
////
////        @Nullable
////        @Override
////        public CharSequence getPageTitle(int position) {
////            return titles.get(position);
////        }
////    }
//
//    //Fetch the musics
//    public List<MusicFiles> getFiles(Context context){
//        SharedPreferences sharedPreferences = getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE);
//        String sortOrder = sharedPreferences.getString("sorting","ByName");
//        albums.clear();
//        List<MusicFiles> FetchedFiles = new ArrayList<>();
//        List<String> DuplicateFilesInAlbum = new ArrayList<>();
//        String order = null;
//        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        switch (sortOrder){
//            case "ByName":
//                order = MediaStore.MediaColumns.DISPLAY_NAME+" ASC";
//                break;
//            case "ByDate":
//                order = MediaStore.MediaColumns.DATE_ADDED+" ASC";
//                break;
//            case "BySize":
//                order = MediaStore.MediaColumns.SIZE+" DESC";
//                break;
//        }
//        String[] projection = {
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.TITLE,
//        };
//        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,order);
//        if (cursor != null){
//            while (cursor.moveToNext()){
//                String album = cursor.getString(0);
//                String path = cursor.getString(1);
//                String artist = cursor.getString(2);
//                String duration = cursor.getString(3);
//                String title = cursor.getString(4);
//
//                MusicFiles musicFiles = new MusicFiles(path,title,album,artist,duration);
//                FetchedFiles.add(musicFiles);
//
//                if (!DuplicateFilesInAlbum.contains(album)){
//                    albums.add(musicFiles);
//                    DuplicateFilesInAlbum.add(album);
//                }
//            }
//            cursor.close();
//        }
//        return FetchedFiles;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences preferences = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
//        String pathForBottom = preferences.getString(MUSIC_FILE,null);
//        String songForBottom = preferences.getString(SONG_NAME,null);
//        String artistForBottom = preferences.getString(ARTIST_NAME,null);
//        if (pathForBottom != null){
//            SHOW_MINI_PLAYER = true;
//            PATH_FOR_BOTTOM = pathForBottom;
//            ARTIST_FOR_BOTTOM = artistForBottom;
//            SONG_FOR_BOTTOM = songForBottom;
//        } else {
//            SHOW_MINI_PLAYER = false;
//            PATH_FOR_BOTTOM = null;
//            ARTIST_FOR_BOTTOM = null;
//            SONG_FOR_BOTTOM = null;
//        }
//    }
}