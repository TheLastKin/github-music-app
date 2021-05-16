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

import com.example.duan1_nhom2.AdapterClass.NhacTrangChu_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_NhacPlaylist;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.PlayList;
import com.example.duan1_nhom2.R;

public class ChiTietPlaylistFragment extends Fragment {
    RecyclerView rvPlaylistNhac;
    NhacTrangChu_rvAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    TextView txtTenPlaylist, txtSoBaiHat;
    Button btnPhatTatCa;
    PlayList playList;
    public ChiTietPlaylistFragment(PlayList playList){
        this.playList = playList;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_chitiet, container, false);
        findView(view);
        adapter = new NhacTrangChu_rvAdapter(getContext(), 200);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPlaylistNhac.setLayoutManager(linearLayoutManager);
        rvPlaylistNhac.setAdapter(adapter);
        if (playList != null){
            txtTenPlaylist.setText(playList.getTenPlayList());
        }
        btnPhatTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).changeToNhacDangNgheFragment(adapter.getMusicList());
            }
        });
        DAO_NhacPlaylist.readPlaylistMusic(playList.getMaPlayList(), adapter, txtSoBaiHat);
        return view;
    }
    private void findView(View view){
        rvPlaylistNhac = view.findViewById(R.id.rvPlaylistChiTiet);
        txtTenPlaylist = view.findViewById(R.id.txtTenPlaylist);
        txtSoBaiHat = view.findViewById(R.id.txtSoBaiHat);
        btnPhatTatCa = view.findViewById(R.id.btnPhatTatCa);
    }
}
