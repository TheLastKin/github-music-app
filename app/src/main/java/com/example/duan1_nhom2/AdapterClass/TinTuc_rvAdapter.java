package com.example.duan1_nhom2.AdapterClass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.RSSObject;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

public class TinTuc_rvAdapter extends RecyclerView.Adapter<TinTuc_rvAdapter.News_ViewHolder> {
    RSSObject rssObject;
    Context context;

    public TinTuc_rvAdapter(RSSObject rssObject, Context context) {
        this.rssObject = rssObject;
        this.context = context;
    }

    @NonNull
    @Override
    public News_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_tintuc, parent, false);
        return new News_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull News_ViewHolder holder, int position) {
        holder.txtNewsTitle.setText(rssObject.items.get(position).getTitle());
        holder.txtNewsPublicDate.setText(rssObject.items.get(position).getPubDate());
        Picasso.with(context).load(rssObject.items.get(position).getThumbnail()).into(holder.ivNewsImage);
        holder.txtNewsContent.setText(Html.fromHtml(rssObject.items.get(position).getContent(),Html.FROM_HTML_MODE_COMPACT));
        holder.setOnItemClickListener(new OnItemClickedListener() {
            @Override
            public void onItemClick(View v, int position) {
                ((MainActivity)context).changeToWebTinTucFragment(rssObject.items.get(position).getLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }

    public static class News_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNewsTitle, txtNewsPublicDate, txtNewsContent;
        ImageView ivNewsImage;
        private OnItemClickedListener onItemClickedListener;
        public News_ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivNewsImage = itemView.findViewById(R.id.ivNewsImage);
            txtNewsTitle = itemView.findViewById(R.id.txtNewsTitle);
            txtNewsContent = itemView.findViewById(R.id.txtNewsContent);
            txtNewsPublicDate = itemView.findViewById(R.id.txtNewsPublicDate);
        }
        public void setOnItemClickListener(OnItemClickedListener onItemClickListener){
            this.onItemClickedListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            onItemClickedListener.onItemClick(v,getAdapterPosition());
        }
    }
    public interface OnItemClickedListener{
        void onItemClick(View v, int position);
    }
}
