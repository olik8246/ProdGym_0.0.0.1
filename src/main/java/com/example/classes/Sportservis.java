package com.example.classes;

public class Sportservis {
    private String name;
    private int contractPrice;

    public Sportservis() {
    }

    public Sportservis(String name, int contractPrice) {
        this.name = name;
        this.contractPrice = contractPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(int contractPrice) {
        this.contractPrice = contractPrice;
    }

    @Override
    public String toString() {
        return name + " (Контракт: " + contractPrice + "грн)";
    }
}