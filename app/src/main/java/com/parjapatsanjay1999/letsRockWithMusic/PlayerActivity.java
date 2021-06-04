package com.parjapatsanjay1999.letsRockWithMusic;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    private static final int FF_VALUE = 5000;
    TextView btnPlayPause, btnPrev, btnNext, btnFastRewind, btnFastForward;
    TextView tvPlayerSongName, tvPlayerSongStart, tvPlayerSongStop;
    ImageView playerImage;
    SeekBar seekBar;
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
        btnFastRewind = findViewById(R.id.fastRewindButton);
        btnFastForward = findViewById(R.id.fastForwardbutton);

        tvPlayerSongName = findViewById(R.id.tvPlayerSongName);
        tvPlayerSongStart = findViewById(R.id.seekbarstart);
        tvPlayerSongStop = findViewById(R.id.seekbarEnd);

        seekBar = findViewById(R.id.seekBar);
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
        seekBarSetup();
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

        btnFastForward.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + FF_VALUE);
            }
        });
        btnFastRewind.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - FF_VALUE);
            }
        });

    }

    private void seekBarSetup() {
        seekBar.setMax(mediaPlayer.getDuration());
        tvPlayerSongStop.setText(createTime(mediaPlayer.getDuration()));
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tvPlayerSongStart.setText(createTime(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 950);
                } catch (Exception e) {
                    seekBar.setProgress(0);
                }
            }
        }, 0);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    tvPlayerSongStart.setText(createTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                tvPlayerSongStart.setText(createTime(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                tvPlayerSongStart.setText(createTime(mediaPlayer.getCurrentPosition()));
            }
        });
    }

    private String createTime(long millis) {
        int min = (int) (millis / 1000 / 60);
        int sec = (int) (millis / 1000 % 60);
        return String.format("%02d:%02d", min, sec);
    }
}