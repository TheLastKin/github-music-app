package com.example.duan1_nhom2.FragmentClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom2.DialogClass.DialogDoiAnhDaiDien;
import com.example.duan1_nhom2.LoginActivity;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.example.duan1_nhom2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaiDatFragment extends Fragment implements DialogDoiAnhDaiDien.OnAvatarUpdated {
    CircleImageView ivAnhNguoiDung;
    TextView txtTenHienThi, txtEmail, txtDoiMatKhau, txtDoiAnhDaiDien, txtDangXuat;
    NguoiDung nguoiDung;
    public CaiDatFragment(NguoiDung nguoiDung){
        this.nguoiDung = nguoiDung;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caidat, container, false);
        findView(view);
        if (!nguoiDung.getURLAnh().isEmpty()){
            Picasso.with(getContext()).load(nguoiDung.getURLAnh()).into(ivAnhNguoiDung);
            Log.d("PhotoURL:", nguoiDung.getURLAnh());
        }else {
            ivAnhNguoiDung.setImageResource(R.drawable.default_user_icon);
        }
        txtTenHienThi.setText(nguoiDung.getTenHienThi());
        txtEmail.setText(nguoiDung.getEmail());
        txtDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).changeToDoiMatKhauFragment();
            }
        });
        txtDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    ((MainActivity)getContext()).finishMainActivity();
                }
            }
        });
        txtDoiAnhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogDoiAnhDaiDien dialog = new DialogDoiAnhDaiDien(nguoiDung.getMaNguoiDung(), nguoiDung.getURLAnh());
                dialog.setOnAvatarUpdatedListener(CaiDatFragment.this);
                dialog.show(getChildFragmentManager(), "DialogDoiAnhDaiDien");
            }
        });
        hideToolbarActions();
        return view;
    }
    private void hideToolbarActions(){
        MainActivity mainActivity = (MainActivity)getContext();
        mainActivity.setOnClickToolbarAction1(null, R.drawable.ic_search, View.INVISIBLE);
        mainActivity.setOnClickToolbarAction2(null, R.drawable.ic_search, View.INVISIBLE);
    }
    private void findView(View view){
        ivAnhNguoiDung = view.findViewById(R.id.ivAnhNguoiDung);
        txtTenHienThi = view.findViewById(R.id.txtTenHienThi);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtDangXuat = view.findViewById(R.id.txtDangXuat);
        txtDoiAnhDaiDien = view.findViewById(R.id.txtDoiAnhDaiDien);
        txtDoiMatKhau = view.findViewById(R.id.txtDoiMatKhau);
    }

    @Override
    public void onAvatarUpdated(String urlAnh) {
        Picasso.with(getContext()).load(urlAnh).into(ivAnhNguoiDung);
    }
}
