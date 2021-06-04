package com.parjapatsanjay1999.letsRockWithMusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parjapatsanjay1999.letsRockWithMusic.MainActivity;
import com.parjapatsanjay1999.letsRockWithMusic.PlayerActivity;
import com.parjapatsanjay1999.letsRockWithMusic.R;

import java.io.File;
import java.util.ArrayList;

import static com.parjapatsanjay1999.letsRockWithMusic.R.layout.list_item;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.SongListViewHolder> {
    static Context context;
    private static ArrayList<File> songsList=  MainActivity.songsList;

    public RecyclerViewAdapter(Context context) {
        RecyclerViewAdapter.context = context;
    }

    @NonNull
    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(list_item, parent, false);
        return new SongListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder holder, int position) {
        String songName = ((songsList.get(position)).getName()).replace(".mp3", "");

        holder.setIcMusic(R.drawable.ic_music);
        holder.setSongName(songName);
    }

    @Override
    public int getItemCount() {
        return (MainActivity.songsList).size();
    }

    public static class SongListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icMusic;
        private TextView songName;

        public SongListViewHolder(@NonNull View itemView) {
            super(itemView);
            icMusic = itemView.findViewById(R.id.icMusic);
            songName = itemView.findViewById(R.id.tvListItemName);
            itemView.setOnClickListener(this);
        }

        public void setIcMusic(int icMusic) {
            this.icMusic.setImageResource(icMusic);
        }

        public void setSongName(String songName) {
            this.songName.setText(songName);
        }

        @Override
        public void onClick(View v) {
            int pos = getBindingAdapterPosition();
            Log.d(MainActivity.TAG, "onClick: Sending song "+pos);
            Intent sendData = new Intent(context, PlayerActivity.class);
            sendData.putExtra(MainActivity.SONG_POSITION,pos);
            context.startActivity(sendData);
        }
    }
}
