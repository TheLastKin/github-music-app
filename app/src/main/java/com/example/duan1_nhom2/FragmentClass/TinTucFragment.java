package com.example.duan1_nhom2.FragmentClass;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.duan1_nhom2.AdapterClass.TinTuc_rvAdapter;
import com.example.duan1_nhom2.AdditionalFunctions.HTTPDataHandler;
import com.example.duan1_nhom2.MainActivity;
import com.example.duan1_nhom2.Model.RSSObject;
import com.example.duan1_nhom2.R;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class TinTucFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RSSObject rssObject;
    private final String rssLink = "https://vnexpress.net/rss/giai-tri.rss";
    private final String rssToJsonAPI = "https://api.rss2json.com/v1/api.json?rss_url=";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tintuc, container, false);
        recyclerView = view.findViewById(R.id.rvTinTuc);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loadRSS();
        ((MainActivity)getActivity()).setOnClickToolbarAction1(null, 0, View.INVISIBLE);
        return view;
    }
    private void loadRSS(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                @SuppressLint("StaticFieldLeak") final AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        String result;
                        HTTPDataHandler http = new HTTPDataHandler();
                        result = http.getHTTPData(strings[0]);
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        JsonParser parser = new JsonParser();
                        rssObject = new Gson().fromJson(parser.parse(s), RSSObject.class);
                        TinTuc_rvAdapter adapter = new TinTuc_rvAdapter(rssObject, getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                };
                StringBuilder urlGetData = new StringBuilder(rssToJsonAPI);
                urlGetData.append(rssLink);
                loadRSSAsync.execute(urlGetData.toString());
            }
        }, 700);
    }
}

