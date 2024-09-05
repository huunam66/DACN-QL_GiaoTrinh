package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name="taikhoan")
public class TaiKhoan {

    @Id
    @Column(name = "nhanvien_id")
    private int nhanvien_id;

    @JoinColumn(name="nhanvien_id")
    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private NhanVien nhanvien;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @OneToMany(mappedBy = "taiKhoan",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Quyen> Quyen;


    public TaiKhoan() {
    }

    public TaiKhoan(int nhanvien_id, NhanVien nhanvien, String username, String password, List<com.MyProject1.entity.Quyen> quyen) {
        this.nhanvien_id = nhanvien_id;
        this.nhanvien = nhanvien;
        this.username = username;
        this.password = password;
        Quyen = quyen;
    }

    public int getNhanvien_id() {
        return nhanvien_id;
    }

    public void setNhanvien_id(int nhanvien_id) {
        this.nhanvien_id = nhanvien_id;
    }

    public NhanVien getNhanvien() {
        return nhanvien;
    }

    public void setNhanvien(NhanVien nhanvien) {
        this.nhanvien = nhanvien;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<com.MyProject1.entity.Quyen> getQuyen() {
        return Quyen;
    }

    public void setQuyen(List<com.MyProject1.entity.Quyen> quyen) {
        Quyen = quyen;
    }
}
