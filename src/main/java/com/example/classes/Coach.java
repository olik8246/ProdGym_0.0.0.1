package com.example.classes;

public class Coach {
    private String firstname;
    private String lastname;
    private int yearexp;
    private int price;
    private Specialty assignedSpecialty;

    public Coach() {
    }

    public Coach(String firstname, String lastname, int yearexp, int price) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.yearexp = yearexp;
        this.price = price;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getYearexp() {
        return yearexp;
    }

    public void setYearexp(int yearexp) {
        this.yearexp = yearexp;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Specialty getAssignedCoach() {
        return assignedSpecialty;
    }

    public void setAssignedSpecialtys(Specialty assignedSpecialty) {
        this.assignedSpecialty = assignedSpecialty;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname + " (" + yearexp + ") - " + price + "грн";
    }
}