package com.MyProject1.service;

import com.MyProject1.config.session.SessionManager;
import com.MyProject1.entity.*;
import com.MyProject1.repositories.ChiTietPhieuXuatRepository;
import com.MyProject1.repositories.PhieuXuatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PhieuXuatServiceImpl implements PhieuXuatService {
    private final PhieuXuatRepository phieuXuatRepository;
    @Autowired
    private ChiTietPhieuXuatRepository chiTietPhieuXuatRepository;
    private final SessionManager sessionManager;
    private final NhanVienService nhanVienService;

    private final GiaoTrinhService giaoTrinhService;

    @Autowired
    private LopService lopService;

    @Autowired
    public PhieuXuatServiceImpl(PhieuXuatRepository phieuXuatRepository, SessionManager sessionManager, NhanVienService nhanVienService, GiaoTrinhService giaoTrinhService) {
        this.phieuXuatRepository = phieuXuatRepository;
        this.sessionManager = sessionManager;
        this.nhanVienService = nhanVienService;
        this.giaoTrinhService = giaoTrinhService;
    }
    @Override
    public List<PhieuXuat> findAll() {
        return phieuXuatRepository.findAll();
    }


    @Transactional
    @Override
    public void save(PhieuXuat phieuXuat) {
        phieuXuatRepository.save(phieuXuat);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        phieuXuatRepository.deleteById(id);
    }


    @Override
    public GiaoTrinh update_count_giaotrinh_tang(ModelMap model) {
        try{
            int id_giaotrinh = Integer.parseInt(model.getAttribute("id_giaotrinh").toString());
            int soluong_chon_xuat = Integer.parseInt(model.getAttribute("soluong_chon_xuat").toString());
            GiaoTrinh gt = giaoTrinhService.findById(id_giaotrinh);
            gt.setSoluong(gt.getSoluong() + soluong_chon_xuat);
            return giaoTrinhService.save(gt);
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    @Override
    public GiaoTrinh update_count_giaotrinh_giam(ModelMap model) {
        try{
            int id_giaotrinh = Integer.parseInt(model.getAttribute("id_giaotrinh").toString());
            int soluong_chon_xuat = Integer.parseInt(model.getAttribute("soluong_chon_xuat").toString());
            GiaoTrinh gt = giaoTrinhService.findById(id_giaotrinh);
            gt.setSoluong(gt.getSoluong() - soluong_chon_xuat);
            return giaoTrinhService.save(gt);
        }
            catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    @Transactional
    @Override
    public Boolean tao_phieu(ModelMap model) {
        //làm giống phiếu nhập nhưng thay int nhacungcap_id bằng Lop lop
        try{
            int lop_id = Integer.parseInt(model.getAttribute("lop_id").toString());
            Lop lop = lopService.findById(lop_id);
            System.err.println(lop_id);
            System.err.println(lop.getId());
            String ghichu =  model.get("ghichu").toString();
            String tennguoinhan = model.get("tennguoinhan").toString();
            String sdtnguoinhan = model.get("sdtnguoinhan").toString();
            List<Map<String, Object>> chitietphieuxuats = (List<Map<String, Object>>)model.get("chitietphieuxuats");
            PhieuXuat phieuXuat = new PhieuXuat();
            phieuXuat.setGhichu(ghichu);
            phieuXuat.setLop(lop);

            phieuXuat.setNgay(Date.from(Instant.now()));
            phieuXuat.setTennguoinhan(tennguoinhan);
            phieuXuat.setSdtnguoinhan(sdtnguoinhan);
            int nhanvien_id = sessionManager.get_session_id_token();
            phieuXuat.setNhanvien(nhanVienService.findById(nhanvien_id));
            phieuXuat = phieuXuatRepository.save(phieuXuat);
            if(phieuXuat != null){
                double tongtien = 0;
                List<ChiTietPhieuXuat> chiTietPhieuXuatList = new ArrayList<>();
                for(Map<String, Object> ct : chitietphieuxuats){
                    ChiTietPhieuXuat chiTietPhieuXuat = new ChiTietPhieuXuat();
                    chiTietPhieuXuat.setPhieuxuat(phieuXuat);
                    chiTietPhieuXuat.setDongia(Double.parseDouble(ct.get("dongia").toString()));
                    chiTietPhieuXuat.setSoluong(Integer.parseInt(ct.get("soluong").toString()));
                    chiTietPhieuXuat.setGiaotrinh(
                            giaoTrinhService.findById(
                                    Integer.parseInt(ct.get("giaotrinh_id").toString())
                            ));
                    tongtien += (chiTietPhieuXuat.getDongia() * chiTietPhieuXuat.getSoluong());
                    chiTietPhieuXuatList.add(chiTietPhieuXuat);
                }
                phieuXuat.setTongtien(tongtien);
                phieuXuat.setChitietphieuxuats(chiTietPhieuXuatList);

                PhieuXuat xuat = phieuXuatRepository.save(phieuXuat);
                if(xuat != null) {
                    int phieuxuat_id = xuat.getId();
                    chiTietPhieuXuatList = chiTietPhieuXuatRepository.findAll().stream().filter(
                            item -> item.getPhieuxuat() != null && item.getPhieuxuat().getId() == phieuxuat_id
                    ).toList();
                    chiTietPhieuXuatList.forEach(item -> {
                        GiaoTrinh gt = giaoTrinhService.findById(item.getGiaotrinh().getId());
                        gt.setSoluong(gt.getSoluong() - item.getSoluong());
                        giaoTrinhService.save(gt);
                    });
                    xuat.setTrangthai(true);
                    xuat = phieuXuatRepository.save(xuat);
                    if(xuat != null) return true;
                }
                phieuXuatRepository.deleteById(phieuXuat.getId());
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    @Override
    public PhieuXuat get_phieu_xuat(int id) {
        try{
            Optional<PhieuXuat> optionalPhieuXuat = phieuXuatRepository.findById(id);

            if(!optionalPhieuXuat.isPresent()) return null;

            PhieuXuat phieuXuat = optionalPhieuXuat.get();
            List<ChiTietPhieuXuat> chiTietPhieuXuatList = phieuXuat.getChitietphieuxuats();
            if(chiTietPhieuXuatList != null && chiTietPhieuXuatList.size() > 0)
                return phieuXuat;

            chiTietPhieuXuatList = chiTietPhieuXuatRepository.findAll().stream().filter(
                    item -> item.getPhieuxuat() != null &&  item.getPhieuxuat().getId() == id
            ).toList();

            phieuXuat.setChitietphieuxuats(chiTietPhieuXuatList);
            return phieuXuat;
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public List<PhieuXuat> thongke_phieuxuat_theothang(int year, int month) {
        SimpleDateFormat dateFormat_year = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat_month = new SimpleDateFormat("MM");
        List<PhieuXuat> phieuXuat_thang = phieuXuatRepository.findAll().stream().filter(
                item ->
                        Integer.parseInt(dateFormat_year.format(item.getNgay()).toString()) == year
                                &&
                        Integer.parseInt(dateFormat_month.format(item.getNgay()).toString()) == month
        ).toList();
        return phieuXuat_thang;
    }

    @Override
    public List<PhieuXuat> thongke_phieuxuat_theonam(int year) {
        SimpleDateFormat dateFormat_year = new SimpleDateFormat("yyyy");
        List<PhieuXuat> phieuXuat_nam = phieuXuatRepository.findAll().stream().filter(
                item -> Integer.parseInt(dateFormat_year.format(item.getNgay()).toString()) == year
        ).toList();
        return phieuXuat_nam;
    }

    @Override
    public int tongSoGiaoTrinh(int id) {
        return chiTietPhieuXuatRepository.findAll().stream().filter(
                item ->
                        item.getPhieuxuat() != null
                                && item.getPhieuxuat().getId() == id
        ).toList().stream().mapToInt(ChiTietPhieuXuat::getSoluong).sum();
    }
}
