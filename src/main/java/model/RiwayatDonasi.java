package com.mycompany.miniproject.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class RiwayatDonasi implements DapatDitampilkan {
    private final int idDonasi;
    private final int idDonor;
    private LocalDate tanggalDonasi;
    private int jumlahKantong;

    public RiwayatDonasi(final int idDonasi, final int idDonor, LocalDate tanggalDonasi, int jumlahKantong) {
        if (idDonasi <= 0) {
            throw new IllegalArgumentException("ID donasi harus lebih dari 0.");
        }
        if (idDonor <= 0) {
            throw new IllegalArgumentException("ID donor harus lebih dari 0.");
        }
        this.idDonasi = idDonasi;
        this.idDonor = idDonor;
        this.tanggalDonasi = validasiTanggal(tanggalDonasi);
        this.jumlahKantong = validasiJumlah(jumlahKantong);
    }

    public RiwayatDonasi(final int idDonasi, final int idDonor, String tanggalDonasi, int jumlahKantong) {
        this(idDonasi, idDonor, parseTanggal(tanggalDonasi), jumlahKantong);
    }

    public RiwayatDonasi(final int idDonasi, final int idDonor, String tanggalDonasi) {
        this(idDonasi, idDonor, tanggalDonasi, 1);
    }

    public int getIdDonasi() {
        return idDonasi;
    }

    public int getIdDonor() {
        return idDonor;
    }

    public LocalDate getTanggalDonasi() {
        return tanggalDonasi;
    }

    public int getJumlahKantong() {
        return jumlahKantong;
    }

    public void setTanggalDonasi(LocalDate tanggalDonasi) {
        this.tanggalDonasi = validasiTanggal(tanggalDonasi);
    }

    public void setTanggalDonasi(String tanggalDonasi) {
        this.tanggalDonasi = validasiTanggal(parseTanggal(tanggalDonasi));
    }

    public void setJumlahKantong(int jumlahKantong) {
        this.jumlahKantong = validasiJumlah(jumlahKantong);
    }

    public void perbaruiData(String tanggal, int jumlahKantong) {
        LocalDate tanggalBaru = validasiTanggal(parseTanggal(tanggal));
        int jumlahBaru = validasiJumlah(jumlahKantong);
        this.tanggalDonasi = tanggalBaru;
        this.jumlahKantong = jumlahBaru;
    }

    private static LocalDate parseTanggal(String teks) {
        if (teks == null || teks.trim().isEmpty()) {
            throw new IllegalArgumentException("Tanggal donasi tidak boleh kosong.");
        }
        try {
            return LocalDate.parse(teks.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Format tanggal tidak valid. Gunakan yyyy-MM-dd dengan tanggal yang nyata (contoh: 2026-01-15).");
        }
    }

    private static LocalDate validasiTanggal(LocalDate tanggal) {
        if (tanggal == null) {
            throw new IllegalArgumentException("Tanggal donasi tidak boleh kosong.");
        }
        if (tanggal.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tanggal donasi tidak boleh di masa depan.");
        }
        return tanggal;
    }

    private static int validasiJumlah(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah kantong darah harus lebih dari 0.");
        }
        return jumlah;
    }

    @Override
    public String formatData() {
        return String.join(System.lineSeparator(),
                "ID Donasi: " + idDonasi,
                "ID Donor: " + idDonor,
                "Tanggal Donasi: " + tanggalDonasi,
                "Jumlah Kantong: " + jumlahKantong);
    }

    @Override
    public String formatRingkas() {
        return "[Donasi " + idDonasi + "] Donor " + idDonor + " | " + tanggalDonasi + " | " + jumlahKantong + " kantong";
    }

    @Override
    public String toString() {
        return formatRingkas();
    }
}