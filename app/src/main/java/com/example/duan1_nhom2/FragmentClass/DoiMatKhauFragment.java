package com.example.duan1_nhom2.FragmentClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom2.R;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhauFragment extends Fragment {
    EditText txtMatKhauCu, txtMatKhauMoi, txtNhapLaiMatKhauMoi;
    Button btnDoiMatKhau;
    FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doimatkhau, container, false);
        findView(view);
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        return view;
    }
    private void findView(View view){
        txtMatKhauCu = view.findViewById(R.id.txtMatKhauCu);
        txtMatKhauMoi = view.findViewById(R.id.txtMatKhauMoi);
        txtNhapLaiMatKhauMoi = view.findViewById(R.id.txtNhapLaiMatKhauMoi);
        btnDoiMatKhau = view.findViewById(R.id.btnDoiMatKhau);
    }
    private void changePassword(){
        String matKhauCu = txtMatKhauCu.getText().toString();
        String matKhauMoi = txtMatKhauMoi.getText().toString();
        String nhapLaiMatKhauMoi = txtNhapLaiMatKhauMoi.getText().toString();
        if (!AdditionalFunctions.isStringEmpty(getContext(),matKhauCu, matKhauMoi, nhapLaiMatKhauMoi)){
            return;
        }
        if (!AdditionalFunctions.isPasswordValid(getContext(), matKhauMoi)){
            return;
        }
        if (!matKhauMoi.equals(nhapLaiMatKhauMoi)){
            Toast.makeText(getContext(), "Nhập lại mật khẩu sai!", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseUser user = myFirebaseAuth.getCurrentUser();
        if (user != null){
            user.updatePassword(matKhauMoi).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getContext(), "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getContext(), "Hãy thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Hãy thử lại!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }
    }
}
