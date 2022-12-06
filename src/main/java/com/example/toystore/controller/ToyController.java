package com.example.toystore.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.toystore.model.Mainan;
import com.example.toystore.model.Admin;
import com.example.toystore.model.Pembeli;
import com.example.toystore.model.Transaksi;

@Controller
public class ToyController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getUser() {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String login(@ModelAttribute(name = "Admin") Admin admin, Model model) {
        String username = admin.getUsername();
        String password = admin.getPassword();
        try {
            String sql = "SELECT * FROM admin WHERE username = ?";
            Admin asli = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Admin.class), username);
            model.addAttribute("asli", asli);
            String userAsli = asli.getUsername();
            String passAsli = asli.getPassword();
            if (password.equals(passAsli)) {
                return "redirect:/index";
            }
        } catch (EmptyResultDataAccessException e) {
            // TODO: handle exception
            model.addAttribute("invalidCredentials", true);
        }
        model.addAttribute("invalidCredentials", true);
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model) {
        String sql = "SELECT * FROM pembeli WHERE hapus='TIDAK'";
        List<Pembeli> pembeliList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Pembeli.class));
        model.addAttribute("pembeli", pembeliList);
        return "index";
    }

    @GetMapping("/search")
    public String search(@PathParam("nama_pembeli") String nama_pembeli, Model model) {
        String sql = "SELECT * FROM pembeli WHERE LOWER(nama_pembeli) LIKE CONCAT(CONCAT ('%', ?), '%')";
        List<Pembeli> pembeliData = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Pembeli.class), nama_pembeli);
        model.addAttribute("pembeli", pembeliData);
        return ("search");
    }

    @GetMapping("/mainan")
    public String adminList(Model model) {
        String sql = "SELECT * FROM mainan";
        List<Mainan> mainanList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Mainan.class));
        model.addAttribute("mainan", mainanList);
        return "mainan";
    }

    @GetMapping("/softdeletepembeli/{id_pembeli}")
    public String softDelete(@PathVariable("id_pembeli") String id_pembeli) {
        String sql = "UPDATE pembeli SET hapus = 'YA' WHERE id_pembeli = ?";
        jdbcTemplate.update(sql, id_pembeli);
        return "redirect:/index";
    }

    @GetMapping("/restore/{id_pembeli}")
    public String restore(@PathVariable("id_pembeli") String id_pembeli) {
        String sql = "UPDATE pembeli SET hapus = 'TIDAK' WHERE id_pembeli = ?";
        jdbcTemplate.update(sql, id_pembeli);
        return "redirect:/index";
    }

    @GetMapping("/sampah")
    public String recycle(Model model) {
        String sql = "SELECT * FROM pembeli WHERE hapus='YA'";
        List<Pembeli> pembeliList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Pembeli.class));
        model.addAttribute("pembeli", pembeliList);
        return "sampah";
    }

    @GetMapping("/harddeletepembeli/{id_pembeli}")
    public String harddelete(@PathVariable("id_pembeli") String id_pembeli) {
        String sql = "DELETE FROM pembeli WHERE id_pembeli = ?";
        jdbcTemplate.update(sql, id_pembeli);
        return "redirect:/index";
    }

    @GetMapping("/addpembeli")
    public String addPembeli(Model model) {
        return "addpembeli";
    }

    @RequestMapping(value ="/addpembeli")
    public String addPembeli(Pembeli pembeli, Model model) {
        String sql = "INSERT INTO pembeli VALUES (?, ?, ?, 'TIDAK')";
        jdbcTemplate.update(sql,
                pembeli.getId_pembeli(),pembeli.getNama_pembeli(),  pembeli.getAlamat_pembeli());
        return "redirect:/index";
    }

    @GetMapping("/editpembeli/{id_pembeli}")
    public String editPembeli(@PathVariable("id_pembeli") String id_pembeli, Model model) {
        String sql = "SELECT * FROM pembeli WHERE id_pembeli = ?";
        Pembeli pembeli = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Pembeli.class), id_pembeli);
        model.addAttribute("pembeli", pembeli);
        return "editpembeli";
    }

    @PostMapping("/editpembeli")
    public String editPembeli(Pembeli pembeli) {
        String sql = "UPDATE pembeli SET id_pembeli=?, nama_pembeli = ?, alamat_pembeli= ? WHERE id_pembeli = ?";
        jdbcTemplate.update(sql, pembeli.getId_pembeli(),pembeli.getNama_pembeli(),pembeli.getAlamat_pembeli(),pembeli.getId_pembeli());
        return "redirect:/index";
    }

    @GetMapping("/transaksi/{id_pembeli}")
    public String transaksiList(@PathVariable("id_pembeli") String id_pembeli, Model model) {
        String sql = "SELECT * FROM transaksi WHERE id_pembeli = ?";
        List<Transaksi> transaksiList = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Transaksi.class), id_pembeli);
        model.addAttribute("transaksi", transaksiList);
        return "transaksi";
    }

    @GetMapping("/addtransaksi")
    public String addTransaksi(Model model) {
        return "addtransaksi";
    }

    @RequestMapping(value ="/addtransaksi")
    public String addTransaksi(Transaksi transaksi, Model model) {

        String sql = "INSERT INTO transaksi VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transaksi.getId_transaksi(), transaksi.getId_pembeli(), transaksi.getId_mainan(), transaksi.getJml_mainan(), transaksi.getPembayaran(), transaksi.getTgl_transaksi());
        return "redirect:/index";
    }

    @GetMapping("/harddeletetransaksi/{id_transaksi}")
    public String harddeletetransaksi(@PathVariable("id_transaksi") String id_transaksi) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";
        jdbcTemplate.update(sql, id_transaksi);
        return "redirect:/index";
    }

    @GetMapping("/detailtransaksimainan/{id_transaksi}")
    public String detailtransaksi(@PathVariable("id_transaksi") String id_transaksi, Model model) {
        String sql = "SELECT * FROM transaksi JOIN mainan ON (transaksi.id_mainan = mainan.id_mainan) WHERE id_transaksi = ?";
        Transaksi transaksi = jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(Transaksi.class), id_transaksi);
        Mainan mainan = jdbcTemplate.queryForObject(sql,
                BeanPropertyRowMapper.newInstance(Mainan.class), id_transaksi);
        model.addAttribute("transaksi", transaksi);
        model.addAttribute("mainan", mainan);
        return "detailtransaksimainan";
    }
}
