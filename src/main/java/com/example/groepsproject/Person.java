package com.example.groepsproject;


import java.time.LocalDate;
import java.time.Period;
public abstract class Person {
    private String firstName;
    private String lastName;
    private String mail;
    private String username;
    private String password;
    private LocalDate dateOfBirth;

    private int age;

    private String IBANnr;



    public Person(String firstName, String lastName, String mail, String username, String password, LocalDate dateOfBirth,String IBANnr) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        //leeftijd van de persoon berekenen
        LocalDate now = LocalDate.now();
        Period period = Period.between(this.dateOfBirth,now);
        this.age = period.getYears();
        this.IBANnr = IBANnr;
    }

    public Person() {

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIBANnr(String IBANnr) {
        this.IBANnr = IBANnr;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public String getIBANnr() {
        return IBANnr;
    }
}
