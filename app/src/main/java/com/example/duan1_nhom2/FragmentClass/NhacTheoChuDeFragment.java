package com.example.duan1_nhom2.FragmentClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.AdapterClass.TimKiemNhac_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatTheLoai;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.Nhac;
import com.example.duan1_nhom2.Model.TheLoai;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class NhacTheoChuDeFragment extends Fragment {
    Button btnPhatTatCa;
    RecyclerView rvNhacCungChuDe;
    TimKiemNhac_rvAdapter adapter;
    LinearLayoutManager layoutManager;
    TheLoai theLoai;
    public NhacTheoChuDeFragment(TheLoai theLoai){
        this.theLoai = theLoai;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timkiem_chude, container, false);
        findView(view);
        adapter = new TimKiemNhac_rvAdapter(getContext(), 20);
        layoutManager = new LinearLayoutManager(getContext());
        rvNhacCungChuDe.setLayoutManager(layoutManager);
        rvNhacCungChuDe.setAdapter(adapter);
        DAO_Nhac.readPopularMusic(theLoai.getTenTheLoai(), adapter);
        btnPhatTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Nhac> dsn = adapter.getMusicList();
                if (dsn.size() > 0){
                    ((MainActivity)getContext()).changeToNhacDangNgheFragment(dsn);
                }
            }
        });
        ifUserIsAnAdmin();
        return view;
    }
    private void ifUserIsAnAdmin(){
        MainActivity mainActivity = (MainActivity)getContext();
        if (mainActivity.isUserAnAdmin()){
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogThemVaCapNhatTheLoai dialog = new DialogThemVaCapNhatTheLoai(theLoai, true);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatTheLoai");
                }
            };
            mainActivity.setOnClickToolbarAction2(listener, R.drawable.ic_edit, View.VISIBLE);
        }
    }
    private void findView(View view){
        btnPhatTatCa = view.findViewById(R.id.btnPhatTatCa);
        rvNhacCungChuDe = view.findViewById(R.id.rvNhacCungChuDe);
    }
}
