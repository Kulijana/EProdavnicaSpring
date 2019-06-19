package com.aleksadj.eprodavnicapring.repository;


import model.ADJKorisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KorisnikRepository extends JpaRepository<ADJKorisnik, Integer> {
    public ADJKorisnik findByUsername(String username);
}
