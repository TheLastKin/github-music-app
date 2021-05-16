package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.DAOClass.DAO_BinhLuan;
import com.example.duan1_nhom2.DialogClass.DialogSuaBinhLuan;
import com.example.duan1_nhom2.DialogClass.DialogXoaBinhLuan;
import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;
import com.example.duan1_nhom2.Model.BinhLuan;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BinhLuan_rvAdapter extends RecyclerView.Adapter<BinhLuan_rvAdapter.BinhLuan_ViewHolder> implements DialogSuaBinhLuan.OnCommentUpdated, DialogXoaBinhLuan.OnCommentDeleted {
    private Context context;
    private ArrayList<BinhLuan> dsbl;
    private ArrayList<NguoiDung> dsnd;
    NhacDangNgheFragment fragment;
    private String userID = "";
    private String maNhac = "";
    public BinhLuan_rvAdapter(Context context, NhacDangNgheFragment fragment){
        this.context = context;
        dsbl = new ArrayList<>();
        dsnd = new ArrayList<>();
        this.fragment = fragment;
        userID = fragment.getUserID();
    }
    @NonNull
    @Override
    public BinhLuan_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BinhLuan_ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_binhluan, parent, false),
                context
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BinhLuan_ViewHolder holder, int position) {
        BinhLuan binhLuan = dsbl.get(position);
        maNhac = binhLuan.getMaNhac();
        DAO_BinhLuan.getUserCommentProfile(context , this, binhLuan.getMaBinhLuan(), binhLuan.getMaNguoiDung(), userID, binhLuan.getBinhLuan(),
                holder.txtTenNguoiDung, holder.txtBinhLuan, holder.ivAvatarNguoiDung, holder.ivTuyChinhBinhLuan);
    }

    @Override
    public int getItemCount() {
        fragment.setTotalComment(dsbl.size());
        return dsbl.size();
    }

    static class BinhLuan_ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenNguoiDung, txtBinhLuan;
        ImageView ivAvatarNguoiDung, ivTuyChinhBinhLuan;
        Context context;
        public BinhLuan_ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            txtTenNguoiDung = itemView.findViewById(R.id.txtTenNguoiDung);
            txtBinhLuan = itemView.findViewById(R.id.txtBinhLuan);
            ivAvatarNguoiDung = itemView.findViewById(R.id.ivAvatarNguoiDung);
            ivTuyChinhBinhLuan = itemView.findViewById(R.id.ivTuyChinhBinhLuan);
        }
    }
    public void updateAdapter(BinhLuan binhLuan){
        dsbl.add(binhLuan);
        notifyDataSetChanged();
    }
    public void clearItems(){
        dsbl.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onCommentUpdated() {
        if (maNhac.isEmpty()) return;
        DAO_BinhLuan.readMusicComments(maNhac, this);
    }

    @Override
    public void onCommentDeleted() {
        if (maNhac.isEmpty()) return;
        DAO_BinhLuan.readMusicComments(maNhac, this);
    }
}
