package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.DAOClass.DAO_TheLoai;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.TheLoai;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChudeTrangchu_rvAdapter extends RecyclerView.Adapter<ChudeTrangchu_rvAdapter.ViewHolder> {
    ArrayList<TheLoai> dscd;
    Context context;
    int maxCount = 0;

    public ChudeTrangchu_rvAdapter(Context context, int maxCount) {
        this.dscd = new ArrayList<>();
        this.context = context;
        this.maxCount = maxCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChudeTrangchu_rvAdapter.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.adapter_chude_trangchu, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final TheLoai theLoai= dscd.get(position);
        holder.txtTenchude.setText(theLoai.getTenTheLoai());
        holder.ivCategoryIcon.setImageResource(R.drawable.android_hiphop);
        holder.txtTenchude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).changeToNhacTheoChuDeFragment(theLoai);
                DAO_TheLoai.updateMonthlyViewAmount(theLoai.getMaTheLoai(), theLoai.getLuotXemThang());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dscd.size()<maxCount){
            return dscd.size();
        }else{
            return maxCount;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenchude;
        CircleImageView ivCategoryIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenchude= itemView.findViewById(R.id.txtTenChuDe);
            ivCategoryIcon= itemView.findViewById(R.id.ivCategoryIcon);
        }
    }
    public void updateAdapter(TheLoai theLoai){
        dscd.add(theLoai);
        notifyDataSetChanged();
    }
    public void resetAdapter(){
        dscd.clear();
        notifyDataSetChanged();
    }
    public ArrayList<TheLoai> getCategoryList(){
        return dscd;
    }
}
