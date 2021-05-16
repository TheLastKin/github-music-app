package com.example.duan1_nhom2.Model;

public class Nhac {
    private String MaNhac;
    private String TenNhac;
    private String TenNgheSi;
    private String TheLoai;
    private String ThoiLuong;
    private String URL;
    private String MaAlbum;
    private int LuotXem;
    private int LuotXemThang;

    public Nhac(){

    }
    public Nhac(String maNhac, String tenNhac, String tenNgheSi, String theLoai, String thoiLuong, String URL, String maAlbum, int luotXem, int luotXemThang) {
        if (tenNhac.trim().equals("")){
            TenNhac = "Không Tên";
        }else {
            TenNhac = tenNhac;
        }
        if (maAlbum.trim().equals("")){
            MaAlbum = "Không";
        }else{
            MaAlbum = maAlbum;
        }
        MaNhac = maNhac;
        TenNgheSi = tenNgheSi;
        TheLoai = theLoai;
        ThoiLuong = thoiLuong;
        this.URL = URL;
        LuotXem = luotXem;
        LuotXemThang = luotXemThang;
    }

    public String getMaNhac() {
        return MaNhac;
    }

    public void setMaNhac(String maNhac) {
        MaNhac = maNhac;
    }

    public String getTenNhac() {
        return TenNhac;
    }

    public void setTenNhac(String tenNhac) {
        TenNhac = tenNhac;
    }

    public String getTenNgheSi() {
        return TenNgheSi;
    }

    public void setTenNgheSi(String tenNgheSi) {
        TenNgheSi = tenNgheSi;
    }

    public String getTheLoai() {
        return TheLoai;
    }

    public void setTheLoai(String theLoai) {
        TheLoai = theLoai;
    }

    public String getThoiLuong() {
        return ThoiLuong;
    }

    public void setThoiLuong(String thoiLuong) {
        ThoiLuong = thoiLuong;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getMaAlbum() {
        return MaAlbum;
    }

    public void setMaAlbum(String maAlbum) {
        MaAlbum = maAlbum;
    }

    public int getLuotXem() {
        return LuotXem;
    }

    public void setLuotXem(int luotXem) {
        LuotXem = luotXem;
    }

    public int getLuotXemThang() {
        return LuotXemThang;
    }

    public void setLuotXemThang(int luotXemThang) {
        LuotXemThang = luotXemThang;
    }
}
