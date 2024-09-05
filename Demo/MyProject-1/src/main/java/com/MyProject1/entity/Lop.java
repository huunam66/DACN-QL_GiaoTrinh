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
@Table(name="lop")
public class Lop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="ten")
    private String ten;

    @Column(name="monhoc")
    private String monhoc;

    @Column(name = "giangvien")
    private String giangvien;

    @Column(name = "phonghoc")
    private String phonghoc;

    @Column(name = "thoigianhoc")
    private String thoigianhoc;

    @Column(name = "namhoc")
    private String namhoc;

    @OneToMany(mappedBy = "lop",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<PhieuXuat> phieuxuats;

    public Lop() {
    }

    public Lop(int id, String ten, String monhoc, String giangvien, String phonghoc, String thoigianhoc, String namhoc, List<PhieuXuat> phieuxuats) {
        this.id = id;
        this.ten = ten;
        this.monhoc = monhoc;
        this.giangvien = giangvien;
        this.phonghoc = phonghoc;
        this.thoigianhoc = thoigianhoc;
        this.namhoc = namhoc;
        this.phieuxuats = phieuxuats;
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

    public String getMonhoc() {
        return monhoc;
    }

    public void setMonhoc(String monhoc) {
        this.monhoc = monhoc;
    }

    public String getGiangvien() {
        return giangvien;
    }

    public void setGiangvien(String giangvien) {
        this.giangvien = giangvien;
    }

    public String getPhonghoc() {
        return phonghoc;
    }

    public void setPhonghoc(String phonghoc) {
        this.phonghoc = phonghoc;
    }

    public String getThoigianhoc() {
        return thoigianhoc;
    }

    public void setThoigianhoc(String thoigianhoc) {
        this.thoigianhoc = thoigianhoc;
    }

    public String getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
    }

    public List<PhieuXuat> getPhieuxuats() {
        return phieuxuats;
    }

    public void setPhieuxuats(List<PhieuXuat> phieuxuats) {
        this.phieuxuats = phieuxuats;
    }
}

