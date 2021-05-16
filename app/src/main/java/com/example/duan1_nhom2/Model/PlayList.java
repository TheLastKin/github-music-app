package com.example.duan1_nhom2.Model;

import androidx.annotation.NonNull;

public class PlayList {

    private String MaPlayList;
    private String TenPlayList;
    private String MaNguoiDung;
    private int SoBaiHat;

    public PlayList() {
    }

    public PlayList(String maPlayList, String tenPlayList, String maNguoiDung, int soBaiHat) {
        MaPlayList = maPlayList;
        TenPlayList = tenPlayList;
        MaNguoiDung = maNguoiDung;
        SoBaiHat = soBaiHat;
    }

    public String getMaPlayList() {
        return MaPlayList;
    }

    public void setMaPlayList(String maPlayList) {
        MaPlayList = maPlayList;
    }

    public String getTenPlayList() {
        return TenPlayList;
    }

    public void setTenPlayList(String tenPlayList) {
        TenPlayList = tenPlayList;
    }

    public String getMaNguoiDung() {
        return MaNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        MaNguoiDung = maNguoiDung;
    }

    public int getSoBaiHat() {
        return SoBaiHat;
    }

    public void setSoBaiHat(int soBaiHat) {
        SoBaiHat = soBaiHat;
    }

    @NonNull
    @Override
    public String toString() {
        return getTenPlayList();
    }
}
