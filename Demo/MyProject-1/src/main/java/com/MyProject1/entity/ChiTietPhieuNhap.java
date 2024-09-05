package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name="chitietphieunhap")
public class ChiTietPhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="soluong")
    private int soluong;

    @Column(name="dongia")
    private double dongia;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="phieunhap_id")
    private PhieuNhap phieunhap;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="giaotrinh_id")
    private GiaoTrinh giaotrinh;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(int id, int soluong, double dongia, PhieuNhap phieunhap, GiaoTrinh giaotrinh) {
        this.id = id;
        this.soluong = soluong;
        this.dongia = dongia;
        this.phieunhap = phieunhap;
        this.giaotrinh = giaotrinh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getDongia() {
        return dongia;
    }

    public void setDongia(double dongia) {
        this.dongia = dongia;
    }

    public PhieuNhap getPhieunhap() {
        return phieunhap;
    }

    public void setPhieunhap(PhieuNhap phieunhap) {
        this.phieunhap = phieunhap;
    }

    public GiaoTrinh getGiaotrinh() {
        return giaotrinh;
    }

    public void setGiaotrinh(GiaoTrinh giaotrinh) {
        this.giaotrinh = giaotrinh;
    }
}

