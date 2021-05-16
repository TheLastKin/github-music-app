package com.example.duan1_nhom2.DAOClass;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duan1_nhom2.FragmentClass.NhacDangNgheFragment;
import com.example.duan1_nhom2.Model.LuotThich;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAO_LuotThich {
    public static void loadFavorite(final String userID, final String maNhac, final NhacDangNgheFragment fragment){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("LuotThich");
        Query query = myDatabaseRef.orderByChild("maNhac").equalTo(maNhac);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        LuotThich luotThich = i.getValue(LuotThich.class);
                        if (luotThich.getMaNguoiDung().equals(userID) && luotThich.getTrangThai().equals("true")){
                            fragment.isUserFavorite(true, luotThich.getMaLuotThich());
                            return;
                        }
                    }
                    fragment.isUserFavorite(false, null);
                }else{
                    fragment.isUserFavorite(false, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void loadTotalLike(String maNhac, final NhacDangNgheFragment fragment){
        final int[] m = {0};
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("LuotThich");
        Query query = myDatabaseRef.orderByChild("maNhac").equalTo(maNhac);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        m[0]++;
                        fragment.setTotalLike(m[0]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void addToFavorite(String maLuotThich, String userID, final String maNhac, final NhacDangNgheFragment fragment){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("LuotThich");
        if (maLuotThich.equals("")){
            final String MaLuotThich = myDatabaseRef.push().getKey();
            LuotThich luotThich = new LuotThich(MaLuotThich, userID, maNhac, "true");
            myDatabaseRef.child(MaLuotThich).setValue(luotThich).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    fragment.setMaLuotThich(MaLuotThich);
                    loadTotalLike(maNhac, fragment);
                }
            });
        }else{
            myDatabaseRef.child(maLuotThich).child("trangThai").setValue("true");
        }
    }
    public static void removeFromFavorite(String maLuotThich, final String maNhac, final NhacDangNgheFragment fragment){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("LuotThich").child(maLuotThich).child("trangThai");
        myDatabaseRef.setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadTotalLike(maNhac, fragment);
            }
        });
    }
    public static void deleteFavorite(String MaNhac){
        final DatabaseReference myDataBaseRef = FirebaseDatabase.getInstance().getReference("LuotThich");
        myDataBaseRef.orderByChild("maNhac").equalTo(MaNhac).addListenerForSingleValueEvent(new ValueEventListener() {
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
