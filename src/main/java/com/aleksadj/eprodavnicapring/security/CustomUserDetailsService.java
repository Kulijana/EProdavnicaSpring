package com.aleksadj.eprodavnicapring.security;


import com.aleksadj.eprodavnicapring.repository.KorisnikRepository;
import model.ADJKorisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{


    @Autowired
    private KorisnikRepository korisnikRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ADJKorisnik user = korisnikRepository.findByUsername(username);
        UserDetailsImpl userDetails =new UserDetailsImpl();
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setRoles(user.getAdjulogas());
        return userDetails;

    }







}