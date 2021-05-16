package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaylistDangNghe_rvAdapter extends RecyclerView.Adapter<PlaylistDangNghe_rvAdapter.PlaylistDangNghe_ViewHolder>{
    private Context context;
    private ArrayList<Nhac> dsn;
    private HashMap<Integer, TextView> dsTextView;
    private ArrayList<Integer> positionNumber;
    private int playPosition = -1;
    private boolean isShuffling = false;
    private boolean isLooping = false;
    private boolean isLoopingOne = false;
    private boolean isViewUpdated = false;
    private MediaPlayer mediaPlayer;
    private Handler myHandler = new Handler();

    public interface UpdateSongTime {
        void updateTimeTextView(String currentTime);
        void setSongInfomation(Nhac nhac, String endTime, int seekbarMax);
        void updateSeekBar(int progress);
        void scrollToCurrentPlayingMusic(int position);
        void onMediaPrepared();
        void onMediaReleased();
        void onMediaFinished();
    }

    private PlaylistDangNghe_rvAdapter.UpdateSongTime listener;

    public PlaylistDangNghe_rvAdapter(Context context) {
        this.context = context;
        this.dsn = new ArrayList<>();
        positionNumber = new ArrayList<>();
        dsTextView = new HashMap<>();
    }

    @NonNull
    @Override
    public PlaylistDangNghe_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaylistDangNghe_ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_playlist_dangnghe, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDangNghe_ViewHolder holder, final int position) {
        Nhac nhac = dsn.get(position);
        dsTextView.put(position, holder.txtTenNhac);
        holder.txtTenNhac.setText(nhac.getTenNhac());
        holder.txtTenNgheSi.setText(nhac.getTenNgheSi());
        holder.txtThoiLuong.setText(nhac.getThoiLuong());
        holder.txtTenNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playPosition != position){
                    releaseMedia();
                    playPosition = position;
                    if (positionNumber.size() > 0) {
                        positionNumber.clear();
                    }
                    triggerAutoPlay(playPosition);
                }
            }
        });
        if (position == 0){
            releaseMedia();
            playPosition = position;
            triggerAutoPlay(position);
            holder.txtTenNhac.setTextColor(ContextCompat.getColor(context, R.color.emberColor));
        }
    }
    @Override
    public int getItemCount() {
        return dsn.size();
    }

    static class PlaylistDangNghe_ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenNhac, txtTenNgheSi, txtThoiLuong;
        public PlaylistDangNghe_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenNhac = itemView.findViewById(R.id.txtTenNhac);
            txtTenNgheSi = itemView.findViewById(R.id.txtTenNgheSi);
            txtThoiLuong = itemView.findViewById(R.id.txtThoiLuong);
        }
    }
    public void setUpdateSongTimeListener(PlaylistDangNghe_rvAdapter.UpdateSongTime listener){
        this.listener = listener;
    }

    public void setMusicList(ArrayList<Nhac> dsn){
        this.dsn.clear();
        this.dsn.addAll(dsn);
        notifyDataSetChanged();
    }

    public Nhac getCurrentSong(){
        return dsn.get(playPosition);
    }
    public void clearQueue(){
        releaseMedia();
        dsn.clear();
        dsTextView.clear();
        notifyDataSetChanged();
    }
    public void removeSongFromQueue(int position){
        if (playPosition == position){
            myHandler.removeCallbacks(UpdateSongTime);
            releaseMedia();
            dsn.remove(position);
            dsTextView.remove(position);
        }else{
            dsn.remove(position);
            dsTextView.remove(position);
        }
    }
    public void addSongToQueue(Nhac nhac) {
        for (Nhac nhac1: dsn){
            if (nhac1.getMaNhac().equals(nhac.getMaNhac())){
                return;
            }
        }
        this.dsn.add(nhac);
        notifyDataSetChanged();
    }

    public void releaseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void releaseUpdateSongTimeListener(){
        myHandler.removeCallbacks(UpdateSongTime);
    }
    public void onSeekbarProgressChanged(int progress){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(progress);
            }
        }
    }
    public boolean playAndPauseMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                return true;
            } else {
                mediaPlayer.start();
                myHandler.postDelayed(UpdateSongTime, 500);
                return false;
            }
        }
        return false;
    }

    public void skipToPrevious() {
        releaseMedia();
        if (isShuffling){
            while (true){
                int newPlayPosition = (int) (100.0 * Math.random() % dsn.size());
                if (playPosition != newPlayPosition) {
                    playPosition = newPlayPosition;
                    break;
                }
            }
            triggerAutoPlay(playPosition);
        } else if (playPosition > 0) {
            playPosition -= 1;
            triggerAutoPlay(playPosition);
        }else {
            playPosition = dsn.size() - 1;
            triggerAutoPlay(playPosition);
        }
    }

    public void skipToNext() {
        releaseMedia();
        if (isShuffling){
            while (true){
                int newPlayPosition = (int) (100.0 * Math.random() % dsn.size());
                if (playPosition != newPlayPosition) {
                    playPosition = newPlayPosition;
                    break;
                }
            }
            triggerAutoPlay(playPosition);
        } else if (playPosition < dsn.size()-1){
            playPosition += 1;
            triggerAutoPlay(playPosition);
        }else {
            playPosition = 0;
            triggerAutoPlay(playPosition);
        }
    }

    public void setLoopingOne(boolean isLoopingOne) {
        this.isLoopingOne = isLoopingOne;
    }

    public void setShuffling(boolean isShuffling) {
        this.isShuffling = isShuffling;
        if (positionNumber.size() > 0) {
            positionNumber.clear();
        }
    }
    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    private void triggerAutoPlay(final int position) {
        for (Integer i: dsTextView.keySet()){
            if (i == position){
                dsTextView.get(i).setTextColor(ContextCompat.getColor(context, R.color.emberColor));
            }else {
                dsTextView.get(i).setTextColor(Color.WHITE);
            }
        }
        final Nhac nhac = dsn.get(position);
        if (nhac.getURL() != null) {
            mediaPlayer = DAO_Nhac.createMediaPlayer(nhac);
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                listener.setSongInfomation(nhac, AdditionalFunctions.changeTimeFormat(mp.getDuration()), mp.getDuration());
                listener.onMediaPrepared();
                listener.scrollToCurrentPlayingMusic(playPosition);
                myHandler.postDelayed(UpdateSongTime, 500);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                listener.onMediaFinished();
                mp.reset();
                dsTextView.get(position).setTextColor(Color.WHITE);
                if (isLoopingOne) {
                    triggerAutoPlay(playPosition);
                } else if (isShuffling && isLooping) {
                    while (true){
                        int newPlayPosition = (int) (100.0 * Math.random() % dsn.size());
                        if (playPosition != newPlayPosition) {
                            playPosition = newPlayPosition;
                            break;
                        }
                    }
                    triggerAutoPlay(playPosition);
                } else if (isShuffling) {
                    while (positionNumber.size() != dsn.size()) {
                        playPosition = (int) (100.0 * Math.random() % dsn.size());
                        if (!positionNumber.contains(playPosition)) {
                            positionNumber.add(playPosition);
                            Log.d("Shuffle: ", positionNumber.toString());
                            triggerAutoPlay(playPosition);
                            break;
                        }
                    }
                } else if (isLooping) {
                    if (playPosition < dsn.size() - 1) {
                        playPosition += 1;
                        triggerAutoPlay(playPosition);
                    } else {
                        playPosition = 0;
                        triggerAutoPlay(playPosition);
                    }
                } else {
                    if (playPosition < dsn.size() - 1) {
                        playPosition += 1;
                        triggerAutoPlay(playPosition);
                    } else {
                        releaseMedia();
                        Log.d("Mediaplayer: ", "Finished list, Media is released!");
                        listener.onMediaReleased();
                    }
                }
                myHandler.removeCallbacks(UpdateSongTime);
            }
        });
        mediaPlayer.prepareAsync();
    }
    public boolean isPlaying(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            int progress = mediaPlayer.getCurrentPosition();
            listener.updateSeekBar(progress);
            listener.updateTimeTextView(AdditionalFunctions.changeTimeFormat(progress));
            if (progress > 5000 && !isViewUpdated){
                DAO_Nhac.updateMusicViewAmount(dsn.get(playPosition));
                isViewUpdated = true;
            }
            myHandler.postDelayed(this, 500);
        }
    };
}
