package com.example.duan1_nhom2.DialogClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.AdapterClass.ThemNhacVaoAlbum_Adapter;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class DialogThemNhacVaoAlbum extends DialogFragment implements ThemNhacVaoAlbum_Adapter.ItemSelectedListener {
    AutoCompleteTextView txtTenAlbum;
    ImageView ivCloseDialog;
    TextView txtXacNhan, txtThemNhacVaoAlbum, txtMaAlbum;
    String maNhac = "";
    ThemNhacVaoAlbum_Adapter adapter;
    public DialogThemNhacVaoAlbum(String maNhac){
        this.maNhac = maNhac;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_themnhac_vaoalbum, container, false);
        findView(view);
        adapter = new ThemNhacVaoAlbum_Adapter(getContext(), new ArrayList<Albums>());
        adapter.setListener(this);
        txtTenAlbum.setAdapter(adapter);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maAlbum = txtMaAlbum.getText().toString();
                if (!AdditionalFunctions.isStringEmpty(maAlbum, maNhac)){
                    DAO_Nhac.addMusicToAlbum(maNhac, maAlbum, txtThemNhacVaoAlbum);
                }
            }
        });
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        DAO_Album.readAllAlbums(adapter);
        return view;
    }
    private void findView(View view){
        txtMaAlbum = view.findViewById(R.id.txtMaAlbum);
        txtTenAlbum = view.findViewById(R.id.txtTenAlbum);
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
        txtThemNhacVaoAlbum = view.findViewById(R.id.txtThemNhacVaoAlbum);
    }
    @Override
    public void onItemSelected(String maAlbum) {
        txtMaAlbum.setText(maAlbum);
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
