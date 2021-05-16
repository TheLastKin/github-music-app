package com.example.duan1_nhom2.FragmentClass;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1_nhom2.AdapterClass.TimKiemAlbum_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.TimKiemNgheSi_rvAdapter;
import com.example.duan1_nhom2.AdapterClass.TimKiemNhac_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_Album;
import com.example.duan1_nhom2.DAOClass.DAO_NgheSi;
import com.example.duan1_nhom2.DAOClass.DAO_Nhac;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNhac;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.R;

public class TimKiemFragment extends Fragment {
    TextView txtXemThemNhac, txtXemThemNgheSi, txtXemThemAlbums;
    RecyclerView rvTimKiemNhac, rvTimKiemAlbum, rvTimKiemNgheSi;
    TimKiemNhac_rvAdapter adapterTimKiemNhac;
    TimKiemAlbum_rvAdapter adapterTimKiemAlbum;
    TimKiemNgheSi_rvAdapter adapterTimKiemNgheSi;
    RecyclerView.LayoutManager layoutManager1, layoutManager2, layoutManager3;
    EditText txtTimKiem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timkiem, container, false);
        findView(view);
        createAdapter();
        txtTimKiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && !txtTimKiem.getText().toString().isEmpty()){
                    adapterTimKiemNhac.resetAdapter();
                    adapterTimKiemAlbum.resetAdapter();
                    adapterTimKiemNgheSi.resetAdapter();
                    String query = txtTimKiem.getText().toString();
                    if (!query.isEmpty()){
                        DAO_Nhac.readSpecificMusics(query, adapterTimKiemNhac, 20);
                        DAO_Album.readSpecificAlbums(query, adapterTimKiemAlbum, 20);
                        DAO_NgheSi.readSpecificArtists(query, adapterTimKiemNgheSi, 20);
                    }
                }
                return false;
            }
        });
        txtXemThemNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).changeFragmentWithData(1, adapterTimKiemNhac.getMusicList());
            }
        });
        txtXemThemNgheSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).changeFragmentWithData(2, adapterTimKiemNgheSi.getArtistList());
            }
        });
        txtXemThemAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).changeFragmentWithData(3, adapterTimKiemAlbum.getAlbumList());
            }
        });
        ifUserIsAnAdmin();
        ((MainActivity)getContext()).setToolbarTitle("Tìm Kiếm");
        return view;
    }

    private void ifUserIsAnAdmin(){
        MainActivity mainActivity = (MainActivity)getContext();
        if (mainActivity.isUserAnAdmin()){
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogThemVaCapNhatNhac dialog = new DialogThemVaCapNhatNhac(null, false);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatNhac");
                }
            };
            mainActivity.setOnClickToolbarAction1(listener, R.drawable.ic_add, View.VISIBLE);
            mainActivity.setOnClickToolbarAction2(null, R.drawable.ic_edit, View.INVISIBLE);
        }
    }
    private void findView(View view) {
        txtXemThemNhac = view.findViewById(R.id.txtXemThemNhac);
        txtXemThemNgheSi = view.findViewById(R.id.txtXemThemNgheSi);
        txtXemThemAlbums = view.findViewById(R.id.txtXemThemAlbum);
        rvTimKiemAlbum = view.findViewById(R.id.rvTimKiemAlbums);
        rvTimKiemNgheSi = view.findViewById(R.id.rvTimKiemNgheSi);
        rvTimKiemNhac = view.findViewById(R.id.rvTimKiemNhac);
        txtTimKiem = view.findViewById(R.id.txtTimKiem);
    }

    private void createAdapter() {
        Context context = getContext();
        adapterTimKiemNhac = new TimKiemNhac_rvAdapter(context, 3);
        adapterTimKiemNgheSi = new TimKiemNgheSi_rvAdapter(context, 3);
        adapterTimKiemAlbum = new TimKiemAlbum_rvAdapter(context, 3);
        rvTimKiemNhac.setAdapter(adapterTimKiemNhac);
        rvTimKiemNgheSi.setAdapter(adapterTimKiemNgheSi);
        rvTimKiemAlbum.setAdapter(adapterTimKiemAlbum);
        layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager3 = new LinearLayoutManager(getContext());
        rvTimKiemNhac.setLayoutManager(layoutManager1);
        rvTimKiemNgheSi.setLayoutManager(layoutManager2);
        rvTimKiemAlbum.setLayoutManager(layoutManager3);
    }

}
