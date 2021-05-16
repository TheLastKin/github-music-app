package com.example.duan1_nhom2.DAOClass;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.duan1_nhom2.AdapterClass.AlbumTrangChu_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.ChudeTrangchu_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.NhacTrangChu_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.TimKiemNgheSi_rvAdapter;
import com.example.duan1_nhom2.AdditionalFunctions.AdditionalFunctions;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNgheSi;
import com.example.duan1_nhom2.FragmentClass.NgheSiFragment;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Albums;
import com.example.duan1_nhom2.Model.NgheSi;
import com.example.duan1_nhom2.Model.Nhac;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class DAO_NgheSi {

    public static void readSpecificArtists(final String tenNgheSi, final TimKiemNgheSi_rvAdapter adapter, final int max){
        final int[] count = {0};
        //Truy cập đến bảng NgheSi
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        //Đọc dữ liệu
        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Nếu có dữ liệu thì
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        //Ép kiểu dữ liệu Firebase thành model NgheSi
                        NgheSi ngheSi = i.getValue(NgheSi.class);
                        if (ngheSi.getTenNgheSi().toLowerCase().contains(tenNgheSi.toLowerCase()) && count[0]<=max){
                            adapter.updateAdapter(ngheSi);
                            count[0]++;
                        }else if (count[0]>max){
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

    public static void readOneArtist(String tenNgheSi, final NhacTrangChu_rvAdapter adapter1,
                                     final AlbumTrangChu_rvAdapter adapter2, final NgheSiFragment fragment){
        DatabaseReference myDatabaseRef1 = FirebaseDatabase.getInstance().getReference("Nhac");
        DatabaseReference myDatabaseRef2 = FirebaseDatabase.getInstance().getReference("Albums");
        final ArrayList<Nhac> dsn = new ArrayList<>();
        final ArrayList<Albums> dsalbums = new ArrayList<>();
        Query query1 = myDatabaseRef1.orderByChild("tenNgheSi").equalTo(tenNgheSi);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        Nhac nhac = i.getValue(Nhac.class);
                        dsn.add(nhac);
                    }
                    Collections.sort(dsn, new Comparator<Nhac>() {
                        @Override
                        public int compare(Nhac o1, Nhac o2) {
                            return o1.getLuotXem()>=o2.getLuotXem()?1:-1;
                        }
                    });
                    adapter1.updateAdapter(dsn);
                    String totalSong = dsn.size() + " Bài Hát";
                    fragment.setTotalSong(totalSong);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query query2 = myDatabaseRef2.orderByChild("tenNgheSi").equalTo(tenNgheSi);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        Albums album = i.getValue(Albums.class);
                        dsalbums.add(album);
                    }
                    Collections.sort(dsalbums, new Comparator<Albums>() {
                        @Override
                        public int compare(Albums o1, Albums o2) {
                            return o1.getLuotXem()>=o2.getLuotXem()?1:-1;
                        }
                    });
                    adapter2.updateAdapter(dsalbums);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void readAritistAndChangeFragment(String tenNgheSi, final Context context){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        myDatabaseRef.orderByChild("tenNgheSi").equalTo(tenNgheSi).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot i: snapshot.getChildren()){
                        NgheSi ngheSi = i.getValue(NgheSi.class);
                        if (ngheSi != null){
                            ((MainActivity)context).changeToNgheSiFragment(ngheSi);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void uploadArtist(Uri uri, Context context, DialogThemVaCapNhatNgheSi dialog, NgheSi ngheSi, final TextView textView, ImageView imageView){
        if (uri == null){
            uploadArtistWithoutImage(ngheSi, textView);
        }else{
            uploadArtistWithImage(uri, context, dialog, ngheSi, textView, imageView);
        }

    }

    private static void uploadArtistWithImage(Uri uri, Context context, final DialogThemVaCapNhatNgheSi dialog,
                                              final NgheSi ngheSi, final TextView textView, ImageView imageView){
        //Truy cập đến bảng NgheSi
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        final String maNgheSi = myDatabaseRef.push().getKey();
        final StorageReference myStorageRef = FirebaseStorage.getInstance().getReference("AnhNgheSi")
                .child(maNgheSi + AdditionalFunctions.getFileExtension(context, uri));
        final UploadTask uploadTask = myStorageRef.putFile(uri);
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
                String urlAnh = uri.toString();
                String tenNgheSi = ngheSi.getTenNgheSi();
                String thongTinThem = ngheSi.getThongTinThem();
                NgheSi newNgheSi = new NgheSi(maNgheSi, tenNgheSi, thongTinThem, urlAnh, 0);
                myDatabaseRef.child(maNgheSi).setValue(newNgheSi).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AdditionalFunctions.changeTextInMilisecond(textView, "Thành Công", "Thêm Nghệ Sĩ");
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask.isInProgress()){
                    uploadTask.cancel();
                    uploadTask.addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            AdditionalFunctions.changeTextInMilisecond(textView, "Hủy Thành Công", "Thêm Nghệ Sĩ");
                        }
                    });
                }
            }
        });
    }

    private static void uploadArtistWithoutImage(NgheSi ngheSi, final TextView textView){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        String maNgheSi = myDatabaseRef.push().getKey();
        String tenNgheSi = ngheSi.getTenNgheSi();
        String thongTinThem = ngheSi.getThongTinThem();
        NgheSi newNgheSi = new NgheSi(maNgheSi, tenNgheSi, thongTinThem, "NoImage", 0);
        myDatabaseRef.child(maNgheSi).setValue(newNgheSi).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdditionalFunctions.changeTextInMilisecond(textView,"Thành Công", "Thêm Nghệ Sĩ");
            }
        });
    }

    public static void updateArist(Uri uri, Context context, DialogThemVaCapNhatNgheSi dialog, NgheSi ngheSi, TextView textView, ImageView imageView){
        if (uri == null){
            updateArtistWithoutImage(ngheSi, textView);
        }else{
            updateArtistWithImage(uri, context, dialog, ngheSi, textView, imageView);
        }
    }

    private static void updateArtistWithImage(Uri uri, Context context, final DialogThemVaCapNhatNgheSi dialog,
                                              final NgheSi ngheSi, final TextView textView, ImageView imageView){
        if (uri == null){
            return;
        }
        if (!ngheSi.getURLAnh().equals("NoImage")){
            FirebaseStorage.getInstance().getReferenceFromUrl(ngheSi.getURLAnh()).delete();
        }
        //Truy cập đến bảng NgheSi
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        final StorageReference myStorageRef = FirebaseStorage.getInstance().getReference("AnhNgheSi")
                .child(ngheSi.getMaNgheSi() + AdditionalFunctions.getFileExtension(context, uri));
        final UploadTask uploadTask = myStorageRef.putFile(uri);
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
                String urlAnh = uri.toString();
                Map<String, Object> postValues = new HashMap<>();
                postValues.put("tenNgheSi", ngheSi.getTenNgheSi());
                postValues.put("thongTinThem", ngheSi.getThongTinThem());
                postValues.put("urlanh", urlAnh);
                myDatabaseRef.child(ngheSi.getMaNgheSi()).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AdditionalFunctions.changeTextInMilisecond(textView, "Thành Công", "Cập nhật Nghệ Sĩ");
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask.isInProgress()){
                    uploadTask.cancel();
                    uploadTask.addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            AdditionalFunctions.changeTextInMilisecond(textView, "Hủy Thành Công", "Cập nhật Nghệ Sĩ");
                        }
                    });
                }
            }
        });
    }

    private static void updateArtistWithoutImage(NgheSi ngheSi, final TextView textView){
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("tenNgheSi", ngheSi.getTenNgheSi());
        postValues.put("thongTinThem", ngheSi.getThongTinThem());
        myDatabaseRef.child(ngheSi.getMaNgheSi()).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AdditionalFunctions.changeTextInMilisecond(textView, "Thành Công", "Cập Nhật Nghệ Sĩ");
            }
        });
    }

    public static void updateArtistViewAmount(String maNgheSi, int luotXem){
        //Truy cập đến bảng NgheSi
        DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        //Tạo HashMap để cập nhật
        Map<String, Object> postValues = new HashMap<>();
        postValues.put("luotXem", luotXem+1);
        //Truyền HashMap vào để cập nhật lượt view của nghệ sĩ dựa theo mã nghệ sĩ
        myDatabaseRef.child(maNgheSi).updateChildren(postValues);
    }

    public static void deleteArtist(final String maNgheSi, final String urlAnh, final TextView textView){
        //Truy cập đến bảng NgheSi
        final DatabaseReference myDatabaseRef = FirebaseDatabase.getInstance().getReference("NgheSi");
        //Xóa nghệ sĩ dựa theo mã nghệ sĩ
        if (!urlAnh.equals("NoImage")){
            FirebaseStorage.getInstance().getReferenceFromUrl(urlAnh).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    myDatabaseRef.child(maNgheSi).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AdditionalFunctions.changeTextInMilisecond(textView, "Xóa Nghệ Sĩ Thành Công", "Xóa Nghệ Sĩ");
                        }
                    });
                }
            });
        }
    }

}
