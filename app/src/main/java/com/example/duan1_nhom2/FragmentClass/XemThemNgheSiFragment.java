package com.example.duan1_nhom2.FragmentClass;

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

import com.example.duan1_nhom2.AdapterClass.TimKiemNgheSi_rvAdapter;
import com.example.duan1_nhom2.DAOClass.DAO_NgheSi;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNgheSi;
import com.example.duan1_nhom2.DialogClass.DialogThemVaCapNhatNhac;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.NgheSi;
import com.example.duan1_nhom2.R;

import java.util.ArrayList;

public class XemThemNgheSiFragment extends Fragment {
    EditText txtTimKiemNgheSi;
    RecyclerView rvXemThemNgheSi;
    ArrayList<NgheSi> dsn;
    TimKiemNgheSi_rvAdapter adapterTimKiemNgheSi;
    public XemThemNgheSiFragment(ArrayList dsn) {
        this.dsn = dsn;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timkiem_don, container, false);
        findView(view);
        adapterTimKiemNgheSi = new TimKiemNgheSi_rvAdapter(getContext(), 30);
        rvXemThemNgheSi.setAdapter(adapterTimKiemNgheSi);
        rvXemThemNgheSi.setLayoutManager(new LinearLayoutManager(getContext()));
        txtTimKiemNgheSi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    adapterTimKiemNgheSi.resetAdapter();
                    String query = txtTimKiemNgheSi.getText().toString();
                    if (!query.isEmpty()) {
                        DAO_NgheSi.readSpecificArtists(txtTimKiemNgheSi.getText().toString(), adapterTimKiemNgheSi, 20);
                    }
                }
                return false;
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
                    DialogThemVaCapNhatNgheSi dialog = new DialogThemVaCapNhatNgheSi(null, false);
                    dialog.show(getChildFragmentManager(), "DialogThemVaCapNhatNgheSi");
                }
            };
            mainActivity.setOnClickToolbarAction1(listener, R.drawable.ic_add, View.VISIBLE);
            mainActivity.setOnClickToolbarAction2(null, R.drawable.ic_edit, View.INVISIBLE);
        }
    }
    private void findView(View view){
        txtTimKiemNgheSi = view.findViewById(R.id.txtTimKiem);
        rvXemThemNgheSi = view.findViewById(R.id.rvXemThem);
    }
}
