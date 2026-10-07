package com.mycompany.miniproject.view;

import com.mycompany.miniproject.model.DapatDitampilkan;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private static final String GARIS = "-----------------------------";

    private final Scanner scanner = new Scanner(System.in);


    public void tampilkanMenuUtama() {
        System.out.println();
        System.out.println("=========================================");
        System.out.println("     SISTEM MANAJEMEN DONASI DARAH");
        System.out.println("=========================================");
        System.out.println("1. Kelola Data Donor");
        System.out.println("2. Kelola Data Donasi");
        System.out.println("3. Lihat Ringkasan");
        System.out.println("0. Keluar");
    }

    public void tampilkanMenuDonor() {
        System.out.println();
        System.out.println("--- KELOLA DATA DONOR ---");
        System.out.println("1. Tambah Donor");
        System.out.println("2. Lihat Semua Donor");
        System.out.println("3. Update Donor");
        System.out.println("4. Hapus Donor");
        System.out.println("5. Cari Donor (nama)");
        System.out.println("0. Kembali ke Menu Utama");
    }

    public void tampilkanMenuDonasi() {
        System.out.println();
        System.out.println("--- KELOLA DATA DONASI ---");
        System.out.println("1. Catat Donasi Baru");
        System.out.println("2. Lihat Semua Donasi");
        System.out.println("3. Update Donasi");
        System.out.println("4. Hapus Donasi");
        System.out.println("0. Kembali ke Menu Utama");
    }

    public void tampilkanPesan(String pesan) {
        System.out.println(pesan);
    }

    public void tampilkanError(String pesan) {
        System.out.println("[GAGAL] " + pesan);
    }

    public void tampilkanData(DapatDitampilkan data) {
        System.out.println(GARIS);
        System.out.println(data.formatData());
        System.out.println(GARIS);
    }

    public void tampilkanData(List<? extends DapatDitampilkan> daftar, String pesanKosong) {
        if (daftar.isEmpty()) {
            System.out.println(pesanKosong);
            return;
        }
        for (DapatDitampilkan data : daftar) {
            System.out.println(GARIS);
            System.out.println(data.formatData());
        }
        System.out.println(GARIS);
    }

    public String bacaTeks(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int bacaAngka(String prompt) {
        while (true) {
            String masukan = bacaTeks(prompt).trim();
            try {
                return Integer.parseInt(masukan);
            } catch (NumberFormatException e) {
                tampilkanError("Input harus berupa angka bulat. Silakan coba lagi.");
            }
        }
    }

    public int bacaAngka(String prompt, int min, int max) {
        while (true) {
            int nilai = bacaAngka(prompt);
            if (nilai >= min && nilai <= max) {
                return nilai;
            }
            tampilkanError("Angka harus antara " + min + " dan " + max + ".");
        }
    }

    public boolean konfirmasi(String prompt) {
        while (true) {
            String jawab = bacaTeks(prompt + " (y/n): ").trim().toLowerCase();
            if (jawab.equals("y")) {
                return true;
            }
            if (jawab.equals("n")) {
                return false;
            }
            tampilkanError("Jawab dengan y atau n.");
        }
    }
}