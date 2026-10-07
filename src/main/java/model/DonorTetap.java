package com.mycompany.miniproject.model;

public final class DonorTetap extends Donor {

    private final String nomorKartu;
    private int jumlahDonasi;

    public DonorTetap(final int id, String nama, String golonganDarah, int umur,
            String noTelepon, int jumlahDonasiSebelumnya) {
        super(id, nama, golonganDarah, umur, noTelepon);
        this.nomorKartu = String.format("KD-%04d", id);
        setJumlahDonasi(jumlahDonasiSebelumnya);
    }

    public String getNomorKartu() {
        return nomorKartu;
    }

    public int getJumlahDonasi() {
        return jumlahDonasi;
    }

    public void setJumlahDonasi(int jumlahDonasi) {
        if (jumlahDonasi < 1) {
            throw new IllegalArgumentException("Jumlah donasi sebelumnya donor tetap minimal 1.");
        }
        this.jumlahDonasi = jumlahDonasi;
    }

    @Override
    public void catatDonasi() {
        jumlahDonasi++;
    }

    @Override
    public String getKategori() {
        return "Donor Tetap";
    }

    @Override
    public String getInfoKhusus() {
        return "Nomor Kartu: " + nomorKartu + System.lineSeparator()
                + "Jumlah Donasi: " + jumlahDonasi + "x";
    }
}