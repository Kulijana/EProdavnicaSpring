package com.aleksadj.eprodavnicapring.repository;

import model.ADJProizvod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProizvodRepository extends JpaRepository<ADJProizvod, Integer> {
}
