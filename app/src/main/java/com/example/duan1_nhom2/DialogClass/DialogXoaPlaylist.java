package com.example.duan1_nhom2.DialogClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.DAOClass.DAO_Playlist;
import com.example.duan1_nhom2.R;

public class DialogXoaPlaylist extends DialogFragment {
    TextView txtXoaPlaylist, txtXacNhan, txtTenPlaylist;
    ImageView ivCloseDialog;
    String maPlaylist = "";
    public DialogXoaPlaylist(String maPlaylist){
        this.maPlaylist = maPlaylist;
    }
    DialogThemVaCapNhatPlaylist.UpdatePlaylist listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_xoa_playlist, container, false);
        findView(view);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlaylist();
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
        txtTenPlaylist = view.findViewById(R.id.txtTenPlaylist);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        txtXoaPlaylist = view.findViewById(R.id.txtXoaPlaylist);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    public void setUpdatePlaylistListener(DialogThemVaCapNhatPlaylist.UpdatePlaylist listener){
        this.listener = listener;
    }
    private void deletePlaylist(){
        if (!maPlaylist.isEmpty() && listener != null){
            DAO_Playlist.deletePlaylist(maPlaylist, listener);
            getDialog().dismiss();
        }
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
