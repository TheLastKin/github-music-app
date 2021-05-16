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

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.DAOClass.DAO_BinhLuan;
import com.example.duan1_nhom2.DAOClass.DAO_NgheSi;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.DAOClass.DAO_Playlist;
import com.example.duan1_nhom2.DAOClass.DAO_TheLoai;
import com.example.duan1_nhom2.R;

public class DialogXoaDuLieu extends DialogFragment {
    TextView txtThongBao, txtXoaDuLieu, txtThongTin1, txtThongTin2, txtXacNhan;
    ImageView ivCloseDialog;
    String thongTin1 = "";
    String thongTin2 = "";
    String modelId = "";
    String modelURL = "";
    int type = 0;
    public DialogXoaDuLieu(String modelId, String modelURL, String thongTin1, String thongTin2) {
        this.modelId = modelId;
        this.modelURL = modelURL;
        this.thongTin1 = thongTin1;
        this.thongTin2 = thongTin2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_xoa_dulieu, container, false);
        findView(view);
        adaptiveDisplayInfo();
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaptiveDeletion();
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
        txtThongBao = view.findViewById(R.id.txtThongBao);
        txtXoaDuLieu = view.findViewById(R.id.txtXoaDuLieu);
        txtThongTin1 = view.findViewById(R.id.txtThongTin1);
        txtThongTin2 = view.findViewById(R.id.txtThongTin2);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    private void adaptiveDisplayInfo(){
        switch (type){
            case 1:{
                txtXoaDuLieu.setText("Xóa Nhạc");
                break;
            }
            case 2:{
                txtXoaDuLieu.setText("Xóa Album");
                break;
            }
            case 3:{
                txtXoaDuLieu.setText("Xóa Nghệ Sĩ");
                break;
            }
            case 4:{
                txtXoaDuLieu.setText("Xóa Thể Loại");
                break;
            }
            default:{
                txtXoaDuLieu.setText("Xóa Dữ Liệu");
            }
        }
        if (!AdditionalFunctions.isStringEmpty(thongTin1, thongTin2)){
            txtThongTin1.setText(thongTin1);
            txtThongTin2.setText(thongTin2);
        }
    }
    private void adaptiveDeletion(){
        switch (type){
            case 1:{
                if (!modelId.isEmpty()) DAO_Nhac.deleteMusic(modelId, modelURL, txtXoaDuLieu);
                break;
            }
            case 2:{
                if (AdditionalFunctions.isStringEmpty(modelId, modelURL)) DAO_Album.deleteAlbum(modelId, modelURL, txtXoaDuLieu);
                break;
            }
            case 3:{
                if (AdditionalFunctions.isStringEmpty(modelId, modelURL)) DAO_NgheSi.deleteArtist(modelId, modelURL, txtXoaDuLieu);
                break;
            }
            case 4:{
                if (!modelId.isEmpty()) DAO_TheLoai.deleteCategory(modelId, txtXoaDuLieu);
                break;
            }
        }
    }
    public void setType(int type){
        this.type = type;
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
