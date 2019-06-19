package com.aleksadj.eprodavnicapring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/auth")
public class LoginController {

    @RequestMapping(value="/loginPage", method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }
}
