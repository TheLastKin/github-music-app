package com.example.duan1_nhom2.DAOClass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DialogClass.DialogDoiAnhDaiDien;
import com.example.duan1_nhom2.LoginActivity;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DAO_NguoiDung {
    public static void loginAsUser(FirebaseUser user, final Context context, String password){
        final Bundle bundle = new Bundle();
        final String email = user.getEmail();
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        bundle.putString("Email", email);
        bundle.putInt("LoginAs", 3);
        bundle.putString("Password", hashedPassword);
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i:snapshot.getChildren()){
                        NguoiDung nguoiDung = i.getValue(NguoiDung.class);
                        if (nguoiDung.getEmail().equals(email)){
                            bundle.putString("UserID", nguoiDung.getMaNguoiDung());
                            bundle.putString("PhotoURL", nguoiDung.getURLAnh());
                            bundle.putString("DisplayName", nguoiDung.getTenHienThi());
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("UserProfile", bundle);
                            context.startActivity(intent);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void addUser(NguoiDung nguoiDung, final Context context){
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        String maNguoiDung = nguoiDung.getMaNguoiDung();
        myDatabaseRef.child(maNguoiDung).setValue(nguoiDung).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
    }
    public static void loginAsGoogle(FirebaseUser user, final Context context){
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        final Bundle bundle = new Bundle();
        final String displayName = user.getDisplayName();
        final String photoURL = user.getPhotoUrl().toString();
        final String email = user.getEmail();
        bundle.putInt("LoginAs", 1);
        bundle.putString("PhotoURL", photoURL);
        bundle.putString("Email", email);
        bundle.putString("DisplayName", displayName);
        bundle.putInt("LoginAs", 1);
        Query query = myDatabaseRef.orderByChild("email").equalTo(user.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        NguoiDung nguoiDung = i.getValue(NguoiDung.class);
                        if (nguoiDung.getEmail().equals(email)){
                            bundle.putString("UserID", nguoiDung.getMaNguoiDung());
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("UserProfile", bundle);
                            context.startActivity(intent);
                            break;
                        }
                    }
                }else{
                    String maNguoiDung = myDatabaseRef.push().getKey();
                    bundle.putString("UserID", maNguoiDung);
                    NguoiDung nguoiDung = new NguoiDung(maNguoiDung, displayName, "", email, photoURL);
                    myDatabaseRef.child(maNguoiDung).setValue(nguoiDung).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("UserProfile", bundle);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void loginAsFacebook(final String id, final String tenHienThi, final String email, final String photoURL, final Context context){
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        final Bundle bundle = new Bundle();
        bundle.putString("UserID", id);
        bundle.putString("DisplayName", tenHienThi);
        bundle.putString("PhotoURL", photoURL);
        bundle.putString("Email", email);
        bundle.putInt("LoginAs", 0);
        Query query = myDatabaseRef.orderByChild("maNguoiDung").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("UserProfile", bundle);
                    context.startActivity(intent);
                }else{
                    NguoiDung nguoiDung = new NguoiDung(id, tenHienThi, "", email, photoURL);
                    myDatabaseRef.child(id).setValue(nguoiDung).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("UserProfile", bundle);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void changeUserPhoto(final Uri uri, final Context context, final DialogDoiAnhDaiDien dialog, final String maNguoiDung, String urlAnh){
        if (urlAnh.contains("firebasestorage")){
            FirebaseStorage.getInstance().getReferenceFromUrl(urlAnh).delete();
        }
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        final StorageReference myStorageRef = FirebaseStorage.getInstance().getReference("AnhNguoiDung").child(maNguoiDung + AdditionalFunctions.getFileExtension(context, uri));
        if (uri == null){
            return;
        }
        UploadTask uploadTask = myStorageRef.putFile(uri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return myStorageRef.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final String urlAnh = uri.toString();
                Map<String, Object> postValues = new HashMap<>();
                postValues.put("urlanh", urlAnh);
                myDatabaseRef.child(maNguoiDung).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Đổi Ảnh Đại Diện Thành Công!", Toast.LENGTH_SHORT).show();
                        dialog.onAvatarUpdated(urlAnh);
                        ((MainActivity)context).changeToolbarImage(urlAnh);
                        dialog.lockTextView(false);
                        dialog.dismiss();
                    }
                });
            }
        });
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                dialog.updateProgressBar(progress);
            }
        });
    }
}
