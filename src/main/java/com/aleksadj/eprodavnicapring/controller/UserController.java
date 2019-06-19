package com.aleksadj.eprodavnicapring.controller;

import com.aleksadj.eprodavnicapring.repository.IzlogRepository;
import com.aleksadj.eprodavnicapring.repository.KorisnikRepository;
import com.aleksadj.eprodavnicapring.repository.ProizvodRepository;
import com.aleksadj.eprodavnicapring.repository.ProizvodjacRepository;
import model.ADJIzlog;
import model.ADJKorisnik;
import model.ADJProizvod;
import model.ADJProizvodjac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    KorisnikRepository kr;

    @Autowired
    IzlogRepository ir;

    @Autowired
    ProizvodjacRepository pr;

    @Autowired
    ProizvodRepository proizvodr;

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model m){
        ADJKorisnik korisnik = kr.findByUsername(getUsername());
        List<ADJIzlog> ostaliIzlozi = ir.findAll();
        Iterator itr = ostaliIzlozi.iterator();
        while(itr.hasNext()){
            ADJIzlog i = (ADJIzlog) itr.next();
            if(i.getAdjkorisnik().getIdKorisnik() == korisnik.getIdKorisnik())
                itr.remove();
        }
        m.addAttribute("korisnik",korisnik);
        m.addAttribute("ostaliIzlozi", ostaliIzlozi);
        return "profile";
    }

    @RequestMapping(value="/mojIzlog")
    public String mojIzlog(Model m, int idIzlog){
        ADJKorisnik korisnik = kr.findByUsername(getUsername());
        ADJIzlog izlog = ir.findById(idIzlog).get();
        List<ADJProizvodjac> proizvodjaci = pr.findAll();
        m.addAttribute("korisnik", korisnik);
        m.addAttribute("izlog", izlog);
        m.addAttribute("proizvodjaci",proizvodjaci);

        return "mojIzlog";
    }

    @RequestMapping(value="/mojIzlog/dodaj", method = RequestMethod.GET)
    public String mojIzlog(Model m, int idIzlog, String naziv, double cena, int idProizvodjac){
        ADJProizvod p = new ADJProizvod();
        p.setNaziv(naziv);
        p.setCena(cena);
        p.setAdjproizvodjac(pr.findById(idProizvodjac).get());
        p.setAdjizlog(ir.findById(idIzlog).get());
        proizvodr.save(p);
        List<ADJProizvodjac> proizvodjaci = pr.findAll();
        ADJKorisnik korisnik = kr.findByUsername(getUsername());
        m.addAttribute("korisnik", korisnik);
        m.addAttribute("proizvodjaci",proizvodjaci);
        ADJIzlog izlog = ir.findById(idIzlog).get();
        m.addAttribute("izlog", izlog);


        return "mojIzlog";
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {

            return  ((UserDetails)principal).getUsername();

        } else {

            return principal.toString();

        }
    }
}
