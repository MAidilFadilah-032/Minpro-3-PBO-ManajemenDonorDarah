package com.mycompany.miniproject.model;

public interface DapatDitampilkan {

    String formatData();

    default String formatRingkas() {
        return formatData();
    }
}