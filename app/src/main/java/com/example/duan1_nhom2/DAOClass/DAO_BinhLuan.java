package com.example.duan1_nhom2.DAOClass;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom2.AdapterClass.BinhLuan_rvAdapter;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DialogClass.DialogSuaBinhLuan;
import com.example.duan1_nhom2.DialogClass.DialogXoaBinhLuan;
import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.BinhLuan;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DAO_BinhLuan {
    public static void readMusicComments(String maNhac, final BinhLuan_rvAdapter adapter){
        adapter.clearItems();
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("BinhLuan");
        Query query = myDatabaseRef.orderByChild("maNhac").equalTo(maNhac);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        BinhLuan binhLuan = i.getValue(BinhLuan.class);
                        adapter.updateAdapter(binhLuan);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void updateMusicComment(String maBinhLuan, final String binhLuan, final DialogSuaBinhLuan.OnCommentUpdated listener){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("BinhLuan");
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("binhLuan", binhLuan);
        myDatabaseRef.child(maBinhLuan).updateChildren(postValues).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    listener.onCommentUpdated();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void deleteMusicComment(String maBinhLuan, final DialogXoaBinhLuan.OnCommentDeleted listener){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("BinhLuan");
        myDatabaseRef.child(maBinhLuan).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    listener.onCommentDeleted();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void postComment(String maNguoiDung, String maNhac, final String binhLuan, final BinhLuan_rvAdapter adapter){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("BinhLuan");
        String maBinhLuan = myDatabaseRef.push().getKey();
        final BinhLuan objBinhLuan = new BinhLuan(maBinhLuan, maNguoiDung, binhLuan, maNhac);
        myDatabaseRef.child(maBinhLuan).setValue(objBinhLuan).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    adapter.updateAdapter(objBinhLuan);
                }
            }
        });
    }
    public static void getUserCommentProfile(final Context context, final BinhLuan_rvAdapter adapter, final String maBinhLuan,
                                             final String maNguoiDung, final String maNguoiDungHienTai, final String binhLuan,
                                             final TextView txtTenNguoiDung, final TextView txtBinhLuan,
                                             final ImageView ivAvatarNguoiDung, final ImageView ivTuyChinhBinhLuan){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NguoiDung");
        myDatabaseRef.orderByChild("maNguoiDung").equalTo(maNguoiDung).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        NguoiDung nguoiDung = i.getValue(NguoiDung.class);
                        txtTenNguoiDung.setText(nguoiDung.getTenHienThi());
                        Picasso.with(context).load(nguoiDung.getURLAnh()).into(ivAvatarNguoiDung);
                        txtBinhLuan.setText(binhLuan);
                        if (nguoiDung.getMaNguoiDung().equals(maNguoiDungHienTai)){
                            ivTuyChinhBinhLuan.setImageResource(R.drawable.ic_more);
                            final PopupMenu popupMenu = popupMenuSetUp(context, adapter, maBinhLuan, binhLuan, ivTuyChinhBinhLuan);
                            ivTuyChinhBinhLuan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupMenu.show();
                                }
                            });
                            ivTuyChinhBinhLuan.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static PopupMenu popupMenuSetUp(final Context context, final BinhLuan_rvAdapter adapter, final String maBinhLuan, final String binhLuan, ImageView ivTuyChinhBinhLuan){
        PopupMenu popupMenu = new PopupMenu(context, ivTuyChinhBinhLuan);
        popupMenu.inflate(R.menu.tuychinh_binhluan_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.txtSuaBinhLuan:{
                        DialogSuaBinhLuan dialog = new DialogSuaBinhLuan(maBinhLuan, binhLuan);
                        dialog.setOnCommentUpdatedListener(adapter);
                        dialog.show(((MainActivity)context).getSupportFragmentManager(), "DialogSuaBinhLuan");
                        return true;
                    }
                    case R.id.txtXoaBinhLuan:{
                        DialogXoaBinhLuan dialog = new DialogXoaBinhLuan(maBinhLuan);
                        dialog.setOnCommentDeleteListener(adapter);
                        dialog.show(((MainActivity)context).getSupportFragmentManager(), "DialogXoaBinhLuan");
                        return true;
                    }
                    default:{
                        return false;
                    }
                }
            }
        });
        return popupMenu;
    }
    public static void deleteComment(String maNhac){
        DatabaseReference myDataRef = FirebaseDatabase.getInstance().getReference("BinhLuan");
        myDataRef.orderByChild("maNhac").equalTo(maNhac).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i:snapshot.getChildren()){
                        i.getRef().removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
