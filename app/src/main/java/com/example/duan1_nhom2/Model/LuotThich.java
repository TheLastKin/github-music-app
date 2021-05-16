package com.example.duan1_nhom2.Model;

public class LuotThich {
    private String MaLuotThich;
    private String MaNguoiDung;
    private String MaNhac;
    private String TrangThai;

    public LuotThich() {
    }

    public LuotThich(String maLuotThich, String maNguoiDung, String maNhac, String trangThai) {
        MaLuotThich = maLuotThich;
        MaNguoiDung = maNguoiDung;
        MaNhac = maNhac;
        TrangThai = trangThai;
    }

    public String getMaLuotThich() {
        return MaLuotThich;
    }

    public void setMaLuotThich(String maLuotThich) {
        MaLuotThich = maLuotThich;
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

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }
}
