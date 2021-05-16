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

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_BinhLuan;
import com.example.duan1_nhom2.R;

public class DialogSuaBinhLuan extends DialogFragment {
    ImageView ivCloseDialog;
    TextView txtXacNhan;
    EditText txtBinhLuan;
    String binhLuan = "";
    String maBinhLuan = "";

    public DialogSuaBinhLuan(String maBinhLuan, String binhLuan) {
        this.binhLuan = binhLuan;
        this.maBinhLuan = maBinhLuan;
    }
    public interface OnCommentUpdated{
        void onCommentUpdated();
    }
    OnCommentUpdated listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_sua_binhluan, container, false);
        findView(view);
        if (!binhLuan.isEmpty()){
            txtBinhLuan.setText(binhLuan);
        }
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String binhLuan = txtBinhLuan.getText().toString();
                if (!AdditionalFunctions.isStringEmpty(binhLuan, maBinhLuan)){
                    DAO_BinhLuan.updateMusicComment(maBinhLuan, binhLuan, listener);
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
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        txtBinhLuan = view.findViewById(R.id.txtBinhLuan);
    }
    public void setOnCommentUpdatedListener(OnCommentUpdated listener){
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
