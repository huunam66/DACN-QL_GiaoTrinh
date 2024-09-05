package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name="phieuxuat")
public class PhieuXuat {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="ngay")
    private Date ngay;

    @Column(name="tongtien")
    private double tongtien;

    @Column(name="trangthai", insertable = false)
    private boolean trangthai;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="nhanvien_id")
    private NhanVien nhanvien;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="lop_id")
    private Lop lop;

    @OneToMany(mappedBy = "phieuxuat",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    private List<ChiTietPhieuXuat> chitietphieuxuats;

    @Column(name="tennguoinhan")
    private String tennguoinhan;

    @Column(name="sdtnguoinhan")
    private String sdtnguoinhan;


    @Column(name ="ghichu")
    private String ghichu;

    public PhieuXuat() {
    }

    public PhieuXuat(int id, Date ngay, double tongtien, boolean trangthai, String lydohuy, NhanVien nhanvien, Lop lop, List<ChiTietPhieuXuat> chitietphieuxuats, String tennguoinhan, String ghichu) {
        this.id = id;
        this.ngay = ngay;
        this.tongtien = tongtien;
        this.trangthai = trangthai;
        this.nhanvien = nhanvien;
        this.lop = lop;
        this.chitietphieuxuats = chitietphieuxuats;
        this.tennguoinhan = tennguoinhan;
        this.ghichu = ghichu;
    }

    public PhieuXuat(int id, Date ngay, double tongtien, boolean trangthai, NhanVien nhanvien, Lop lop, List<ChiTietPhieuXuat> chitietphieuxuats, String tennguoinhan, String sdtnguoinhan, String ghichu) {
        this.id = id;
        this.ngay = ngay;
        this.tongtien = tongtien;
        this.trangthai = trangthai;
        this.nhanvien = nhanvien;
        this.lop = lop;
        this.chitietphieuxuats = chitietphieuxuats;
        this.tennguoinhan = tennguoinhan;
        this.sdtnguoinhan = sdtnguoinhan;
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

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }

    public NhanVien getNhanvien() {
        return nhanvien;
    }

    public void setNhanvien(NhanVien nhanvien) {
        this.nhanvien = nhanvien;
    }

    public Lop getLop() {
        return lop;
    }

    public void setLop(Lop lop) {
        this.lop = lop;
    }

    public String getSdtnguoinhan() {
        return sdtnguoinhan;
    }

    public void setSdtnguoinhan(String sdtnguoinhan) {
        this.sdtnguoinhan = sdtnguoinhan;
    }

    public List<ChiTietPhieuXuat> getChitietphieuxuats() {
        return chitietphieuxuats;
    }

    public void setChitietphieuxuats(List<ChiTietPhieuXuat> chitietphieuxuats) {
        this.chitietphieuxuats = chitietphieuxuats;
    }

    public String getTennguoinhan() {
        return tennguoinhan;
    }

    public void setTennguoinhan(String tennguoinhan) {
        this.tennguoinhan = tennguoinhan;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }
}
