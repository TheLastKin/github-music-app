package com.example.duan1_nhom2.Model;

public class NguoiDung {
    private String MaNguoiDung;
    private String TenHienThi;
    private String Password;
    private String Email;
    private String URLAnh;

    public NguoiDung() {
    }

    public NguoiDung(String MaNguoiDung, String TenHienThi, String Password, String Email, String URLAnh) {
        this.MaNguoiDung = MaNguoiDung;
        this.TenHienThi = TenHienThi;
        this.Password = Password;
        this.Email = Email;
        this.URLAnh = URLAnh;
    }

    public String getMaNguoiDung() {
        return MaNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        MaNguoiDung = maNguoiDung;
    }

    public String getTenHienThi() {
        return TenHienThi;
    }

    public void setTenHienThi(String tenHienThi) {
        TenHienThi = tenHienThi;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getURLAnh() {
        return URLAnh;
    }

    public void setURLAnh(String URLAnh) {
        this.URLAnh = URLAnh;
    }
}
