package com.example.duan1_nhom2.Model;

public class Albums {

    private String MaAlbum;
    private String TenAlbum;
    private String TenNgheSi;
    private int SoBaiHat;
    private String URLAnh;
    private int LuotXem;
    private int LuotXemThang;

    public Albums() {
    }

    public Albums(String maAlbums, String tenAlbums, String tenNgheSi, int soBaiHat, String urlAnh, int luotXem, int luotXemThang) {
        MaAlbum = maAlbums;
        TenAlbum = tenAlbums;
        TenNgheSi = tenNgheSi;
        SoBaiHat = soBaiHat;
        URLAnh = urlAnh;
        LuotXem = luotXem;
        LuotXemThang = luotXemThang;
    }

    public String getMaAlbum() {
        return MaAlbum;
    }

    public void setMaAlbum(String maAlbum) {
        MaAlbum = maAlbum;
    }

    public String getTenAlbum() {
        return TenAlbum;
    }

    public void setTenAlbum(String tenAlbum) {
        TenAlbum = tenAlbum;
    }

    public String getTenNgheSi() {
        return TenNgheSi;
    }

    public void setTenNgheSi(String tenNgheSi) {
        TenNgheSi = tenNgheSi;
    }

    public int getSoBaiHat() {
        return SoBaiHat;
    }

    public void setSoBaiHat(int soBaiHat) {
        SoBaiHat = soBaiHat;
    }

    public String getURLAnh() {
        return URLAnh;
    }

    public void setURLAnh(String URLAnh) {
        this.URLAnh = URLAnh;
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
