package com.aleksadj.eprodavnicapring.controller;


import com.aleksadj.eprodavnicapring.repository.KorisnikRepository;
import com.aleksadj.eprodavnicapring.repository.ProizvodRepository;
import com.aleksadj.eprodavnicapring.repository.TransakcijaRepository;
import model.ADJIzlog;
import model.ADJKorisnik;
import model.ADJProizvod;
import model.ADJTransakcija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    TransakcijaRepository tr;

    @Autowired
    KorisnikRepository kr;

    @Autowired
    ProizvodRepository proizvodr;


    @RequestMapping(value = "/allTransactions")
    public String showTransactions(Model m){
        m.addAttribute("korisnik",kr.findByUsername(getUsername()));
        m.addAttribute("transakcije", tr.findAll());
        return "transakcije";
    }

    @RequestMapping(value = "/ponistiTransakciju")
    public String denyTransaction(Model m, int idTransakcija){
        ADJTransakcija transakcija = tr.findById(idTransakcija).get();
        ADJKorisnik kupac = transakcija.getAdjkorisnik();
        ADJIzlog izlog = transakcija.getAdjizlog();
        ADJProizvod proizvod = transakcija.getAdjproizvod();
        ADJKorisnik prodavac = izlog.getAdjkorisnik();


        kupac.setStanjeRacuna(kupac.getStanjeRacuna() + proizvod.getCena());
        prodavac.setStanjeRacuna(prodavac.getStanjeRacuna() - proizvod.getCena());

        proizvod.setAdjizlog(izlog);

        kr.save(kupac);
        kr.save(prodavac);

        proizvodr.save(proizvod);

        tr.delete(transakcija);

        m.addAttribute("transakcije", tr.findAll());
        m.addAttribute("korisnik", kr.findByUsername(getUsername()));
        return "transakcije";
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
