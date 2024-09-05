package com.MyProject1.entity;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


//@NoArgsConstructor
//@AllArgsConstructor
//@Setter
//@Getter
@Entity
@Table(name="nhanvien")
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="mssv", unique = true)
    private String mssv;

    @Column(name="ten")
    private String ten;

    @Column(name="sdt")
    private String sdt;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "nhanvien", cascade = CascadeType.ALL)
    private TaiKhoan taiKhoan;


    @Getter
    @OneToMany(mappedBy = "nhanvien",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<PhieuNhap> phieunhaps;

    @Getter
    @OneToMany(mappedBy = "nhanvien",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<PhieuXuat> phieuxuats;



    public NhanVien() {
    }

    public NhanVien(int id, String mssv, String ten, String sdt, @Nullable TaiKhoan taiKhoan) {
        this.id = id;
        this.mssv = mssv;
        this.ten = ten;
        this.sdt = sdt;
        this.taiKhoan = taiKhoan;
    }

    public NhanVien(int id, String mssv, String ten, String sdt, TaiKhoan taiKhoan, List<PhieuNhap> phieunhaps, List<PhieuXuat> phieuxuats) {
        this.id = id;
        this.mssv = mssv;
        this.ten = ten;
        this.sdt = sdt;
        this.taiKhoan = taiKhoan;
        this.phieunhaps = phieunhaps;
        this.phieuxuats = phieuxuats;
    }

    public void setPhieunhaps(List<PhieuNhap> phieunhaps) {
        this.phieunhaps = phieunhaps;
    }

    public void setPhieuxuats(List<PhieuXuat> phieuxuats) {
        this.phieuxuats = phieuxuats;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Nullable
    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(@Nullable TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public int getId() {
        return id;
    }

    public String getMssv() {
        return mssv;
    }

    public String getTen() {
        return ten;
    }

    public String getSdt() {
        return sdt;
    }
}
