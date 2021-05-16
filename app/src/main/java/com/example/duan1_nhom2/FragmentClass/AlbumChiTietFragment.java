package com.example.duan1_nhom2.FragmentClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.AdapterClass.NhacTrangChu_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.DAOClass.DAO_NgheSi;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatAlbum;
import com.example.duan1_nhom2.DialogClass.DialogXoaDuLieu;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumChiTietFragment extends Fragment {
    ImageView ivAnhAlbum;
    RecyclerView rvNhacAlbum;
    NhacTrangChu_rvAdapter adapter;
    LinearLayoutManager layoutManager;
    Button btnPhatTatCa;
    TextView txtTenAlbum, txtTenNgheSi;
    Albums albums;

    public AlbumChiTietFragment(Albums albums) {
        this.albums = albums;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_chitiet, container, false);
        findView(view);
        if (albums != null){
            txtTenAlbum.setText(albums.getTenAlbum());
            txtTenNgheSi.setText(albums.getTenNgheSi());
            String urlAnh = albums.getURLAnh();
            if (urlAnh.equals("NoImage")){
                ivAnhAlbum.setImageResource(R.drawable.album_default_icon);
            }else {
                Picasso.with(getContext()).load(albums.getURLAnh()).into(ivAnhAlbum);
            }
        }
        adapter = new NhacTrangChu_rvAdapter(getContext(), 100);
        layoutManager = new LinearLayoutManager(getContext());
        rvNhacAlbum.setLayoutManager(layoutManager);
        rvNhacAlbum.setAdapter(adapter);
        DAO_Album.readAlbumSongs(albums.getMaAlbum(), adapter);
        txtTenNgheSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO_NgheSi.readAritistAndChangeFragment(txtTenNgheSi.getText().toString(), getContext());
            }
        });
        btnPhatTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Nhac> dsn = adapter.getMusicList();
                if (dsn.size()>0) {
                    ((MainActivity) getContext()).changeToNhacDangNgheFragment(dsn);
                }
            }
        });
        ifUserIsAnAdmin();
        return view;
    }
    private void ifUserIsAnAdmin(){
        MainActivity mainActivity = (MainActivity)getContext();
        if (mainActivity.isUserAnAdmin()){
            View.OnClickListener listener1 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogXoaDuLieu dialog = new DialogXoaDuLieu(albums.getMaAlbum(), albums.getURLAnh(), albums.getTenAlbum(), albums.getTenNgheSi());
                    dialog.setType(2);
                    dialog.show(getChildFragmentManager(), "DialogXoaDuLieu");
                }
            };
            mainActivity.setOnClickToolbarAction1(listener1, R.drawable.ic_delete, View.VISIBLE);
            View.OnClickListener listener2 = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogThemVaCapNhatAlbum dialog = new DialogThemVaCapNhatAlbum(albums, true);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatAlbum");
                }
            };
            mainActivity.setOnClickToolbarAction2(listener2, R.drawable.ic_edit, View.VISIBLE);
        }
    }
    private void findView(View view){
        rvNhacAlbum = view.findViewById(R.id.rvNhacAlbum);
        btnPhatTatCa = view.findViewById(R.id.btnPhatTatCa);
        txtTenAlbum = view.findViewById(R.id.txtTenAlbum);
        txtTenNgheSi = view.findViewById(R.id.txtTenNgheSi);
        ivAnhAlbum = view.findViewById(R.id.ivAnhAlbum);
    }
}
