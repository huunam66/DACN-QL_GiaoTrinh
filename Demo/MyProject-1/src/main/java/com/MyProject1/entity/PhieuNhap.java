package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Setter
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name="phieunhap")
public class PhieuNhap {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name="ngay")
    private Date ngay;

    @Column(name="tongtien")
    private double tongtien;

    @Column(name="tinhtrang", columnDefinition = "Chưa duyệt")
    private String tinhtrang;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="nhanvien_id")
    private NhanVien nhanvien;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="nhacungcap_id")
    private NhaCungCap nhacungcap;

    @OneToMany(mappedBy = "phieunhap",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ChiTietPhieuNhap> chitietphieunhaps;

    @Column(name ="ghichu")
    private String ghichu;


    public PhieuNhap() {
    }

    public PhieuNhap(int id, Date ngay, double tongtien, String tinhtrang, NhanVien nhanvien, NhaCungCap nhacungcap, List<ChiTietPhieuNhap> chitietphieunhaps, String ghichu) {
        this.id = id;
        this.ngay = ngay;
        this.tongtien = tongtien;
        this.tinhtrang = tinhtrang;
        this.nhanvien = nhanvien;
        this.nhacungcap = nhacungcap;
        this.chitietphieunhaps = chitietphieunhaps;
        this.ghichu = ghichu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public NhanVien getNhanvien() {
        return nhanvien;
    }

    public void setNhanvien(NhanVien nhanvien) {
        this.nhanvien = nhanvien;
    }

    public NhaCungCap getNhacungcap() {
        return nhacungcap;
    }

    public void setNhacungcap(NhaCungCap nhacungcap) {
        this.nhacungcap = nhacungcap;
    }

    public List<ChiTietPhieuNhap> getChitietphieunhaps() {
        return chitietphieunhaps;
    }

    public void setChitietphieunhaps(List<ChiTietPhieuNhap> chitietphieunhaps) {
        this.chitietphieunhaps = chitietphieunhaps;
    }





    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }
}

