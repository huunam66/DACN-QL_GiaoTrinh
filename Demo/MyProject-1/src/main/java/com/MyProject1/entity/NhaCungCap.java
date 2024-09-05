package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name="nhacungcap")
public class NhaCungCap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="ten")
    private String ten;

    @Column(name="diachi")
    private String diachi;

    @Column(name="phuongxa")
    private String phuongxa;

    @Column(name="quanhuyen")
    private String quanhuyen;

    @Column(name="tinhthanh")
    private String tinhthanh;

    @Column(name="sdt")
    private String sdt;

    @Column(name="email")
    private String email;


    @OneToMany(mappedBy = "nhacungcap",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<PhieuNhap> phieunhaps;


    public NhaCungCap() {
    }

    public NhaCungCap(int id, String ten, String diachi, String phuongxa, String quanhuyen, String tinhthanh, String sdt, String email, List<PhieuNhap> phieunhaps) {
        this.id = id;
        this.ten = ten;
        this.diachi = diachi;
        this.phuongxa = phuongxa;
        this.quanhuyen = quanhuyen;
        this.tinhthanh = tinhthanh;
        this.sdt = sdt;
        this.email = email;
        this.phieunhaps = phieunhaps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getPhuongxa() {
        return phuongxa;
    }

    public void setPhuongxa(String phuongxa) {
        this.phuongxa = phuongxa;
    }

    public String getQuanhuyen() {
        return quanhuyen;
    }

    public void setQuanhuyen(String quanhuyen) {
        this.quanhuyen = quanhuyen;
    }

    public String getTinhthanh() {
        return tinhthanh;
    }

    public void setTinhthanh(String tinhthanh) {
        this.tinhthanh = tinhthanh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhieuNhap> getPhieunhaps() {
        return phieunhaps;
    }

    public void setPhieunhaps(List<PhieuNhap> phieunhaps) {
        this.phieunhaps = phieunhaps;
    }
}

