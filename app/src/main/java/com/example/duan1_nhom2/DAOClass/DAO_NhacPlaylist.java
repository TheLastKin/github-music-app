package com.example.duan1_nhom2.DAOClass;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duan1_nhom2.AdapterClass.NhacTrangChu_rvAdapter;
import com.example.duan1_nhom2.DialogClass.DialogThemVaoPlaylist;
import com.example.duan1_nhom2.Model.NhacPlaylist;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DAO_NhacPlaylist {
    public static void uploadPlaylistMusic(String maNguoiDung, String maNhac, final String maPlaylist, final int soBaiHat, final Context context){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NhacPlaylist");
        String maNhacPlaylist = myDatabaseRef.push().getKey();
        NhacPlaylist nhacPlaylist = new NhacPlaylist(maNhacPlaylist, maPlaylist, maNguoiDung, maNhac);
        myDatabaseRef.child(maNhacPlaylist).setValue(nhacPlaylist).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DAO_Playlist.updatePlaylistMusicAmount(maPlaylist, soBaiHat);
                Toast.makeText(context, "Thêm Thành Công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void readPlaylistMusic(final String maPlaylist, final NhacTrangChu_rvAdapter adapter, final TextView textView){
        final int[] count = {0};
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NhacPlaylist");
        myDatabaseRef.orderByChild("maPlaylist").equalTo(maPlaylist).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        NhacPlaylist nhacPlaylist = i.getValue(NhacPlaylist.class);
                        if (nhacPlaylist != null){
                            if (nhacPlaylist.getMaPlaylist().equals(maPlaylist)){
                                DAO_Nhac.readPlaylistMusic(nhacPlaylist.getMaNhac(), adapter);
                                count[0]++;
                            }
                        }
                    }
                    String soBaiHat = count[0] + " Bài Hát";
                    textView.setText(soBaiHat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void deletePlaylistMusic(String maPlaylist){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NhacPlaylist");
        myDatabaseRef.orderByChild("maPlaylist").equalTo(maPlaylist).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
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
