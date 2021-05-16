package com.example.duan1_nhom2.DAOClass;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.duan1_nhom2.AdapterClass.Playlist_rvAdapter;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatPlaylist;
import com.example.duan1_nhom2.DialogClass.DialogThemVaoPlaylist;
import com.example.duan1_nhom2.DialogClass.DialogXoaPlaylist;
import com.example.duan1_nhom2.FragmentClass.PlaylistFragment;
import com.example.duan1_nhom2.Model.NguoiDung;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.Model.PlayList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DAO_Playlist {
    public static void readAllUserPlaylist(String maNguoiDung, final PlaylistFragment fragment){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Playlist");
        Query query = myDatabaseRef.orderByChild("maNguoiDung").equalTo(maNguoiDung);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    fragment.setNotificationVisibility(false);
                    fragment.setRecyclerViewVisibility(true);
                    for (DataSnapshot i: snapshot.getChildren()){
                        PlayList playList = i.getValue(PlayList.class);
                        fragment.updatePlaylistAdapter(playList);
                    }
                }else{
                    fragment.setNotificationVisibility(true);
                    fragment.setRecyclerViewVisibility(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void readAllUserPlaylist(String maNguoiDung, final DialogThemVaoPlaylist dialog){
        final ArrayList<PlayList> dspl = new ArrayList<>();
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Playlist");
        myDatabaseRef.orderByChild("maNguoiDung").equalTo(maNguoiDung).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        PlayList playList = i.getValue(PlayList.class);
                        dspl.add(playList);
                    }
                    dialog.spinnerSetUp(dspl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public static void changePlaylistName(String maPlaylist, String tenPlaylist, final DialogThemVaCapNhatPlaylist.UpdatePlaylist listener){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Playlist");
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("tenPlaylist", tenPlaylist);
        myDatabaseRef.child(maPlaylist).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onWorkCompleted();
            }
        });
    }
    public static void createPlaylist(String maNguoiDung, String tenPlaylist, final DialogThemVaCapNhatPlaylist.UpdatePlaylist listener){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Playlist");
        String maPlaylist = myDatabaseRef.push().getKey();
        PlayList playListMoi = new PlayList(maPlaylist,tenPlaylist, maNguoiDung, 0);
        myDatabaseRef.child(maPlaylist).setValue(playListMoi).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onWorkCompleted();
            }
        });
    }
    public static void deletePlaylist(final String maPlaylist, final DialogThemVaCapNhatPlaylist.UpdatePlaylist listener){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Playlist");
        myDatabaseRef.child(maPlaylist).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DAO_NhacPlaylist.deletePlaylistMusic(maPlaylist);
                listener.onWorkCompleted();
            }
        });
    }
    public static void updatePlaylistMusicAmount(String maPlaylist, int soBaiHat){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("Playlist");
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("soBaiHat", soBaiHat+1);
        myDatabaseRef.child(maPlaylist).updateChildren(postValues);
    }
}
