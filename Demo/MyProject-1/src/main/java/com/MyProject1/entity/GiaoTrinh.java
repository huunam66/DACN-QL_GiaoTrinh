package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "giaotrinh")
public class GiaoTrinh {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ten")
    private String ten;

    @Column(name = "tacgia")
    private String tacgia;

    @Column(name = "nhaxuatban")
    private String nhaxuatban;

    @Column(name = "namxuatban")
    private int namxuatban;

    @Column(name = "ngaytao")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date ngaytao;


    @Column(name = "gia")
    private double gia;

    @Column(name = "soluong")
    private int soluong;

    @Column(name = "hinhanh")
    private String hinhanh;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "chuyennganh_id")
    private ChuyenNganh chuyennganh;


    @OneToMany(mappedBy = "giaotrinh",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ChiTietPhieuNhap> chitietphieunhaps;


    @OneToMany(mappedBy = "giaotrinh",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ChiTietPhieuXuat> chitietphieuxuats;


    public GiaoTrinh(int id, String ten, String tacgia, String nhaxuatban, int namxuatban, Date ngaytao, double gia, int soluong, String hinhanh, ChuyenNganh chuyennganh) {
        this.id = id;
        this.ten = ten;
        this.tacgia = tacgia;
        this.nhaxuatban = nhaxuatban;
        this.namxuatban = namxuatban;
        this.ngaytao = ngaytao;
        this.gia = gia;
        this.soluong = soluong;
        this.hinhanh = hinhanh;
        this.chuyennganh = chuyennganh;
    }

    public GiaoTrinh(int id, String ten, String tacgia, String nhaxuatban, int namxuatban, Date ngaytao, double gia, int soluong, String hinhanh, ChuyenNganh chuyennganh, List<ChiTietPhieuNhap> chitietphieunhaps, List<ChiTietPhieuXuat> chitietphieuxuats) {
        this.id = id;
        this.ten = ten;
        this.tacgia = tacgia;
        this.nhaxuatban = nhaxuatban;
        this.namxuatban = namxuatban;
        this.ngaytao = ngaytao;
        this.gia = gia;
        this.soluong = soluong;
        this.hinhanh = hinhanh;
        this.chuyennganh = chuyennganh;
        this.chitietphieunhaps = chitietphieunhaps;
        this.chitietphieuxuats = chitietphieuxuats;
    }

    public GiaoTrinh() {
    }


    public List<ChiTietPhieuNhap> getChitietphieunhaps() {
        return chitietphieunhaps;
    }

    public void setChitietphieunhaps(List<ChiTietPhieuNhap> chitietphieunhaps) {
        this.chitietphieunhaps = chitietphieunhaps;
    }

    public List<ChiTietPhieuXuat> getChitietphieuxuats() {
        return chitietphieuxuats;
    }

    public void setChitietphieuxuats(List<ChiTietPhieuXuat> chitietphieuxuats) {
        this.chitietphieuxuats = chitietphieuxuats;
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

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public String getNhaxuatban() {
        return nhaxuatban;
    }

    public void setNhaxuatban(String nhaxuatban) {
        this.nhaxuatban = nhaxuatban;
    }

    public int getNamxuatban() {
        return namxuatban;
    }

    public void setNamxuatban(int namxuatban) {
        this.namxuatban = namxuatban;
    }

    public Date getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(Date ngaytao) {
        this.ngaytao = ngaytao;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public ChuyenNganh getChuyennganh() {
        return chuyennganh;
    }

    public void setChuyennganh(ChuyenNganh chuyennganh) {
        this.chuyennganh = chuyennganh;
    }
}


