package com.example.duan1_nhom2.DialogClass;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.DAOClass.DAO_NguoiDung;
import com.example.duan1_nhom2.InterfaceClass.UploadProgressBarTracker;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

public class DialogDoiAnhDaiDien extends DialogFragment implements UploadProgressBarTracker {
    TextView txtXacNhan;
    Button btnChonAnh;
    ImageView ivCloseDialog, ivAnhNguoiDung;
    ProgressBar pbTienDo;
    String userID = "";
    String urlAnh = "";
    Uri uri;
    public DialogDoiAnhDaiDien(String userID, String urlAnh){
        this.userID = userID;
        this.urlAnh = urlAnh;
    }
    public interface OnAvatarUpdated{
        void onAvatarUpdated(String urlAnh);
    }
    OnAvatarUpdated listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_doianh_daidien, container, false);
        findView(view);
        if (!urlAnh.isEmpty()){
            Picasso.with(getContext()).load(urlAnh).into(ivAnhNguoiDung);
        }
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFile();
            }
        });
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO_NguoiDung.changeUserPhoto(uri, getContext(), DialogDoiAnhDaiDien.this, userID, urlAnh);
                lockTextView(true);
            }
        });
        return view;
    }
    private void findView(View view){
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivAnhNguoiDung = view.findViewById(R.id.ivAnhNguoiDung);
        btnChonAnh = view.findViewById(R.id.btnChonAnh);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
        pbTienDo = view.findViewById(R.id.pbTienDo);
    }
    private void chooseImageFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    public void lockTextView(boolean type){
        txtXacNhan.setEnabled(type);
    }
    public void setOnAvatarUpdatedListener(OnAvatarUpdated listener){
        this.listener = listener;
    }
    public void onAvatarUpdated(String urlAnh){
        listener.onAvatarUpdated(urlAnh);
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
            Picasso.with(getContext()).load(uri).into(ivAnhNguoiDung);
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
