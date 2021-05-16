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
import com.example.duan1_nhom2.DAOClass.DAO_TheLoai;
import com.example.duan1_nhom2.Model.TheLoai;
import com.example.duan1_nhom2.R;

public class DialogThemVaCapNhatTheLoai extends DialogFragment {
    TextView txtThemVaSuaTheLoai, txtXacNhan;
    EditText txtTenTheLoai;
    ImageView ivCloseDialog;
    TheLoai theLoai;
    boolean type;
    public DialogThemVaCapNhatTheLoai(TheLoai theLoai, boolean type){
        this.theLoai = theLoai;
        this.type = type;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_them_capnhat_theloainhac, container, false);
        findView(view);
        displayInfo();
        String toDo = type?"Cập Nhật Thể Loại":"Thêm Thể Loại";
        txtThemVaSuaTheLoai.setText(toDo);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadOrUpdateCategory();
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
        txtThemVaSuaTheLoai = view.findViewById(R.id.txtThemVaSuaTheLoai);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        txtTenTheLoai = view.findViewById(R.id.txtTenTheLoai);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    private void uploadOrUpdateCategory(){
        String tenTheLoai = txtTenTheLoai.getText().toString();
        if (AdditionalFunctions.isStringEmpty(getContext(),tenTheLoai)){
            return;
        }
        if (type){
            DAO_TheLoai.updateCategory(theLoai.getMaTheLoai(), tenTheLoai, txtThemVaSuaTheLoai);
        }else{
            DAO_TheLoai.uploadCategory(tenTheLoai, txtThemVaSuaTheLoai);
        }
    }
    private void displayInfo(){
        if (theLoai != null && type){
            txtTenTheLoai.setText(theLoai.getTenTheLoai());
        }
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
