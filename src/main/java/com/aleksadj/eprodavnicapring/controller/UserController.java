package com.aleksadj.eprodavnicapring.controller;

import com.aleksadj.eprodavnicapring.reports.FinancialReport;
import com.aleksadj.eprodavnicapring.repository.*;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

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

    @Autowired
    TransakcijaRepository tr;

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public String profile(Model m){
        m = generateProfile(m);
        return "profile";
    }

    @RequestMapping(value="/pregledIzloga")
    public String pregledIzloga(Model m, int idIzlog){
        m.addAttribute("korisnik", kr.findByUsername(getUsername()));
        m.addAttribute("izlog", ir.findById(idIzlog).get());
        return "izlog";
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


    @RequestMapping(value="/dodajIzlog")
    public String dodajIzlog(Model m){
        m.addAttribute("korisnik",kr.findByUsername(getUsername()));
        return "dodajIzlog";
    }

    @RequestMapping(value="/sacuvajIzlog")
    public String sacuvajIzlog(Model m, String naziv){
        ADJKorisnik korisnik = kr.findByUsername(getUsername());
        ADJIzlog i = new ADJIzlog();
        i.setNaziv(naziv);
        i.setAdjkorisnik(korisnik);
        ir.save(i);
        m = generateProfile(m);
        return "profile";
    }


    @RequestMapping(value = "/kupovina")
    public void kupovina(Model m, int idProizvod, HttpServletResponse response) throws Exception{
        ADJKorisnik kupac = kr.findByUsername(getUsername());
        ADJProizvod proizvod = proizvodr.findById(idProizvod).get();
        if(kupac.getStanjeRacuna() < proizvod.getCena()){
            return;
        }


        ADJKorisnik prodavac = proizvod.getAdjizlog().getAdjkorisnik();
        kupac.setStanjeRacuna(kupac.getStanjeRacuna() - proizvod.getCena());
        prodavac.setStanjeRacuna(prodavac.getStanjeRacuna() + proizvod.getCena());


        ADJTransakcija transakcija = new ADJTransakcija();
        transakcija.setAdjizlog(proizvod.getAdjizlog());
        transakcija.setAdjkorisnik(kupac);
        transakcija.setAdjproizvod(proizvod);
        transakcija.setKolicina(1);

        proizvod.setAdjizlog(null);

        kr.save(prodavac);
        kr.save(kupac);
        proizvodr.save(proizvod);
        tr.save(transakcija);

        generateReportForSale(response, transakcija);
    }


    @RequestMapping(value = "/completeFinancialReport")
    public void completeFinancialReport(HttpServletResponse response) throws Exception{
        ADJKorisnik korisnik = kr.findByUsername(getUsername());
        List<FinancialReport> list = new ArrayList<>();
        double suma = 0;
        for(ADJTransakcija t: korisnik.getAdjtransakcijas()){
            FinancialReport r = new FinancialReport();
            r.setProdajaKupovina("Kupovina");
            r.setProdavacKupac(t.getAdjizlog().getAdjkorisnik().getImePrezime());
            r.setIzlog(t.getAdjizlog().getNaziv());
            r.setPredmet(t.getAdjproizvod().getNaziv());
            r.setVrednost(-1*t.getAdjproizvod().getCena());
            suma += r.getVrednost();
            list.add(r);
        }
        for(ADJIzlog i: korisnik.getAdjizlogs()){
            for(ADJTransakcija t: i.getAdjtransakcijas()){
                FinancialReport r = new FinancialReport();
                r.setProdajaKupovina("Prodaja");
                r.setProdavacKupac(t.getAdjizlog().getAdjkorisnik().getImePrezime());
                r.setIzlog(t.getAdjizlog().getNaziv());
                r.setPredmet(t.getAdjproizvod().getNaziv());
                r.setVrednost(t.getAdjproizvod().getCena());
                suma += r.getVrednost();
                list.add(r);
            }
        }

        InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/CompleteFinancialReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("imeKorisnika", korisnik.getImePrezime());
        params.put("trenutnoStanje", korisnik.getStanjeRacuna());
        params.put("profitOdTransakcija", suma);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        inputStream.close();

        response.setContentType("application/x-download");
        response.addHeader("Content-disposition", "attachment; filename=completeFinancialReport.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }

    private void generateReportForSale(HttpServletResponse response, ADJTransakcija transakcija) throws Exception{
        InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/transactionReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(transakcija.getAdjizlog().getAdjproizvods());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("imeProdavaca", transakcija.getAdjizlog().getAdjkorisnik().getImePrezime());
        params.put("imeIzloga", transakcija.getAdjizlog().getNaziv());
        params.put("imeKupca", transakcija.getAdjkorisnik().getImePrezime());
        params.put("imeProizvoda", transakcija.getAdjproizvod().getNaziv());
        params.put("Cena", transakcija.getAdjproizvod().getCena());
        params.put("brojTransakcije", transakcija.getIdTransakcija());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
        inputStream.close();

        response.setContentType("application/x-download");
        response.addHeader("Content-disposition", "attachment; filename=transakcija.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }



    private Model generateProfile(Model m){
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

        return m;
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
