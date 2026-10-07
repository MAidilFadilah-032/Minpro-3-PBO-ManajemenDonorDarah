package com.mycompany.miniproject.controller;

import com.mycompany.miniproject.model.DataTidakDitemukanException;
import com.mycompany.miniproject.model.DonasiDarahManager;
import com.mycompany.miniproject.model.Donor;
import com.mycompany.miniproject.view.ConsoleView;
import java.util.List;

public class DonasiDarahController {

    private final DonasiDarahManager manager;
    private final ConsoleView view;

    public DonasiDarahController(final DonasiDarahManager manager, final ConsoleView view) {
        this.manager = manager;
        this.view = view;
    }

    public void jalankan() {
        boolean berjalan = true;

        while (berjalan) {
            view.tampilkanMenuUtama();
            int pilihan = view.bacaAngka("Pilih menu: ");

            switch (pilihan) {
                case 1 -> menuDonor();
                case 2 -> menuDonasi();
                case 3 -> tampilkanRingkasan();
                case 0 -> {
                    berjalan = false;
                    view.tampilkanPesan("Terima kasih telah menggunakan Sistem Donasi Darah.");
                }
                default -> view.tampilkanError("Pilihan tidak tersedia.");
            }
        }
    }

    private void menuDonor() {
        boolean kembali = false;

        while (!kembali) {
            view.tampilkanMenuDonor();
            int pilihan = view.bacaAngka("Pilih menu: ");

            try {
                switch (pilihan) {
                    case 1 -> tambahDonor();
                    case 2 -> view.tampilkanData(manager.getDaftarDonor(), "Belum ada data donor.");
                    case 3 -> updateDonor();
                    case 4 -> hapusDonor();
                    case 5 -> cariDonor();
                    case 0 -> kembali = true;
                    default -> view.tampilkanError("Pilihan tidak tersedia.");
                }
            } catch (IllegalArgumentException | DataTidakDitemukanException e) {
                view.tampilkanError(e.getMessage());
            }
        }
    }

    private void tambahDonor() {
        view.tampilkanPesan("Pilih kategori donor:");
        view.tampilkanPesan("1. Donor Baru");
        view.tampilkanPesan("2. Donor Tetap");
        int kategori = view.bacaAngka("Pilih kategori (1-2): ", 1, 2);

        String nama = view.bacaTeks("Nama Donor: ");
        String golongan = view.bacaTeks("Golongan Darah (A/B/AB/O): ");
        int umur = view.bacaAngka("Umur: ");
        String telepon = view.bacaTeks("No. Telepon: ");

        Donor donor;
        if (kategori == 2) {
            int jumlahSebelumnya = view.bacaAngka("Jumlah donasi sebelumnya: ");
            donor = manager.tambahDonor(nama, golongan, umur, telepon, jumlahSebelumnya);
        } else {
            String sumber = view.bacaTeks("Tahu program donor darah dari mana? (boleh kosong): ");
            donor = manager.tambahDonor(nama, golongan, umur, telepon, sumber);
        }

        view.tampilkanPesan("Donor berhasil ditambahkan dengan ID " + donor.getId() + ".");
    }

    private void updateDonor() throws DataTidakDitemukanException {
        view.tampilkanData(manager.getDaftarDonor(), "Belum ada data donor.");
        int id = view.bacaAngka("Masukkan ID Donor yang ingin diupdate: ");
        manager.cariDonor(id); // cek dulu supaya tidak perlu mengetik data jika ID salah

        String nama = view.bacaTeks("Nama baru: ");
        String golongan = view.bacaTeks("Golongan darah baru: ");
        int umur = view.bacaAngka("Umur baru: ");
        String telepon = view.bacaTeks("No. telepon baru: ");

        Donor donor = manager.updateDonor(id, nama, golongan, umur, telepon);
        view.tampilkanPesan("Update berhasil. Data terbaru:");
        view.tampilkanData(donor);
    }

