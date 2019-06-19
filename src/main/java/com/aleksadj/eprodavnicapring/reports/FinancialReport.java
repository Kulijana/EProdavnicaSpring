package com.aleksadj.eprodavnicapring.reports;


public class FinancialReport {
    String prodajaKupovina;
    String prodavacKupac;
    double vrednost;
    String izlog;
    String predmet;

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getProdajaKupovina() {
        return prodajaKupovina;
    }

    public void setProdajaKupovina(String prodajaKupovina) {
        this.prodajaKupovina = prodajaKupovina;
    }

    public String getProdavacKupac() {
        return prodavacKupac;
    }

    public void setProdavacKupac(String prodavacKupac) {
        this.prodavacKupac = prodavacKupac;
    }

    public double getVrednost() {
        return vrednost;
    }

    public void setVrednost(double vrednost) {
        this.vrednost = vrednost;
    }

    public String getIzlog() {
        return izlog;
    }

    public void setIzlog(String izlog) {
        this.izlog = izlog;
    }
}
