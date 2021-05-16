package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;
import java.util.HashMap;

public class NhacTrangChu_rvAdapter extends RecyclerView.Adapter<NhacTrangChu_rvAdapter.ViewHolder> {

    ArrayList<Nhac> dsn;
    Context context;
    int maxCount = 0;
    int playPosition = -1;
    MediaPlayer mediaPlayer;
    HashMap<Integer, ImageView> dsImageViews;

    public NhacTrangChu_rvAdapter(Context context, int maxCount) {
        this.dsn = new ArrayList<>();
        this.context = context;
        this.maxCount = maxCount;
        dsImageViews = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NhacTrangChu_rvAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_nhac, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Nhac nhac = dsn.get(position);
        dsImageViews.put(position, holder.ivPhatNhac);
        holder.txtTenBaiHat.setText(nhac.getTenNhac());
        holder.txtTenNgheSi.setText(nhac.getTenNgheSi());
        holder.txtThoiLuong.setText(nhac.getThoiLuong());
        holder.txtTenBaiHat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseMedia();
                ArrayList<Nhac> newDSN = new ArrayList<>();
                newDSN.add(nhac);
                ((MainActivity) context).changeToNhacDangNgheFragment(newDSN);
            }
        });
        holder.ivPhatNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    playPosition = position;
                    playMusic(playPosition);
                } else if (playPosition != position) {
                    playPosition = position;
                    playMusic(playPosition);
                } else {
                    playAndPauseMusic(holder.ivPhatNhac);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dsn.size() < maxCount) {
            return dsn.size();
        } else {
            return maxCount;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenBaiHat, txtTenNgheSi, txtThoiLuong;
        ImageView ivPhatNhac;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenBaiHat = itemView.findViewById(R.id.txtTenBaiHat);
            txtTenNgheSi = itemView.findViewById(R.id.txtTenNgheSi);
            txtThoiLuong = itemView.findViewById(R.id.txtThoiLuong);
            ivPhatNhac = itemView.findViewById(R.id.ivPhatNhac);
        }
    }

    public void updateAdapter(ArrayList<Nhac> nhac) {
        dsn.clear();
        dsn.addAll(nhac);
        notifyDataSetChanged();
    }

    public void releaseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void playMusic(int position) {
        releaseMedia();
        for (Integer i: dsImageViews.keySet()){
            if (playPosition != i){
                dsImageViews.get(i).setImageResource(R.drawable.ic_play_arrow_red);
            }else{
                dsImageViews.get(i).setImageResource(R.drawable.ic_pause_red);
            }
        }
        mediaPlayer = DAO_Nhac.createMediaPlayer(dsn.get(position));
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMedia();
            }
        });
        mediaPlayer.prepareAsync();
    }

    public void playAndPauseMusic(ImageView ivPlayAndPause) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                ivPlayAndPause.setImageResource(R.drawable.ic_play_arrow_red);
                mediaPlayer.pause();
            } else {
                ivPlayAndPause.setImageResource(R.drawable.ic_pause_red);
                mediaPlayer.start();
            }
        }
    }
    public void updateAdapter(Nhac nhac){
        dsn.add(nhac);
        notifyDataSetChanged();
    }
    public ArrayList<Nhac> getMusicList() {
        return dsn;
    }
}
