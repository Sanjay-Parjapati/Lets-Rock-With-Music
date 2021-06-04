package com.parjapatsanjay1999.letsRockWithMusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView btnPlayPause, btnPrev, btnNext;
    TextView tvPlayerSongName;
    ImageView playerImage;
    String songName;
    int pos;
    static MediaPlayer mediaPlayer;
    static final ArrayList<File> mySongs = MainActivity.songsList;
    BarVisualizer barVisualizer;

    @Override
    protected void onDestroy() {

        if (mediaPlayer != null)
            stopMediaPlayer();
        if (barVisualizer != null)
            barVisualizer.release();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_);

        // play current song
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // initialises all other required variables
        initialiseAll();

        Intent intent = getIntent();
        pos = intent.getIntExtra(MainActivity.SONG_POSITION, 0);

        Log.d(MainActivity.TAG, "onCreate: Received Song " + pos + " " + songName);
        playSong(pos);
        setListeners();
    }

    private void initialiseAll() {
        btnPlayPause = findViewById(R.id.playPauseButton);
        btnPrev = findViewById(R.id.prevButton);
        btnNext = findViewById(R.id.nextButton);

        tvPlayerSongName = findViewById(R.id.tvPlayerSongName);

        playerImage = findViewById(R.id.ivPlayerSongImage);

        barVisualizer = findViewById(R.id.barVisualiser);

    }

    private void playSong(int pos) {
        Uri uri = Uri.parse((mySongs.get(pos)).toString());
        tvPlayerSongName.setSelected(true);
        songName = (((mySongs.get(pos))).getName()).replace(".mp3", "");
        tvPlayerSongName.setText(songName);
        tvPlayerSongName.setSelected(true);
        Log.d(MainActivity.TAG, "Now playing: " + pos + " " + songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        startMediaPlayer();
    }

    private void startMediaPlayer() {
        if (mediaPlayer == null)
            Log.d(MainActivity.TAG, "startMediaPlayer: mediaPlayer is null");
        else {
            mediaPlayer.start();
            btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        }

        int audioSessionID = mediaPlayer.getAudioSessionId();
        if (audioSessionID != -1)
            barVisualizer.setAudioSessionId(audioSessionID);
    }

    private void pauseMediaPlayer() {
        mediaPlayer.pause();
        btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
    }

    private void stopMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void playPreviousSong() {
        if (mediaPlayer != null)
            stopMediaPlayer();
        pos = (pos - 1 < 0) ? (mySongs.size() - 1) : (pos - 1);
        playSong(pos);
    }

    private void playNextSong() {
        if (mediaPlayer != null)
            stopMediaPlayer();
        pos = (pos + 1) % mySongs.size();
        playSong(pos);
    }

    private void setListeners() {
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying())
                pauseMediaPlayer();
            else
                startMediaPlayer();
        });
        btnNext.setOnClickListener(v -> playNextSong());
        btnPrev.setOnClickListener(v -> playPreviousSong());
        mediaPlayer.setOnCompletionListener(mp -> playNextSong());
    }
}