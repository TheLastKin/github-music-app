package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.FragmentClass.AlbumChiTietFragment;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumTrangChu_rvAdapter extends RecyclerView.Adapter<AlbumTrangChu_rvAdapter.ViewHolder> {
    ArrayList<Albums> dsalbums;
    Context context;
    int maxCount = 0;

    public AlbumTrangChu_rvAdapter(Context context, int maxCount) {
        this.dsalbums = new ArrayList<>();
        this.context = context;
        this.maxCount = maxCount;
    }

    @NonNull
    @Override
    public AlbumTrangChu_rvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumTrangChu_rvAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_album, parent, false)
        );

    }

    @Override
    public void onBindViewHolder(@NonNull AlbumTrangChu_rvAdapter.ViewHolder holder, int position) {
        final Albums albums= dsalbums.get(position);
        holder.txtTenAlbum.setText(albums.getTenAlbum());
        holder.txtTenNgheSi.setText(albums.getTenNgheSi());
        if (albums.getURLAnh().equals("NoImage")){
            holder.ivAnhAlbum.setImageResource(R.drawable.album_default_icon);
        }else{
            Picasso.with(context).load(albums.getURLAnh()).into(holder.ivAnhAlbum);
        }
        holder.cvAlbumTrangChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).changeToAlbumChiTietFragment(albums);
                DAO_Album.updateAlbumViewAmount(albums);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dsalbums.size()<maxCount){
            return dsalbums.size();
        }else{
            return maxCount;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenAlbum, txtTenNgheSi;
        ImageView ivAnhAlbum;
        CardView cvAlbumTrangChu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenAlbum = itemView.findViewById(R.id.txtTenAlbum);
            txtTenNgheSi = itemView.findViewById(R.id.txtTenNgheSi);
            ivAnhAlbum = itemView.findViewById(R.id.ivAnhAlbum);
            cvAlbumTrangChu = itemView.findViewById(R.id.cvAlbumTrangChu);
        }
    }
    public void updateAdapter(ArrayList<Albums> dsalbums){
        this.dsalbums.clear();
        this.dsalbums.addAll(dsalbums);
        notifyDataSetChanged();
    }
    public void resetAdapter(){
        dsalbums.clear();
        notifyDataSetChanged();
    }
    public ArrayList<Albums> getAlbumList(){
        return dsalbums;
    }
}
