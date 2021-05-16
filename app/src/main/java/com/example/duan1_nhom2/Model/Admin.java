package com.example.duan1_nhom2.Model;

public class Admin {
    private String MaAdmin;
    private String TenAdmin;
    private String Password;
    private String Email;
    private String UrlAnh;

    public Admin() {
    }

    public Admin(String maAdmin, String tenAdmin, String password, String email, String urlAnh) {
        MaAdmin = maAdmin;
        TenAdmin = tenAdmin;
        Password = password;
        Email = email;
        UrlAnh = urlAnh;
    }

    public String getMaAdmin() {
        return MaAdmin;
    }

    public void setMaAdmin(String maAdmin) {
        MaAdmin = maAdmin;
    }

    public String getTenAdmin() {
        return TenAdmin;
    }

    public void setTenAdmin(String tenAdmin) {
        TenAdmin = tenAdmin;
    }

    public String getPassWord() {
        return Password;
    }

    public void setPassWord(String passWord) {
        Password = passWord;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUrlAnh() {
        return UrlAnh;
    }

    public void setUrlAnh(String urlAnh) {
        UrlAnh = urlAnh;
    }
}
