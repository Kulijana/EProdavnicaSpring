package com.aleksadj.eprodavnicapring.controller;


import com.aleksadj.eprodavnicapring.repository.ProizvodjacRepository;
import model.ADJProizvodjac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value="/Proizvodjac")
public class ProizvodjacController {

    @Autowired
    ProizvodjacRepository pr;

    @RequestMapping(value="/")
    public String sviProizvodjaci(Model m){
        List<ADJProizvodjac> proizvodjaci = pr.findAll();
        m.addAttribute("proizvodjaci", proizvodjaci);
        return "proizvodjaci";
    }
}
