package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class TimKiemNhac_rvAdapter extends RecyclerView.Adapter<TimKiemNhac_rvAdapter.TimKiemNhac_ViewHolder>{
    private Context context;
    private ArrayList<Nhac> dsn;
    private int maxCount;
    public TimKiemNhac_rvAdapter(Context context, int maxCount){
        this.context = context;
        this.dsn = new ArrayList<>();
        this.maxCount = maxCount;
    }

    @NonNull
    @Override
    public TimKiemNhac_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimKiemNhac_ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_timkiem_nhac, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiemNhac_ViewHolder holder, int position) {
        final Nhac nhac = dsn.get(position);
        holder.txtTenNhac.setText(nhac.getTenNhac());
        holder.txtTenNgheSi.setText(nhac.getTenNgheSi());
        holder.txtThoiLuong.setText(nhac.getThoiLuong());
        holder.ivMusicIcon.setImageResource(R.drawable.music_default_icon);
        holder.txtTenNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Nhac> newDSN = new ArrayList<>();
                newDSN.add(nhac);
                ((MainActivity)context).changeToNhacDangNgheFragment(newDSN);
            }
        });
        holder.ivThemVaoDanhSachDangNghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).queueSongToCurrentPlaylist(nhac);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dsn.size()<=maxCount){
            return dsn.size();
        }else {
            return maxCount;
        }
    }

    static class TimKiemNhac_ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenNhac, txtTenNgheSi, txtThoiLuong;
        ImageView ivMusicIcon, ivThemVaoDanhSachDangNghe;
        public TimKiemNhac_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenNhac = itemView.findViewById(R.id.txtTenNhac);
            txtTenNgheSi = itemView.findViewById(R.id.txtTenNgheSi);
            txtThoiLuong = itemView.findViewById(R.id.txtThoiLuong);
            ivMusicIcon = itemView.findViewById(R.id.ivMusicIcon);
            ivThemVaoDanhSachDangNghe = itemView.findViewById(R.id.ivThemVaoDanhSachDangNghe);
        }
    }
    public void updateAdapter(ArrayList<Nhac> dsn){
        this.dsn.clear();
        this.dsn.addAll(dsn);
        notifyDataSetChanged();
    }
    public void resetAdapter(){
        this.dsn.clear();
        notifyDataSetChanged();
    }
    public void setMusicList(ArrayList<Nhac> dsn){
        this.dsn = dsn;
        notifyDataSetChanged();
    }
    public ArrayList<Nhac> getMusicList(){
        return dsn;
    }
}
