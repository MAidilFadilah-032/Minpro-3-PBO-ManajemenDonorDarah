package com.mycompany.miniproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DonasiDarahManager {

    private final List<Donor> daftarDonor = new ArrayList<>();
    private final List<RiwayatDonasi> daftarDonasi = new ArrayList<>();

    private int idDonorTerakhir = 0;
    private int idDonasiTerakhir = 0;

    public DonasiDarahManager() {
        isiDataAwal();
    }

    private void isiDataAwal() {
        tambahDonor("Siti Aminah", "A", 22, "081234567890", "Media sosial");
        tambahDonor("Budi Santoso", "O", 30, "081298765432", 2);
        try {
            tambahDonasi(2, "2026-01-15", 1);
        } catch (DataTidakDitemukanException e) {
            throw new IllegalStateException("Data awal tidak valid.", e);
        }
    }

    public Donor tambahDonor(String nama, String golonganDarah, int umur, String noTelepon,
            String sumberInformasi) {
        int idBaru = idDonorTerakhir + 1;
        Donor donor = new DonorBaru(idBaru, nama, golonganDarah, umur, noTelepon, sumberInformasi);
        daftarDonor.add(donor);
        idDonorTerakhir = idBaru; 
        return donor;
    }

    public Donor tambahDonor(String nama, String golonganDarah, int umur, String noTelepon,
            int jumlahDonasiSebelumnya) {
        int idBaru = idDonorTerakhir + 1;
        Donor donor = new DonorTetap(idBaru, nama, golonganDarah, umur, noTelepon, jumlahDonasiSebelumnya);
        daftarDonor.add(donor);
        idDonorTerakhir = idBaru;
        return donor;
    }

    public List<Donor> getDaftarDonor() {
        return Collections.unmodifiableList(daftarDonor);
    }

    public Donor cariDonor(int id) throws DataTidakDitemukanException {
        for (Donor donor : daftarDonor) {
            if (donor.getId() == id) {
                return donor;
            }
        }
        throw new DataTidakDitemukanException("Donor dengan ID " + id + " tidak ditemukan.");
    }

    public List<Donor> cariDonor(String kataKunci) {
        List<Donor> hasil = new ArrayList<>();
        String kunci = (kataKunci == null) ? "" : kataKunci.trim().toLowerCase();
        for (Donor donor : daftarDonor) {
            if (donor.getNama().toLowerCase().contains(kunci)) {
                hasil.add(donor);
            }
        }
        return hasil;
    }

    public Donor updateDonor(int id, String nama, String golonganDarah, int umur, String noTelepon)
            throws DataTidakDitemukanException {
        Donor donor = cariDonor(id);
        donor.perbaruiData(nama, golonganDarah, umur, noTelepon);
        return donor;
    }

    public int hapusDonor(int id) throws DataTidakDitemukanException {
        Donor donor = cariDonor(id);
        int sebelum = daftarDonasi.size();
        daftarDonasi.removeIf(donasi -> donasi.getIdDonor() == id);
        daftarDonor.remove(donor);
        return sebelum - daftarDonasi.size();
    }

    public RiwayatDonasi tambahDonasi(int idDonor, String tanggal, int jumlahKantong)
            throws DataTidakDitemukanException {
        Donor donor = cariDonor(idDonor);

        int idBaru = idDonasiTerakhir + 1;
        RiwayatDonasi donasi = new RiwayatDonasi(idBaru, idDonor, tanggal, jumlahKantong);
        daftarDonasi.add(donasi);
        idDonasiTerakhir = idBaru;

        donor.catatDonasi(); // POLYMORPHISM: DonorTetap menambah hitungan, DonorBaru tidak
        return donasi;
    }

    public RiwayatDonasi tambahDonasi(int idDonor, String tanggal) throws DataTidakDitemukanException {
        return tambahDonasi(idDonor, tanggal, 1);
    }

    public List<RiwayatDonasi> getDaftarDonasi() {
        return Collections.unmodifiableList(daftarDonasi);
    }

    public RiwayatDonasi cariDonasi(int id) throws DataTidakDitemukanException {
        for (RiwayatDonasi donasi : daftarDonasi) {
            if (donasi.getIdDonasi() == id) {
                return donasi;
            }
        }
        throw new DataTidakDitemukanException("Donasi dengan ID " + id + " tidak ditemukan.");
    }

    public RiwayatDonasi updateDonasi(int id, String tanggal, int jumlahKantong)
            throws DataTidakDitemukanException {
        RiwayatDonasi donasi = cariDonasi(id);
        donasi.perbaruiData(tanggal, jumlahKantong);
        return donasi;
    }

    public void hapusDonasi(int id) throws DataTidakDitemukanException {
        daftarDonasi.remove(cariDonasi(id));
    }

    public int getJumlahDonor() {
        return daftarDonor.size();
    }

    public int getJumlahDonasi() {
        return daftarDonasi.size();
    }

    public int getTotalKantong() {
        int total = 0;
        for (RiwayatDonasi donasi : daftarDonasi) {
            total += donasi.getJumlahKantong();
        }
        return total;
    }
}