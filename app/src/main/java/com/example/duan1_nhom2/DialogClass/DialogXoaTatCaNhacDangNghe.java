package com.example.duan1_nhom2.DialogClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.duan1_nhom2.R;

public class DialogXoaTatCaNhacDangNghe extends DialogFragment {
    TextView txtXacNhan;
    ImageView ivCloseDialog;
    public interface RemoveQueue{
        void removeAllSongsFromQueue();
    }
    RemoveQueue listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_xoatatca_nhacdangnghe, container, false);
        findView(view);
        txtXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.removeAllSongsFromQueue();
                getDialog().dismiss();
            }
        });
        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }
    private void findView(View view) {
        txtXacNhan = view.findViewById(R.id.txtXacNhan);
        ivCloseDialog = view.findViewById(R.id.ivCloseDialog);
    }
    public void setRemoveQueueListener(RemoveQueue listener){
        this.listener = listener;
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams)layoutParams);
    }
}
