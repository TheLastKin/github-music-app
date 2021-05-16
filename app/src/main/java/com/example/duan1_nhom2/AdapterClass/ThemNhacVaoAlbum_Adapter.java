package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ThemNhacVaoAlbum_Adapter extends ArrayAdapter<Albums> {
    private ArrayList<Albums> dsalbums;

    public ThemNhacVaoAlbum_Adapter(@NonNull Context context, @NonNull List<Albums> dsalbums) {
        super(context, 0, dsalbums);
        this.dsalbums = new ArrayList<>();
    }

    public interface ItemSelectedListener{
        void onItemSelected(String maAlbum);
    }
    ItemSelectedListener listener;

    public void setListener(ItemSelectedListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_themnhac_vaoalbum, parent, false);
        }
        TextView txtTenAlbum = convertView.findViewById(R.id.txtTenAlbum);
        TextView txtTenNgheSi = convertView.findViewById(R.id.txtTenNgheSi);
        ImageView ivAnhAlbum = convertView.findViewById(R.id.ivAnhAlbum);
        Albums album = getItem(position);
        if (album != null){
            txtTenAlbum.setText(album.getTenAlbum());
            txtTenNgheSi.setText(album.getTenNgheSi());
            if (album.getURLAnh().equals("NoImage")){
                ivAnhAlbum.setImageResource(R.drawable.album_default_icon);
            }else{
                Picasso.with(getContext()).load(album.getURLAnh()).into(ivAnhAlbum);
            }
        }

        return convertView;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Albums> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                suggestions.addAll(dsalbums);
            }else{
                String filterPattern = constraint.toString().toLowerCase();
                for (Albums albums: dsalbums){
                    if (albums.getTenAlbum().toLowerCase().contains(filterPattern)){
                        suggestions.add(albums);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            listener.onItemSelected(((Albums)resultValue).getMaAlbum());
            return ((Albums)resultValue).getTenAlbum();
        }
    };
    public void updateAdapter(Albums albums){
        dsalbums.add(albums);
        notifyDataSetChanged();
    }
}
