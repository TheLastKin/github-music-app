package com.example.duan1_nhom2.DialogClass;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.InterfaceClass.UploadProgressBarTracker;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

public class DialogThemVaCapNhatAlbum extends DialogFragment implements UploadProgressBarTracker {
    EditText txtTenAlbum, txtSoBaiHat;
    AutoCompleteTextView txtTenNgheSi;
    TextView txtThemVaSuaAlbum, txtXacNhan;
    ImageView ivAnhAlbum, ivCloseDialog, ivHuyUpload;
    Button btnChonAnh;
    Uri uri = null;
    Albums albums;
    ProgressBar pbTienDo;
    boolean type = false;
    public DialogThemVaCapNhatAlbum(Albums albums, boolean type){
        this.albums = albums;
        this.type = type;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_them_capnhat_album, container, false);
        findView(view);
        displayInfo();
        String toDo = type?"Cập Nhật Album":"Thêm Album";
        txtThemVaSuaAlbum.setText(toDo);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type){
                    updateAlbum();
                }else{
                    uploadAlbum();
                }
            }
        });
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFile();
            }
        });
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        pbTienDo.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.emberColor, null), android.graphics.PorterDuff.Mode.SRC_IN);
        return view;
    }
    private void findView(View view){
        txtTenAlbum = view.findViewById(R.id.txtTenAlbum);
        txtTenNgheSi = view.findViewById(R.id.txtTenNgheSi);
        txtThemVaSuaAlbum = view.findViewById(R.id.txtThemVaSuaAlbum);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        txtSoBaiHat = view.findViewById(R.id.txtSoBaiHat);
        ivAnhAlbum = view.findViewById(R.id.ivAnhAlbum);
        btnChonAnh = view.findViewById(R.id.btnChonAnh);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
        ivHuyUpload = view.findViewById(R.id.ivHuyUpload);
        pbTienDo = view.findViewById(R.id.pbTienDo);
    }
    private void displayInfo(){
        if (albums != null && type){
            txtTenAlbum.setText(albums.getTenAlbum());
            txtSoBaiHat.setText(String.valueOf(albums.getSoBaiHat()));
            txtTenNgheSi.setText(albums.getTenNgheSi());
            if (albums.getURLAnh().equals("NoImage")){
                ivAnhAlbum.setImageResource(R.drawable.album_default_icon);
            }else{
                Picasso.with(getContext()).load(albums.getURLAnh()).into(ivAnhAlbum);
            }
        }
    }
    private void chooseImageFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private void uploadAlbum(){
        String tenAlbum = txtTenAlbum.getText().toString();
        String tenNgheSi = txtTenNgheSi.getText().toString();
        String soBaiHat = txtSoBaiHat.getText().toString();
        if (AdditionalFunctions.isStringEmpty(getContext(), tenAlbum, tenNgheSi, soBaiHat)){
            return;
        }
        int soBaiHat1 = Integer.parseInt(soBaiHat);
        Albums albums = new Albums("", tenAlbum, tenNgheSi, soBaiHat1, "", 0, 0);
        DAO_Album.uploadAlbum(uri, getContext(),this, albums, txtThemVaSuaAlbum, ivHuyUpload);
    }
    private void updateAlbum(){
        String maAlbum = albums.getMaAlbum();
        String tenAlbum = txtTenAlbum.getText().toString();
        String tenNgheSi = txtTenNgheSi.getText().toString();
        String soBaiHat = txtSoBaiHat.getText().toString();
        String urlAnh = albums.getURLAnh();
        if (AdditionalFunctions.isStringEmpty(getContext(), tenAlbum, tenNgheSi, soBaiHat)){
            return;
        }
        int soBaiHat1 = Integer.parseInt(soBaiHat);
        Albums albums = new Albums(maAlbum, tenAlbum, tenNgheSi, soBaiHat1, urlAnh, 0, 0);
        DAO_Album.updateAlbum(uri, getContext(), this, albums, txtThemVaSuaAlbum, ivHuyUpload);
    }

    @Override
    public void updateProgressBar(int progress) {
        pbTienDo.setProgress(progress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            this.uri = uri;
            Picasso.with(getContext()).load(uri).into(ivAnhAlbum);
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
