package com.MyProject1.service;

import com.MyProject1.config.roles.RolesStore;
import com.MyProject1.config.session.SessionManager;
import com.MyProject1.repositories.QuyenRepository;
import com.MyProject1.repositories.TaiKhoanRepository;
import com.MyProject1.entity.Quyen;
import com.MyProject1.entity.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("TaiKhoanServiceImple")
public class TaiKhoanServiceImple implements TaiKhoanService {

    @Autowired
    SessionManager session;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private QuyenRepository quyenRepository;

    @Override
    public TaiKhoan save(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    @Override
    public TaiKhoan findById(int id) {
        return taiKhoanRepository.findById(id).get();
    }

    @Override
    public List<TaiKhoan> List_TaiKhoan() {
        try {
            return taiKhoanRepository.findAll();
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public Boolean account_exists(int id) {
        Optional<TaiKhoan> taiKhoan = taiKhoanRepository.findById(id);
        return taiKhoan.isPresent();
    }

    @Override
    public String get_username(int id) {
        Optional<TaiKhoan> taiKhoan = taiKhoanRepository.findById(id);
        if (taiKhoan.isPresent())
            return taiKhoan.get().getUsername();
        return null;
    }

    @Override
    public Boolean change_pw(int id, String pw) {
        Optional<TaiKhoan> taiKhoan = taiKhoanRepository.findById(id);
        if (taiKhoan.isPresent()) {
            TaiKhoan tk = taiKhoan.get();
            tk.setPassword(pw);
            tk = taiKhoanRepository.save(tk);
            if (tk.getPassword().equals(pw))
                return true;
        }
        return false;
    }

    @Override
    public Boolean roles_is_manager(int id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).get();
        Boolean is_manager = taiKhoan.getQuyen().stream().filter(
                item -> item.getTen().toUpperCase().equals("MANAGER")
        ).findFirst().isPresent();
        return is_manager;
    }

    @Override
    public Boolean constraint_username(String username) {
        Optional<TaiKhoan> op = taiKhoanRepository.findAll()
                .stream().filter(item -> item.getUsername().equals(username)).findFirst();
        return op.isPresent();
    }


    @Override
    public Boolean update_account(int id, String username) {
        try {
            TaiKhoan taiKhoan = taiKhoanRepository.findById(id).get();

            taiKhoan.setUsername(username);
            taiKhoan = taiKhoanRepository.save(taiKhoan);
            if (taiKhoan != null) {
                List<Quyen> quyens = new ArrayList<>();
                Quyen q = new Quyen();
                q.setTen("USER");
                q.setUsername(username);
                q.setTaiKhoan(taiKhoan);
                quyens.add(q);
                taiKhoan.setQuyen(quyens);
                taiKhoan = taiKhoanRepository.save(taiKhoan);

                if (taiKhoan != null) {
                    if(session.addUserDetails(taiKhoan))
                        return true;
                }

                taiKhoanRepository.delete(
                        taiKhoanRepository.findById(id).get());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    @Override
    public Boolean create_account(int id, String username, String password) {
        try {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setUsername(username);
            taiKhoan.setPassword(password);
            taiKhoan.setNhanvien_id(id);
            taiKhoan = taiKhoanRepository.save(taiKhoan);
            if (taiKhoan != null) {
                List<Quyen> quyens = new ArrayList<>();
                for(String role : RolesStore.Roles_user){
                    Quyen q = new Quyen();
                    q.setTen(role);
                    q.setUsername(username);
                    q.setTaiKhoan(taiKhoan);
                    quyens.add(q);
                };
                taiKhoan.setQuyen(quyens);
                taiKhoan = taiKhoanRepository.save(taiKhoan);

                if (taiKhoan != null) {
                    if(session.addUserDetails(taiKhoan))
                        return true;
                }

                taiKhoanRepository.delete(
                        taiKhoanRepository.findById(id).get());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    @Override
    public Boolean update_roles_grant(int id, String new_role) {
        List<String> old_roles = (List<String>)session.get_session_roles_token();
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).get();
        try{
            delete_all_roles(taiKhoan);

            List<String> roles = new_role.equals(RolesStore.manager) ? RolesStore.Roles_manager : RolesStore.Roles_user;

            Boolean grant_new = set_roles(taiKhoan, roles);
            if(grant_new){
                Boolean grant_on_server = session.grantRoles_UserDetails(taiKhoan.getQuyen(), taiKhoan.getUsername());
                if(grant_on_server) return true;
            }

            rollback_transaction_role(taiKhoan, old_roles);
        }
        catch (Exception e){
            rollback_transaction_role(taiKhoan, old_roles);
            System.err.println(e);
        }
        return false;
    }


    private void rollback_transaction_role(TaiKhoan taiKhoan, List<String> old_roles){
        delete_all_roles(taiKhoan);
        set_roles(taiKhoan, old_roles);
        session.grantRoles_UserDetails(taiKhoan.getQuyen(), taiKhoan.getUsername());
    }


    private void delete_all_roles(TaiKhoan taiKhoan){
        List<Quyen> quyens = taiKhoan.getQuyen();
        if(quyens != null && quyens.size() > 0){
            for(Quyen q : quyens){
                taiKhoan.getQuyen().set(taiKhoan.getQuyen().indexOf(q), null);
                q.setTaiKhoan(null);
                quyenRepository.deleteById(q.getId());
            }
        }
    }
    private Boolean set_roles(TaiKhoan taiKhoan, List<String> roles){
        List<Quyen> quyens = new ArrayList<>();

        for(String role : roles){
            Quyen q = new Quyen();
            q.setTaiKhoan(taiKhoan);
            q.setTen(role);
            q.setUsername(taiKhoan.getUsername());
            quyens.add(q);
        }

        taiKhoan.setQuyen(quyens);
        taiKhoan = taiKhoanRepository.save(taiKhoan);
        if(taiKhoan != null) return true;

        return false;
    }
}
