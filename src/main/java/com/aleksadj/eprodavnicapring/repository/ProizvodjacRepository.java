package com.aleksadj.eprodavnicapring.repository;

import model.ADJProizvodjac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProizvodjacRepository extends JpaRepository<ADJProizvodjac, Integer> {
}
