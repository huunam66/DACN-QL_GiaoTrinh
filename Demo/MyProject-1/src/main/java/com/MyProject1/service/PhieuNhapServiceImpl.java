package com.MyProject1.service;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.ChiTietPhieuNhap;
import com.MyProject1.entity.GiaoTrinh;
import com.MyProject1.entity.NhaCungCap;
import com.MyProject1.repositories.ChiTietPhieuNhapRepository;
import com.MyProject1.repositories.NhaCungCapRepository;
import com.MyProject1.repositories.NhanVienRepository;
import com.MyProject1.repositories.PhieuNhapRepository;
import com.MyProject1.entity.PhieuNhap;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Service
public class PhieuNhapServiceImpl implements PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;

    @Autowired
    private ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private NhaCungCapService nhaCungCapService;

    @Autowired
    private NhanVienService nhanVienService;

    @Autowired
    private GiaoTrinhService giaoTrinhService;


    @Autowired
    public PhieuNhapServiceImpl(PhieuNhapRepository phieuNhapRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
    }

    @Override
    public List<PhieuNhap> findAll() {
        return phieuNhapRepository.findAll();
    }


    @Transactional
    @Override
    public void save(PhieuNhap phieuNhap) {
        phieuNhapRepository.save(phieuNhap);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        phieuNhapRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Boolean tao_phieu(ModelMap model) {
        try{
            int nhacungcap_id = Integer.parseInt(model.get("nhacungcap_id").toString());
            String ghichu =  model.get("ghichu").toString();
            List<Map<String, Object>> chitietphieunhaps = (List<Map<String, Object>>)model.get("chitietphieunhaps");
            NhaCungCap nhaCungCap = nhaCungCapService.findById(nhacungcap_id);
            PhieuNhap phieuNhap = new PhieuNhap();
            phieuNhap.setGhichu(ghichu);
            phieuNhap.setNhacungcap(nhaCungCap);
            phieuNhap.setNgay(Date.from(Instant.now()));
            int nhanvien_id = sessionManager.get_session_id_token();
            phieuNhap.setNhanvien(nhanVienService.findById(nhanvien_id));
            phieuNhap = phieuNhapRepository.save(phieuNhap);
            if(phieuNhap != null){
                double tongtien = 0;
                List<ChiTietPhieuNhap> chiTietPhieuNhapList = new ArrayList<>();
                for(Map<String, Object> ct : chitietphieunhaps){
                    ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
                    chiTietPhieuNhap.setPhieunhap(phieuNhap);
                    chiTietPhieuNhap.setDongia(Double.parseDouble(ct.get("dongia").toString()));
                    chiTietPhieuNhap.setSoluong(Integer.parseInt(ct.get("soluong").toString()));
                    chiTietPhieuNhap.setGiaotrinh(
                            giaoTrinhService.findById(
                                    Integer.parseInt(ct.get("giaotrinh_id").toString())
                            ));
                    tongtien += (chiTietPhieuNhap.getDongia() * chiTietPhieuNhap.getSoluong());
                    chiTietPhieuNhapList.add(chiTietPhieuNhap);
                }
                phieuNhap.setTongtien(tongtien);
                phieuNhap.setChitietphieunhaps(chiTietPhieuNhapList);

                phieuNhap.setTinhtrang("Chưa duyệt");
                
                PhieuNhap nhap = phieuNhapRepository.save(phieuNhap);
                if(nhap != null) return true;

                phieuNhapRepository.deleteById(phieuNhap.getId());
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
        return false;
    }

    @Override
    public PhieuNhap get_phieu_nhap(int id) {
        try{
            Optional<PhieuNhap> optionalPhieuNhap = phieuNhapRepository.findById(id);

            if(!optionalPhieuNhap.isPresent()) return null;

            PhieuNhap phieuNhap = optionalPhieuNhap.get();
            List<ChiTietPhieuNhap> chiTietPhieuNhapList = phieuNhap.getChitietphieunhaps();
            if(chiTietPhieuNhapList != null && chiTietPhieuNhapList.size() > 0)
                return phieuNhap;

            chiTietPhieuNhapList = chiTietPhieuNhapRepository.findAll().stream().filter(
                    item -> item.getPhieunhap().getId() == id
            ).toList();

            phieuNhap.setChitietphieunhaps(chiTietPhieuNhapList);
            return phieuNhap;
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public List<PhieuNhap> thongke_phieunhap_theothang(int year, int month) {
        SimpleDateFormat dateFormat_year = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat_month = new SimpleDateFormat("MM");
        List<PhieuNhap> phieuNhap_thang = phieuNhapRepository.findAll().stream().filter(
                item ->
                        Integer.parseInt(dateFormat_year.format(item.getNgay()).toString()) == year
                        &&
                        Integer.parseInt(dateFormat_month.format(item.getNgay()).toString()) == month
        ).toList();
        return phieuNhap_thang;
    }

    @Override
    public List<PhieuNhap> thongke_phieunhap_theonam(int year) {
        SimpleDateFormat dateFormat_year = new SimpleDateFormat("yyyy");
        List<PhieuNhap> phieuNhap_nam = phieuNhapRepository.findAll().stream().filter(
                item -> Integer.parseInt(dateFormat_year.format(item.getNgay()).toString()) == year
        ).toList();
        return phieuNhap_nam;
    }

    @Override
    public int tongSoGiaoTrinh(int id) {
        return chiTietPhieuNhapRepository.findAll().stream().filter(
                item ->
                item.getPhieunhap() != null
                        && item.getPhieunhap().getId() == id
        ).toList().stream().mapToInt(ChiTietPhieuNhap::getSoluong).sum();
    }

    @Override
    public Boolean duyetPhieu(ModelMap model) {

        try{
            int id = Integer.parseInt(model.getAttribute("id").toString());
            String tinhtrang = model.getAttribute("tinhtrang").toString();

            PhieuNhap phieuNhap = phieuNhapRepository.findById(id).get();
            phieuNhap.setTinhtrang(tinhtrang);
            phieuNhap = phieuNhapRepository.save(phieuNhap);
            if(phieuNhap != null){
                int id_phieuNhap = phieuNhap.getId();
                List<ChiTietPhieuNhap> chiTietPhieuNhapList = chiTietPhieuNhapRepository.findAll().stream().filter(
                        item -> item.getPhieunhap() != null && item.getPhieunhap().getId() == id_phieuNhap
                ).toList();
                for(ChiTietPhieuNhap ctpn : chiTietPhieuNhapList){
                    GiaoTrinh gt = giaoTrinhService.findById(ctpn.getGiaotrinh().getId());
                    gt.setSoluong(gt.getSoluong() + ctpn.getSoluong());
                    double gia_chiet_khau_5_percent = Math.round(ctpn.getDongia() + ctpn.getDongia() * (float)5/100);
                    gt.setGia(gia_chiet_khau_5_percent);
                    giaoTrinhService.save(gt);
                }
            }
            return true;
        }
        catch (Exception e){
            System.err.println(e);
        }
        return false;
    }

    @Override
    public Boolean huyPhieu(ModelMap model) {
        try{
            int id = Integer.parseInt(model.getAttribute("id").toString());
            String tinhtrang = model.getAttribute("tinhtrang").toString();

            PhieuNhap phieuNhap = phieuNhapRepository.findById(id).get();
            phieuNhap.setTinhtrang(tinhtrang);
            phieuNhapRepository.save(phieuNhap);
            return true;
        }
        catch (Exception e){
            System.err.println(e);
        }
        return false;
    }
}
