package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.DAOClass.DAO_NgheSi;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.NgheSi;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimKiemNgheSi_rvAdapter extends RecyclerView.Adapter<TimKiemNgheSi_rvAdapter.TimKiemNgheSi_ViewHolder>{
    private Context context;
    private ArrayList<NgheSi> dsns;
    private int maxCount = 0;

    public TimKiemNgheSi_rvAdapter(Context context, int maxCount) {
        this.context = context;
        this.dsns = new ArrayList<>();
        this.maxCount = maxCount;
    }

    @NonNull
    @Override
    public TimKiemNgheSi_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimKiemNgheSi_ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_timkiem_nghesi, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiemNgheSi_ViewHolder holder, int position) {
        final NgheSi ngheSi = dsns.get(position);
        holder.txtTenNgheSi.setText(ngheSi.getTenNgheSi());
        String urlAnh = ngheSi.getURLAnh();
        if (!urlAnh.isEmpty()){
            Picasso.with(context).load(urlAnh).into(holder.ivArtistIcon);
        }else{
            holder.ivArtistIcon.setImageResource(R.drawable.artist_default_icon);
        }
        holder.txtTenNgheSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).changeToNgheSiFragment(ngheSi);
                DAO_NgheSi.updateArtistViewAmount(ngheSi.getMaNgheSi(), ngheSi.getLuotXem());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dsns.size()<maxCount){
            return dsns.size();
        }else{
            return maxCount;
        }
    }

    static class TimKiemNgheSi_ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenNgheSi;
        ImageView ivArtistIcon;
        public TimKiemNgheSi_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenNgheSi = itemView.findViewById(R.id.txtTenNgheSi);
            ivArtistIcon = itemView.findViewById(R.id.ivArtistIcon);
        }
    }
    public void updateAdapter(NgheSi ngheSi){
        dsns.add(ngheSi);
        notifyDataSetChanged();
    }
    public void resetAdapter(){
        dsns.clear();
        notifyDataSetChanged();
    }
    public ArrayList<NgheSi> getArtistList(){
        return dsns;
    }
}
