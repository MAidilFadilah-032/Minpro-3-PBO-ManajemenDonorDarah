package dao;

import config.DatabaseConnection;
import model.Donor;
import model.DonorBaru;
import model.DonorTetap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DonorDAOImpl implements DonorDAO {

    private static final String KATEGORI_BARU = "BARU";
    private static final String KATEGORI_TETAP = "TETAP";

    private static final String SQL_INSERT =
            "INSERT INTO donor (nama, golongan_darah, umur, no_telepon, kategori, sumber_informasi, jumlah_donasi) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM donor ORDER BY id";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM donor WHERE id = ?";
    private static final String SQL_SELECT_BY_NAMA =
            "SELECT * FROM donor WHERE LOWER(nama) LIKE ? ORDER BY id";
    private static final String SQL_UPDATE =
            "UPDATE donor SET nama = ?, golongan_darah = ?, umur = ?, no_telepon = ?, "
            + "kategori = ?, sumber_informasi = ?, jumlah_donasi = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM donor WHERE id = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM donor";

    @Override
    public int insert(Donor donor) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            isiParameter(ps, donor);
            ps.executeUpdate();
            try (ResultSet kunci = ps.getGeneratedKeys()) {
                if (kunci.next()) {
                    return kunci.getInt(1);
                }
            }
            throw new SQLException("ID donor baru tidak berhasil dibuat oleh database.");
        }
    }

    @Override
    public List<Donor> findAll() throws SQLException {
        List<Donor> hasil = new ArrayList<>();
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
    public Donor findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? petakanBaris(rs) : null;
            }
        }
    }

    @Override
    public List<Donor> findByNama(String kataKunci) throws SQLException {
        List<Donor> hasil = new ArrayList<>();
        String kunci = (kataKunci == null) ? "" : kataKunci.trim().toLowerCase();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_NAMA)) {
            ps.setString(1, "%" + kunci + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(petakanBaris(rs));
                }
            }
        }
        return hasil;
    }

    @Override
    public boolean update(Donor donor) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            isiParameter(ps, donor);
            ps.setInt(8, donor.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public int count() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_COUNT);
                ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void isiParameter(PreparedStatement ps, Donor donor) throws SQLException {
        ps.setString(1, donor.getNama());
        ps.setString(2, donor.getGolonganDarah());
        ps.setInt(3, donor.getUmur());
        ps.setString(4, donor.getNoTelepon());

        if (donor instanceof DonorBaru baru) {
            ps.setString(5, KATEGORI_BARU);
            ps.setString(6, baru.getSumberInformasi());
            ps.setNull(7, Types.INTEGER);
        } else if (donor instanceof DonorTetap tetap) {
            ps.setString(5, KATEGORI_TETAP);
            ps.setNull(6, Types.VARCHAR);
            ps.setInt(7, tetap.getJumlahDonasi());
        } else {
            throw new SQLException("Jenis donor tidak dikenali: " + donor.getClass().getSimpleName());
        }
    }

    private Donor petakanBaris(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nama = rs.getString("nama");
        String golongan = rs.getString("golongan_darah");
        int umur = rs.getInt("umur");
        String telepon = rs.getString("no_telepon");
        String kategori = rs.getString("kategori");

        if (KATEGORI_TETAP.equals(kategori)) {
            return new DonorTetap(id, nama, golongan, umur, telepon, rs.getInt("jumlah_donasi"));
        }
        if (KATEGORI_BARU.equals(kategori)) {
            return new DonorBaru(id, nama, golongan, umur, telepon, rs.getString("sumber_informasi"));
        }
        throw new SQLException("Kategori donor tidak dikenali di database: " + kategori);
    }
}