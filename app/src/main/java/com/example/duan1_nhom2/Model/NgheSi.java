package com.example.duan1_nhom2.Model;

import java.util.Date;

public class NgheSi {

    private String MaNgheSi;
    private String TenNgheSi;
    private String ThongTinThem;
    private String URLAnh;
    private int LuotXem;

    public NgheSi() {
    }

    public NgheSi(String maNgheSi, String tenNgheSi, String thongTinThem, String urlAnh, int luotXem) {
        MaNgheSi = maNgheSi;
        TenNgheSi = tenNgheSi;
        LuotXem = luotXem;
        ThongTinThem = thongTinThem;
        URLAnh = urlAnh;
    }

    public String getMaNgheSi() {
        return MaNgheSi;
    }

    public void setMaNgheSi(String maNgheSi) {
        MaNgheSi = maNgheSi;
    }

    public String getTenNgheSi() {
        return TenNgheSi;
    }

    public void setTenNgheSi(String tenNgheSi) {
        TenNgheSi = tenNgheSi;
    }

    public int getLuotXem() {
        return LuotXem;
    }

    public void setLuotXem(int luotXem) {
        LuotXem = luotXem;
    }

    public String getThongTinThem() {
        return ThongTinThem;
    }

    public void setThongTinThem(String thongTinThem) {
        ThongTinThem = thongTinThem;
    }

    public String getURLAnh() {
        return URLAnh;
    }

    public void setURLAnh(String URLAnh) {
        this.URLAnh = URLAnh;
    }
}
