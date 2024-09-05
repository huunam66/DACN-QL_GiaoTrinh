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
@Table(name="chitietphieuxuat")
public class ChiTietPhieuXuat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="soluong")
    private int soluong;

    @Column(name="dongia")
    private double dongia;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="phieuxuat_id")
    private PhieuXuat phieuxuat;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="giaotrinh_id")
    private GiaoTrinh giaotrinh;

    public ChiTietPhieuXuat() {
    }

    public ChiTietPhieuXuat(int id, int soluong, double dongia, PhieuXuat phieuxuat, GiaoTrinh giaotrinh) {
        this.id = id;
        this.soluong = soluong;
        this.dongia = dongia;
        this.phieuxuat = phieuxuat;
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

    public PhieuXuat getPhieuxuat() {
        return phieuxuat;
    }

    public void setPhieuxuat(PhieuXuat phieuxuat) {
        this.phieuxuat = phieuxuat;
    }

    public GiaoTrinh getGiaotrinh() {
        return giaotrinh;
    }

    public void setGiaotrinh(GiaoTrinh giaotrinh) {
        this.giaotrinh = giaotrinh;
    }
}

