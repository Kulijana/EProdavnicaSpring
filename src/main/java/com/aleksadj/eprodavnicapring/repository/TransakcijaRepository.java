package com.aleksadj.eprodavnicapring.repository;

import model.ADJTransakcija;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransakcijaRepository extends JpaRepository<ADJTransakcija, Integer> {
}
