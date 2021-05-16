package com.example.duan1_nhom2.Model;

public class TheLoai {

    private String MaTheLoai;
    private String TenTheLoai;
    private int LuotXemThang;

    public TheLoai() {
    }

    public TheLoai(String maTheLoai, String tenTheLoai, int luotXemThang) {
        MaTheLoai = maTheLoai;
        TenTheLoai = tenTheLoai;
        LuotXemThang = luotXemThang;
    }

    public String getMaTheLoai() {
        return MaTheLoai;
    }

    public void setMaTheLoai(String maTheLoai) {
        MaTheLoai = maTheLoai;
    }

    public String getTenTheLoai() {
        return TenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        TenTheLoai = tenTheLoai;
    }

    public int getLuotXemThang() {
        return LuotXemThang;
    }

    public void setLuotXemThang(int luotXemThang) {
        LuotXemThang = luotXemThang;
    }
}
