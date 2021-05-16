package com.example.duan1_nhom2.FragmentClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.AdapterClass.AlbumTrangChu_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.ChudeTrangchu_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.NhacTrangChu_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.DAOClass.DAO_TheLoai;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class TrangChuFragment extends Fragment {
    RecyclerView rvChuDeTrangChu, rvAlbumTrangChu, rvNhacTrangChu;
    ChudeTrangchu_rvAdapter adapterChuDe;
    AlbumTrangChu_rvAdapter adapterAlbum;
    NhacTrangChu_rvAdapter adapterNhac;
    LinearLayoutManager llm1, llm2, llm3;
    TextView txtPhatTatCa;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findView(view);
        recyclerViewSetUp();
        txtPhatTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Nhac> dsn = adapterNhac.getMusicList();
                if (dsn.size()>0){
                    adapterNhac.releaseMedia();
                    ((MainActivity)getContext()).changeToNhacDangNgheFragment(dsn);
                }
            }
        });
        return view;
    }
    private void findView(View view){
        rvNhacTrangChu = view.findViewById(R.id.rvNhacTrangChu);
        rvChuDeTrangChu = view.findViewById(R.id.rvChuDeTrangChu);
        rvAlbumTrangChu = view.findViewById(R.id.rvAlbumsTrangChu);
        txtPhatTatCa = view.findViewById(R.id.txtPhatTatCa);
    }
    private void recyclerViewSetUp(){
        adapterChuDe = new ChudeTrangchu_rvAdapter(getContext(), 7);
        adapterAlbum = new AlbumTrangChu_rvAdapter(getContext(), 10);
        adapterNhac = new NhacTrangChu_rvAdapter(getContext(), 7);
        llm1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
        llm2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        llm3 = new LinearLayoutManager(getContext());
        llm3.setReverseLayout(true);
        rvChuDeTrangChu.setLayoutManager(llm1);
        rvAlbumTrangChu.setLayoutManager(llm2);
        rvNhacTrangChu.setLayoutManager(llm3);
        rvChuDeTrangChu.setAdapter(adapterChuDe);
        rvAlbumTrangChu.setAdapter(adapterAlbum);
        rvNhacTrangChu.setAdapter(adapterNhac);
        DAO_Nhac.readPopularMusic( 7,adapterNhac);
        DAO_TheLoai.readPopularCategory(7, adapterChuDe);
        DAO_Album.readPopularAlbum(10, adapterAlbum);
    }
}
