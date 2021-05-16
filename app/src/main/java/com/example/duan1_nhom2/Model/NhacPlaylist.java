package com.example.duan1_nhom2.Model;

public class NhacPlaylist {
    private String MaNhacPlaylist;
    private String MaPlaylist;
    private String MaNguoiDung;
    private String MaNhac;

    public NhacPlaylist() {
    }

    public NhacPlaylist(String maNhacPlaylist, String maPlaylist, String maNguoiDung, String maNhac) {
        MaNhacPlaylist = maNhacPlaylist;
        MaPlaylist = maPlaylist;
        MaNguoiDung = maNguoiDung;
        MaNhac = maNhac;
    }

    public String getMaNhacPlaylist() {
        return MaNhacPlaylist;
    }

    public void setMaNhacPlaylist(String maNhacPlaylist) {
        MaNhacPlaylist = maNhacPlaylist;
    }

    public String getMaPlaylist() {
        return MaPlaylist;
    }

    public void setMaPlaylist(String maPlaylist) {
        MaPlaylist = maPlaylist;
    }

    public String getMaNguoiDung() {
        return MaNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        MaNguoiDung = maNguoiDung;
    }

    public String getMaNhac() {
        return MaNhac;
    }

    public void setMaNhac(String maNhac) {
        MaNhac = maNhac;
    }
}
