package service;

import dao.DonorDAO;
import dao.DonorDAOImpl;
import dao.RiwayatDonasiDAO;
import dao.RiwayatDonasiDAOImpl;
import model.DataTidakDitemukanException;
import model.DatabaseException;
import model.Donor;
import model.DonorBaru;
import model.DonorTetap;
import model.RiwayatDonasi;
import java.sql.SQLException;
import java.util.List;

public class DonasiDarahManager {

    private final DonorDAO donorDAO;
    private final RiwayatDonasiDAO donasiDAO;

    public DonasiDarahManager() {
        this(new DonorDAOImpl(), new RiwayatDonasiDAOImpl());
    }

    public DonasiDarahManager(final DonorDAO donorDAO, final RiwayatDonasiDAO donasiDAO) {
        this.donorDAO = donorDAO;
        this.donasiDAO = donasiDAO;
    }

    public Donor tambahDonor(String nama, String golonganDarah, int umur, String noTelepon,
            String sumberInformasi) {
        Donor draft = new DonorBaru(Donor.ID_BELUM_ADA, nama, golonganDarah, umur, noTelepon, sumberInformasi);
        return simpanDonor(draft);
    }

    public Donor tambahDonor(String nama, String golonganDarah, int umur, String noTelepon,
            int jumlahDonasiSebelumnya) {
        Donor draft = new DonorTetap(Donor.ID_BELUM_ADA, nama, golonganDarah, umur, noTelepon,
                jumlahDonasiSebelumnya);
        return simpanDonor(draft);
    }

    private Donor simpanDonor(Donor draft) {
        try {
            int idBaru = donorDAO.insert(draft);
            Donor tersimpan = donorDAO.findById(idBaru);
            if (tersimpan == null) {
                throw new DatabaseException("Donor baru tidak ditemukan setelah disimpan.", null);
            }
            return tersimpan;
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menyimpan donor: " + e.getMessage(), e);
        }
    }

    public List<Donor> getDaftarDonor() {
        try {
            return donorDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal membaca data donor: " + e.getMessage(), e);
        }
    }

    public Donor cariDonor(int id) throws DataTidakDitemukanException {
        try {
            Donor donor = donorDAO.findById(id);
            if (donor == null) {
                throw new DataTidakDitemukanException("Donor dengan ID " + id + " tidak ditemukan.");
            }
            return donor;
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mencari donor: " + e.getMessage(), e);
        }
    }

    public List<Donor> cariDonor(String kataKunci) {
        try {
            return donorDAO.findByNama(kataKunci);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mencari donor: " + e.getMessage(), e);
        }
    }

    public Donor updateDonor(int id, String nama, String golonganDarah, int umur, String noTelepon)
            throws DataTidakDitemukanException {
        Donor donor = cariDonor(id);
        donor.perbaruiData(nama, golonganDarah, umur, noTelepon); 
        try {
            donorDAO.update(donor);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal memperbarui donor: " + e.getMessage(), e);
        }
        return donor;
    }

    public int hapusDonor(int id) throws DataTidakDitemukanException {
        cariDonor(id); 
        try {
            int donasiTerhapus = donasiDAO.countByDonor(id);
            donorDAO.delete(id);
            return donasiTerhapus;
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghapus donor: " + e.getMessage(), e);
        }
    }

    public RiwayatDonasi tambahDonasi(int idDonor, String tanggal, int jumlahKantong)
            throws DataTidakDitemukanException {
        Donor donor = cariDonor(idDonor);
        RiwayatDonasi draft = new RiwayatDonasi(RiwayatDonasi.ID_BELUM_ADA, idDonor, tanggal, jumlahKantong);

        try {
            int idBaru = donasiDAO.insert(draft);
            donor.catatDonasi();      
            donorDAO.update(donor);   
            return donasiDAO.findById(idBaru);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mencatat donasi: " + e.getMessage(), e);
        }
    }

    
    public RiwayatDonasi tambahDonasi(int idDonor, String tanggal) throws DataTidakDitemukanException {
        return tambahDonasi(idDonor, tanggal, 1);
    }

    public List<RiwayatDonasi> getDaftarDonasi() {
        try {
            return donasiDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal membaca data donasi: " + e.getMessage(), e);
        }
    }

    public RiwayatDonasi cariDonasi(int id) throws DataTidakDitemukanException {
        try {
            RiwayatDonasi donasi = donasiDAO.findById(id);
            if (donasi == null) {
                throw new DataTidakDitemukanException("Donasi dengan ID " + id + " tidak ditemukan.");
            }
            return donasi;
        } catch (SQLException e) {
            throw new DatabaseException("Gagal mencari donasi: " + e.getMessage(), e);
        }
    }

    public RiwayatDonasi updateDonasi(int id, String tanggal, int jumlahKantong)
            throws DataTidakDitemukanException {
        RiwayatDonasi donasi = cariDonasi(id);
        donasi.perbaruiData(tanggal, jumlahKantong); 
        try {
            donasiDAO.update(donasi);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal memperbarui donasi: " + e.getMessage(), e);
        }
        return donasi;
    }

    public void hapusDonasi(int id) throws DataTidakDitemukanException {
        cariDonasi(id); 
        try {
            donasiDAO.delete(id);
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghapus donasi: " + e.getMessage(), e);
        }
    }


    public int getJumlahDonor() {
        try {
            return donorDAO.count();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghitung donor: " + e.getMessage(), e);
        }
    }

    public int getJumlahDonasi() {
        try {
            return donasiDAO.count();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghitung donasi: " + e.getMessage(), e);
        }
    }

    public int getTotalKantong() {
        try {
            return donasiDAO.sumKantong();
        } catch (SQLException e) {
            throw new DatabaseException("Gagal menghitung total kantong: " + e.getMessage(), e);
        }
    }
}