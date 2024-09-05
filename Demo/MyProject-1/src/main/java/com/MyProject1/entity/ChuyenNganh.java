package com.MyProject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name="chuyennganh")
public class ChuyenNganh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="tenchuyennganh")
    private String tenchuyennganh;


    @OneToMany(mappedBy = "chuyennganh",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private List<GiaoTrinh> giaoTrinhs;

    public ChuyenNganh(int id, String tenchuyennganh, List<GiaoTrinh> giaoTrinhs) {
        this.id = id;
        this.tenchuyennganh = tenchuyennganh;
        this.giaoTrinhs = giaoTrinhs;
    }

    public ChuyenNganh() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenchuyennganh() {
        return tenchuyennganh;
    }

    public void setTenchuyennganh(String tenchuyennganh) {
        this.tenchuyennganh = tenchuyennganh;
    }


    public List<GiaoTrinh> getGiaoTrinhs() {
        return giaoTrinhs;
    }

    public void setGiaoTrinhs(List<GiaoTrinh> giaoTrinhs) {
        this.giaoTrinhs = giaoTrinhs;
    }
}


