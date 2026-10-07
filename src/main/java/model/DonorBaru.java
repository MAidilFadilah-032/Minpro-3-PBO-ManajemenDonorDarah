package com.mycompany.miniproject.model;

public final class DonorBaru extends Donor {

    private static final String SUMBER_DEFAULT = "Tidak disebutkan";

    private String sumberInformasi;

    public DonorBaru(final int id, String nama, String golonganDarah, int umur, String noTelepon, String sumberInformasi) {
        super(id, nama, golonganDarah, umur, noTelepon);
        setSumberInformasi(sumberInformasi);
    }

    public DonorBaru(final int id, String nama, String golonganDarah, int umur, String noTelepon) {
        this(id, nama, golonganDarah, umur, noTelepon, SUMBER_DEFAULT);
    }

    public String getSumberInformasi() {
        return sumberInformasi;
    }

    public void setSumberInformasi(String sumberInformasi) {
        if (sumberInformasi == null || sumberInformasi.trim().isEmpty()) {
            this.sumberInformasi = SUMBER_DEFAULT;
        } else {
            this.sumberInformasi = sumberInformasi.trim();
        }
    }

    @Override
    public String getKategori() {
        return "Donor Baru";
    }

    @Override
    public String getInfoKhusus() {
        return "Sumber Informasi: " + sumberInformasi;
    }
}