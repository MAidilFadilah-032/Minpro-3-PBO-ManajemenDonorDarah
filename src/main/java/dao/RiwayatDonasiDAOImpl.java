package dao;

import config.DatabaseConnection;
import model.RiwayatDonasi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RiwayatDonasiDAOImpl implements RiwayatDonasiDAO {

    private static final String SQL_INSERT =
            "INSERT INTO riwayat_donasi (id_donor, tanggal_donasi, jumlah_kantong) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM riwayat_donasi ORDER BY id_donasi";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM riwayat_donasi WHERE id_donasi = ?";
    private static final String SQL_UPDATE =
            "UPDATE riwayat_donasi SET tanggal_donasi = ?, jumlah_kantong = ? WHERE id_donasi = ?";
    private static final String SQL_DELETE = "DELETE FROM riwayat_donasi WHERE id_donasi = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM riwayat_donasi";
    private static final String SQL_COUNT_BY_DONOR = "SELECT COUNT(*) FROM riwayat_donasi WHERE id_donor = ?";
    private static final String SQL_SUM_KANTONG = "SELECT COALESCE(SUM(jumlah_kantong), 0) FROM riwayat_donasi";

    @Override
    public int insert(RiwayatDonasi donasi) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, donasi.getIdDonor());
            ps.setDate(2, Date.valueOf(donasi.getTanggalDonasi()));
            ps.setInt(3, donasi.getJumlahKantong());
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    return kunci.getInt(1);
                }
            }
            throw new SQLException("ID donasi baru tidak berhasil dibuat oleh database.");
        }
    }

    @Override
    public List<RiwayatDonasi> findAll() throws SQLException {
        List<RiwayatDonasi> hasil = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                hasil.add(petakanBaris(rs));
            }
        }
        return hasil;
    }

    @Override
    public RiwayatDonasi findById(int idDonasi) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idDonasi);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakanBaris(rs) : null;
            }
        }
    }

    @Override
    public boolean update(RiwayatDonasi donasi) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setDate(1, Date.valueOf(donasi.getTanggalDonasi()));
            ps.setInt(2, donasi.getJumlahKantong());
            ps.setInt(3, donasi.getIdDonasi());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int idDonasi) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, idDonasi);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int count() throws SQLException {
        return ambilAngka(SQL_COUNT, null);
    }

    @Override
    public int countByDonor(int idDonor) throws SQLException {
        return ambilAngka(SQL_COUNT_BY_DONOR, idDonor);
    }

    @Override
    public int sumKantong() throws SQLException {
        return ambilAngka(SQL_SUM_KANTONG, null);
    }

    private int ambilAngka(String sql, Integer parameter) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (parameter != null) {
                ps.setInt(1, parameter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private RiwayatDonasi petakanBaris(ResultSet rs) throws SQLException {
        return new RiwayatDonasi(
                rs.getInt("id_donasi"),
                rs.getInt("id_donor"),
                rs.getDate("tanggal_donasi").toLocalDate(),
                rs.getInt("jumlah_kantong"));
    }
}