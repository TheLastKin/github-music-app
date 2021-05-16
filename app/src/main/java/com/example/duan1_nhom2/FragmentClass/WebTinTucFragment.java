package com.example.duan1_nhom2.FragmentClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1_nhom2.R;

public class WebTinTucFragment extends Fragment {
    WebView wvTinTuc;
    String link;
    public WebTinTucFragment(String link){
        this.link = link;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webtintuc, container, false);
        findView(view);
        if (!link.isEmpty()){
            wvTinTuc.loadUrl(link);
        }
        return view;
    }
    private void findView(View view){
        wvTinTuc = view.findViewById(R.id.wvTinTuc);
    }
}
