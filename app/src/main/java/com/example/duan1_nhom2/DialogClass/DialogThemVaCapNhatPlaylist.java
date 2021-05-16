package com.example.duan1_nhom2.DialogClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.AdapterClass.Playlist_rvAdapter;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_Playlist;
import com.example.duan1_nhom2.Model.PlayList;
import com.example.duan1_nhom2.R;

public class DialogThemVaCapNhatPlaylist extends DialogFragment {
    TextView txtTaoVaCapNhatPlaylist, txtXacNhan;
    EditText txtTenPlaylist;
    ImageView ivCloseDialog;
    boolean type = false;
    PlayList playList;
    public DialogThemVaCapNhatPlaylist(boolean type, PlayList playList) {
        this.type = type;
        this.playList = playList;
    }
    public interface UpdatePlaylist{
        void onWorkCompleted();
    }
    UpdatePlaylist listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_them_capnhat_playlist, container, false);
        findView(view);
        displayInfo();
        String toDo = type?"Đổi Tên Playlist":"Tạo Playlist";
        txtTaoVaCapNhatPlaylist.setText(toDo);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrUpdatePlaylist();
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
        txtTaoVaCapNhatPlaylist = view.findViewById(R.id.txtTaoVaCapNhatPlaylist);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        txtTenPlaylist = view.findViewById(R.id.txtTenPlaylist);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    private void displayInfo(){
        if (type && playList != null){
            txtTenPlaylist.setText(playList.getTenPlayList());
        }
    }
    private void createOrUpdatePlaylist(){
        String tenPlaylist = txtTenPlaylist.getText().toString();
        if (AdditionalFunctions.isStringEmpty(getContext(), tenPlaylist, playList.getMaNguoiDung())){
            return;
        }
        if (type){
            DAO_Playlist.changePlaylistName(playList.getMaPlayList(), tenPlaylist, listener);
        }else{
            DAO_Playlist.createPlaylist(playList.getMaNguoiDung(), tenPlaylist, listener);
        }
    }
    public void setUpdatePlaylistListener(UpdatePlaylist listener){
        this.listener = listener;
    }
    public void closeDialog(View view){
        getDialog().dismiss();
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
