package com.example.duan1_nhom2.DialogClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.DAOClass.DAO_NhacPlaylist;
import com.example.duan1_nhom2.DAOClass.DAO_Playlist;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.Model.PlayList;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class DialogThemVaoPlaylist extends DialogFragment {
    Spinner spnDanhSachPlaylist;
    TextView txtXacNhan;
    String userID = "";
    ImageView ivCloseDialog;
    PlayList playList;
    Nhac nhac;
    public DialogThemVaoPlaylist(String userID, Nhac nhac){
        this.userID = userID;
        this.nhac = nhac;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_themvao_playlist, container, false);
        findView(view);
        if (!userID.isEmpty()){
            DAO_Playlist.readAllUserPlaylist(userID, this);
        }
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playList != null && nhac != null && !userID.isEmpty()){
                    DAO_NhacPlaylist.uploadPlaylistMusic(userID, nhac.getMaNhac(), playList.getMaPlayList(), playList.getSoBaiHat(),
                            getContext());
                    getDialog().dismiss();
                }else{
                    Toast.makeText(getContext(), "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }
    private void findView(View view){
        spnDanhSachPlaylist = view.findViewById(R.id.spnDanhSachPlaylist);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    public void spinnerSetUp(ArrayList<PlayList> dspl){
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item_layout, dspl);
        spnDanhSachPlaylist.setAdapter(adapter);
        spnDanhSachPlaylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playList = (PlayList) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams)layoutParams);
    }

}