    private void hapusDonor() throws DataTidakDitemukanException {
        view.tampilkanData(manager.getDaftarDonor(), "Belum ada data donor.");
        int id = view.bacaAngka("Masukkan ID Donor yang ingin dihapus: ");
        Donor donor = manager.cariDonor(id);

        if (view.konfirmasi("Hapus donor \"" + donor.getNama() + "\" beserta riwayat donasinya?")) {
            int donasiTerhapus = manager.hapusDonor(id);
            view.tampilkanPesan("Donor ID " + id + " berhasil dihapus (" + donasiTerhapus
                    + " riwayat donasi ikut terhapus).");
        } else {
            view.tampilkanPesan("Penghapusan dibatalkan.");
        }
    }

    private void cariDonor() {
        String kataKunci = view.bacaTeks("Masukkan nama / potongan nama: ");
        List<Donor> hasil = manager.cariDonor(kataKunci);
        view.tampilkanData(hasil, "Tidak ada donor yang cocok.");
    }

    private void menuDonasi() {
        boolean kembali = false;

        while (!kembali) {
            view.tampilkanMenuDonasi();
            int pilihan = view.bacaAngka("Pilih menu: ");

            try {
                switch (pilihan) {
                    case 1 -> catatDonasi();
                    case 2 -> view.tampilkanData(manager.getDaftarDonasi(), "Belum ada data donasi.");
                    case 3 -> updateDonasi();
                    case 4 -> hapusDonasi();
                    case 0 -> kembali = true;
                    default -> view.tampilkanError("Pilihan tidak tersedia.");
                }
            } catch (IllegalArgumentException | DataTidakDitemukanException e) {
                view.tampilkanError(e.getMessage());
            }
        }
    }

    private void catatDonasi() throws DataTidakDitemukanException {
        view.tampilkanData(manager.getDaftarDonor(), "Belum ada data donor.");
        int idDonor = view.bacaAngka("ID Donor: ");
        manager.cariDonor(idDonor);

        String tanggal = view.bacaTeks("Tanggal (yyyy-MM-dd): ");
        int jumlahKantong = view.bacaAngka("Jumlah Kantong: ");

        int idDonasi = manager.tambahDonasi(idDonor, tanggal, jumlahKantong).getIdDonasi();
        view.tampilkanPesan("Donasi berhasil dicatat dengan ID " + idDonasi + ".");
    }

    private void updateDonasi() throws DataTidakDitemukanException {
        view.tampilkanData(manager.getDaftarDonasi(), "Belum ada data donasi.");
        int id = view.bacaAngka("Masukkan ID Donasi yang ingin diupdate: ");
        manager.cariDonasi(id);

        String tanggal = view.bacaTeks("Tanggal baru (yyyy-MM-dd): ");
        int jumlahKantong = view.bacaAngka("Jumlah kantong baru: ");

        view.tampilkanPesan("Update berhasil. Data terbaru:");
        view.tampilkanData(manager.updateDonasi(id, tanggal, jumlahKantong));
    }

    private void hapusDonasi() throws DataTidakDitemukanException {
        view.tampilkanData(manager.getDaftarDonasi(), "Belum ada data donasi.");
        int id = view.bacaAngka("Masukkan ID Donasi yang ingin dihapus: ");
        manager.cariDonasi(id);

        if (view.konfirmasi("Yakin ingin menghapus donasi ID " + id + "?")) {
            manager.hapusDonasi(id);
            view.tampilkanPesan("Donasi ID " + id + " berhasil dihapus.");
        } else {
            view.tampilkanPesan("Penghapusan dibatalkan.");
        }
    }

    private void tampilkanRingkasan() {
        view.tampilkanPesan("");
        view.tampilkanPesan("--- RINGKASAN ---");
        view.tampilkanPesan("Total Donor terdaftar: " + manager.getJumlahDonor());
        view.tampilkanPesan("Total Donasi tercatat: " + manager.getJumlahDonasi());
        view.tampilkanPesan("Total kantong darah: " + manager.getTotalKantong());
    }
}