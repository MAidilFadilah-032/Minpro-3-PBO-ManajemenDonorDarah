package com.mycompany.miniproject.model;

import java.util.List;

public abstract class Donor implements DapatDitampilkan {

    public static final int UMUR_MINIMAL = 17;
    public static final int UMUR_MAKSIMAL = 65;
    public static final List<String> GOLONGAN_VALID = List.of("A", "B", "AB", "O");

    private final int id;
    private String nama;
    private String golonganDarah;
    private int umur;
    private String noTelepon;

    protected Donor(final int id, String nama, String golonganDarah, int umur, String noTelepon) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID donor harus lebih dari 0.");
        }
        this.id = id;
        setNama(nama);
        setGolonganDarah(golonganDarah);
        setUmur(umur);
        setNoTelepon(noTelepon);
    }

    public abstract String getKategori();

    public abstract String getInfoKhusus();

    public final int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getGolonganDarah() {
        return golonganDarah;
    }

    public int getUmur() {
        return umur;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public final void setNama(String nama) {
        this.nama = validasiNama(nama);
    }

    public final void setGolonganDarah(String golonganDarah) {
        this.golonganDarah = validasiGolongan(golonganDarah);
    }

    public final void setUmur(int umur) {
        this.umur = validasiUmur(umur);
    }

    public final void setNoTelepon(String noTelepon) {
        this.noTelepon = validasiTelepon(noTelepon);
    }

    public final void perbaruiData(String nama, String golonganDarah, int umur, String noTelepon) {
        String namaBaru = validasiNama(nama);
        String golonganBaru = validasiGolongan(golonganDarah);
        int umurBaru = validasiUmur(umur);
        String teleponBaru = validasiTelepon(noTelepon);

        this.nama = namaBaru;
        this.golonganDarah = golonganBaru;
        this.umur = umurBaru;
        this.noTelepon = teleponBaru;
    }

    public void catatDonasi() {
        // sengaja kosong
    }

    private static String validasiNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong.");
        }
        return nama.trim();
    }

    private static String validasiGolongan(String golongan) {
        String bersih = (golongan == null) ? "" : golongan.trim().toUpperCase();
        if (!GOLONGAN_VALID.contains(bersih)) {
            throw new IllegalArgumentException("Golongan darah harus A, B, AB, atau O.");
        }
        return bersih;
    }

    private static int validasiUmur(int umur) {
        if (umur < UMUR_MINIMAL || umur > UMUR_MAKSIMAL) {
            throw new IllegalArgumentException(
                    "Umur pendonor harus antara " + UMUR_MINIMAL + " sampai " + UMUR_MAKSIMAL + " tahun.");
        }
        return umur;
    }

    private static String validasiTelepon(String noTelepon) {
        String bersih = (noTelepon == null) ? "" : noTelepon.trim();
        if (!bersih.matches("\\+?\\d{8,15}")) {
            throw new IllegalArgumentException(
                    "No. telepon harus berupa angka (8-15 digit), boleh diawali tanda +.");
        }
        return bersih;
    }

    @Override
    public String formatData() {
        String pemisah = System.lineSeparator();
        return String.join(pemisah,
                "ID: " + id,
                "Nama: " + nama,
                "Golongan Darah: " + golonganDarah,
                "Umur: " + umur,
                "No. Telepon: " + noTelepon,
                "Kategori: " + getKategori(),
                getInfoKhusus());
    }

    public String formatData(boolean ringkas) {
        return ringkas ? formatRingkas() : formatData();
    }

    @Override
    public String formatRingkas() {
        return "[" + id + "] " + nama + " | Gol. " + golonganDarah + " | " + getKategori();
    }

    @Override
    public String toString() {
        return formatRingkas();
    }
}