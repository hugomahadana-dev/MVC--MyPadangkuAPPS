package com.adpl.model;

public enum StatusStock {
    AMAN("Aman"),
    KRITIS("Kritis"),
    HABIS("Habis");

    private final String label;

    StatusStock(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
