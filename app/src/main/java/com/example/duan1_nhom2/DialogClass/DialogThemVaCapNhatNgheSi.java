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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_NgheSi;
import com.example.duan1_nhom2.InterfaceClass.UploadProgressBarTracker;
import com.example.duan1_nhom2.Model.NgheSi;
import com.example.duan1_nhom2.R;
import com.squareup.picasso.Picasso;

public class DialogThemVaCapNhatNgheSi extends DialogFragment implements UploadProgressBarTracker {
    EditText txtTenNgheSi, txtThongTinThem, txtThemNgheSi;
    ImageView ivAnhNgheSi, ivCloseDialog, ivHuyUpload;
    TextView txtXacNhan, txtThemVaSuaNgheSi;
    Button btnChonAnh;
    Uri uri = null;
    String fileName = null;
    NgheSi ngheSi;
    ProgressBar pbTienDo;
    boolean type = false;
    public DialogThemVaCapNhatNgheSi(NgheSi ngheSi, boolean type){
        this.ngheSi = ngheSi;
        this.type = type;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_them_capnhat_nghesi, container, false);
        findView(view);
        displayInfo();
        String toDo = type?"Cập Nhật Nghệ Sĩ":"Thêm Nghệ Sĩ";
        txtThemVaSuaNgheSi.setText(toDo);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type){
                    updateArtist();
                }else{
                    uploadArtist();
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

    private void findView(View view) {
        txtTenNgheSi = view.findViewById(R.id.txtTenNgheSi);
        txtThongTinThem = view.findViewById(R.id.txtThongTinThem);
        ivAnhNgheSi = view.findViewById(R.id.ivAnhNgheSi);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        txtThemVaSuaNgheSi = view.findViewById(R.id.txtThemVaSuaNgheSi);
        btnChonAnh = view.findViewById(R.id.btnChonAnh);
        ivHuyUpload = view.findViewById(R.id.ivHuyUpload);
        txtThemNgheSi = view.findViewById(R.id.txtThemNgheSi);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
        pbTienDo = view.findViewById(R.id.pbTienDo);
    }

    private void chooseImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private void displayInfo(){
        if (ngheSi != null && type){
            txtTenNgheSi.setText(ngheSi.getTenNgheSi());
            txtThongTinThem.setText(ngheSi.getThongTinThem());
            if (ngheSi.getURLAnh().equals("NoImage")){
                ivAnhNgheSi.setImageResource(R.drawable.artist_default_icon);
            }else{
                Picasso.with(getContext()).load(ngheSi.getURLAnh()).into(ivAnhNgheSi);
            }
        }
    }
    private void uploadArtist() {
        String tenNgheSi = txtTenNgheSi.getText().toString();
        String thongTinThem = txtThongTinThem.getText().toString();
        if (AdditionalFunctions.isStringEmpty(getContext(), tenNgheSi, thongTinThem)) {
            return;
        }
        NgheSi newNgheSi = new NgheSi(ngheSi.getMaNgheSi(), tenNgheSi, thongTinThem, "", 0);
        DAO_NgheSi.uploadArtist(uri, getContext(), this, newNgheSi, txtThemVaSuaNgheSi, ivHuyUpload);

    }
    private void updateArtist(){
        String tenNgheSi = txtTenNgheSi.getText().toString();
        String thongTinThem = txtThongTinThem.getText().toString();
        String urlAnh = ngheSi.getURLAnh();
        if (AdditionalFunctions.isStringEmpty(getContext(), tenNgheSi, thongTinThem)) {
            return;
        }
        NgheSi newNgheSi = new NgheSi(ngheSi.getMaNgheSi(), tenNgheSi, thongTinThem, urlAnh, 0);
        DAO_NgheSi.updateArist(uri, getContext(), this, newNgheSi, txtThemVaSuaNgheSi, ivHuyUpload);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            this.uri = uri;
            fileName = AdditionalFunctions.getFileName(getContext(), uri);
            Picasso.with(getContext()).load(uri).into(ivAnhNgheSi);
        }
    }

    @Override
    public void updateProgressBar(int progress) {
        pbTienDo.setProgress(progress);
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
