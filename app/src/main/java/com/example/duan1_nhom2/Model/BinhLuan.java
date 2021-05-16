package com.example.duan1_nhom2.Model;

public class BinhLuan {
    private String MaBinhLuan;
    private String MaNguoiDung;
    private String BinhLuan;
    private String MaNhac;

    public BinhLuan() {
    }

    public BinhLuan(String maBinhLuan, String maNguoiDung, String binhLuan, String maNhac) {
        MaBinhLuan = maBinhLuan;
        MaNguoiDung = maNguoiDung;
        BinhLuan = binhLuan;
        MaNhac = maNhac;
    }

    public String getMaBinhLuan() {
        return MaBinhLuan;
    }

    public void setMaBinhLuan(String maBinhLuan) {
        MaBinhLuan = maBinhLuan;
    }

    public String getMaNguoiDung() {
        return MaNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        MaNguoiDung = maNguoiDung;
    }

    public String getBinhLuan() {
        return BinhLuan;
    }

    public void setBinhLuan(String binhLuan) {
        BinhLuan = binhLuan;
    }

    public String getMaNhac() {
        return MaNhac;
    }

    public void setMaNhac(String maNhac) {
        MaNhac = maNhac;
    }
}
