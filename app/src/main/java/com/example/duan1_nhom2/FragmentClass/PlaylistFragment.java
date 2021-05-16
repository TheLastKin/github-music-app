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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.AdapterClass.Playlist_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_Playlist;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatPlaylist;
import com.example.duan1_nhom2.DialogClass.DialogXoaPlaylist;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.PlayList;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment implements DialogThemVaCapNhatPlaylist.UpdatePlaylist {
    TextView txtThongBao;
    Button btnTaoPlaylist;
    RecyclerView recyclerView;
    Playlist_rvAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PlayList> dspl;
    String userID = "";

    public PlaylistFragment(String userID) {
        this.userID = userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_nguoidung, container, false);
        findView(view);
        recyclerViewSetUp();
        btnTaoPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userID.isEmpty()){
                    PlayList playList = new PlayList();
                    playList.setMaNguoiDung(userID);
                    DialogThemVaCapNhatPlaylist dialog = new DialogThemVaCapNhatPlaylist(false, playList);
                    dialog.setUpdatePlaylistListener(PlaylistFragment.this);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatPlaylist");
                }
            }
        });
        mainToolBarSetUp();
        DAO_Playlist.readAllUserPlaylist(userID, this);
        return view;
    }
    private void mainToolBarSetUp(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayList playList = new PlayList();
                playList.setMaNguoiDung(userID);
                DialogThemVaCapNhatPlaylist dialog = new DialogThemVaCapNhatPlaylist(false, playList);
                dialog.setUpdatePlaylistListener(PlaylistFragment.this);
                dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatPlaylist");
            }
        };
        ((MainActivity)getContext()).setOnClickToolbarAction1(listener, R.drawable.ic_add, View.VISIBLE);
    }
    private void findView(View view){
        txtThongBao = view.findViewById(R.id.txtThongBao);
        btnTaoPlaylist = view.findViewById(R.id.btnTaoPlaylist);
        recyclerView = view.findViewById(R.id.rvPlaylist);
    }
    private void recyclerViewSetUp(){
        dspl = new ArrayList<>();
        adapter = new Playlist_rvAdapter(getContext(), dspl);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DialogXoaPlaylist dialog = new DialogXoaPlaylist(adapter.getPlaylistAtPosition(viewHolder.getAdapterPosition()).getMaPlayList());
                dialog.setUpdatePlaylistListener(PlaylistFragment.this);
                dialog.show(getChildFragmentManager(), "DialogXoaPlaylist");
            }
        }).attachToRecyclerView(recyclerView);
    }
    public void setNotificationVisibility(boolean type){
        if (type){
            txtThongBao.setVisibility(View.VISIBLE);
            btnTaoPlaylist.setVisibility(View.VISIBLE);
        }else{
            txtThongBao.setVisibility(View.INVISIBLE);
            btnTaoPlaylist.setVisibility(View.INVISIBLE);
        }
    }
    public void setRecyclerViewVisibility(boolean type){
        if (type){
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
    public void updatePlaylistAdapter(PlayList playList){
        adapter.updateAdapter(playList);
    }
    public void resetPlaylist(){
        adapter.resetAdapter();
        DAO_Playlist.readAllUserPlaylist(userID, this);
    }

    @Override
    public void onWorkCompleted() {
        resetPlaylist();
    }
}
