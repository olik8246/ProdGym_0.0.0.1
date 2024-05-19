package com.example.classes;

public class Specialty {
    private String specialty;
    private String rank;
    private int experience; // Досвід у роках
    private Coach assignedCoach;
    private String status;

    public Specialty() {
        this.status = "Вільний";
    }

    public Specialty(String specialty, String rank, int experience) {
        this.specialty = specialty;
        this.rank = rank;
        this.experience = experience;
        this.status = "Вільний";
    }

    public String getName(){
        return specialty + " " + rank;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Coach getAssignedCoachs() {
        return assignedCoach;
    }

    public void setAssignedCoach(Coach assignedCoach) {
        this.assignedCoach = assignedCoach;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String CoachInfo = assignedCoach != null ? " - " + assignedCoach.toString() : "";
        return specialty + " " + rank + " (Досвід: 4" + " років)" + CoachInfo;
    }
}
