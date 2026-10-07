package com.mycompany.miniproject.model;

public class DataTidakDitemukanException extends Exception {

    private static final long serialVersionUID = 1L;

    public DataTidakDitemukanException(String pesan) {
        super(pesan);
    }
}