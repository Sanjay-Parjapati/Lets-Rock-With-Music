package com.parjapatsanjay1999.letsRockWithMusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.parjapatsanjay1999.letsRockWithMusic.adapters.RecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

    public static final String SONG_POSITION = "position";
    public static final String TAG = "verma";

    private RecyclerView recyclerViewSongList;
    public static ArrayList<File> songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseAll();
        askForPermissions();
    }

    private void initialiseAll() {
        recyclerViewSongList = findViewById(R.id.listViewSongs);
        recyclerViewSongList.setHasFixedSize(true);
        recyclerViewSongList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void askForPermissions() {
        Dexter.withContext(getApplicationContext()).withPermissions(READ_EXTERNAL_STORAGE, RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        setAdapter();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private ArrayList<File> findSongs(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && !f.isHidden())
                    arrayList.addAll(findSongs(f));
                else {
                    if (f.getName().endsWith(".mp3"))
                        arrayList.add(f);
                }
            }
        }
        return arrayList;
    }

    private void setAdapter() {
        songsList = findSongs(Environment.getExternalStorageDirectory());
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        recyclerViewSongList.setAdapter(adapter);

    }
}