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

import com.example.duan1_nhom2.DAOClass.DAO_BinhLuan;
import com.example.duan1_nhom2.R;

public class DialogXoaBinhLuan extends DialogFragment {
    TextView txtXoaBinhLuan, txtThongBao, txtXacNhan;
    ImageView ivCloseDialog;
    String maBinhLuan = "";
    public DialogXoaBinhLuan(String maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }
    public interface OnCommentDeleted{
        void onCommentDeleted();
    }
    OnCommentDeleted listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_xoa_playlist, container, false);
        findView(view);
        txtXoaBinhLuan.setText("Xóa Bình Luận");
        txtThongBao.setText("Bạn có chắc là muốn xóa bình luận?");
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!maBinhLuan.isEmpty()){
                    DAO_BinhLuan.deleteMusicComment(maBinhLuan, listener);
                    getDialog().dismiss();
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
        txtXoaBinhLuan = view.findViewById(R.id.txtXoaPlaylist);
        txtThongBao = view.findViewById(R.id.txtThongBao);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    public void setOnCommentDeleteListener(OnCommentDeleted listener){
        this.listener = listener;
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
