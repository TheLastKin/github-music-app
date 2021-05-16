package com.example.duan1_nhom2.DialogClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.DAOClass.DAO_Admin;
import com.example.duan1_nhom2.R;

public class DialogResetViewThang extends DialogFragment {
    TextView txtXoaDuLieu, txtThongBao, txtThongTin1, txtThongTin2, txtXacNhan;
    ImageView ivCloseDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_xoa_dulieu, container, false);
        findView(view);
        txtXoaDuLieu.setText("Reset View Tháng");
        txtThongBao.setText("Bạn có chắc rằng muốn reset view tháng?");
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAO_Admin.resetMonthlyViewAmount(txtThongBao, txtThongTin1, txtThongTin2);
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
        txtThongTin1 = view.findViewById(R.id.txtThongTin1);
        txtThongTin2 = view.findViewById(R.id.txtThongTin2);
        txtXoaDuLieu = view.findViewById(R.id.txtXoaDuLieu);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
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
