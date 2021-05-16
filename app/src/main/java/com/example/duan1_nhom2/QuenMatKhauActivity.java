package com.example.duan1_nhom2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class QuenMatKhauActivity extends AppCompatActivity {
    EditText txtEmail;
    Button btnLayLaiMatKhau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        findView();
        btnLayLaiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });
    }
    private void findView(){
        txtEmail = findViewById(R.id.txtEmail);
        btnLayLaiMatKhau = findViewById(R.id.btnLayLaiMatKhau);
    }
    private void sendPasswordResetEmail(){
        FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
        String email = txtEmail.getText().toString();
        if (!AdditionalFunctions.isEmailValid(getBaseContext(),email)){
            return;
        }
        myFirebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Email lấy lại mật khẩu đã gửi!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getBaseContext(), "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
