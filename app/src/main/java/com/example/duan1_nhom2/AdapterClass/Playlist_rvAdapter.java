package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.PlayList;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class Playlist_rvAdapter extends RecyclerView.Adapter<Playlist_rvAdapter.Playlist_ViewHolder>{
    private Context context;
    private ArrayList<PlayList> dspl;

    public Playlist_rvAdapter(Context context, ArrayList<PlayList> dspl) {
        this.context = context;
        this.dspl = dspl;
    }

    @NonNull
    @Override
    public Playlist_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Playlist_ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_playlist, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull Playlist_ViewHolder holder, int position) {
        final PlayList playList = dspl.get(position);
        holder.txtTenPlaylist.setText(playList.getTenPlayList());
        holder.ivPlaylistBackground.setImageResource(generateBackground());
        holder.txtTenPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).changeToPlaylistChiTietFragment(playList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dspl.size();
    }

    static class Playlist_ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenPlaylist;
        ImageView ivPlaylistBackground;

        public Playlist_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenPlaylist = itemView.findViewById(R.id.txtTenPlaylist);
            ivPlaylistBackground = itemView.findViewById(R.id.ivPlaylistBackground);
        }
    }
    public void updateAdapter(PlayList playList){
        dspl.add(playList);
        notifyDataSetChanged();
    }
    public void resetAdapter(){
        dspl.clear();
        notifyDataSetChanged();
    }
    public PlayList getPlaylistAtPosition(int position){
        return dspl.get(position);
    }
    private int generateBackground(){
        int type = (int) ((Math.random()*10%4)+1);
        switch (type){
            case 1:{
                return R.drawable.playlist_background_1;
            }
            case 2:{
                return R.drawable.playlist_background_2;
            }
            case 3:{
                return R.drawable.playlist_background_3;
            }
            case 4:{
                return R.drawable.playlist_background_4;
            }
            default:{
                return R.drawable.playlist_background_1;
            }
        }
    }
}
