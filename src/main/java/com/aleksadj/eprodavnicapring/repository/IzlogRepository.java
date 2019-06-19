package com.aleksadj.eprodavnicapring.repository;


import model.ADJIzlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IzlogRepository extends JpaRepository<ADJIzlog, Integer> {
}
