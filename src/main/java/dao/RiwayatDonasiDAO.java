package dao;

import model.RiwayatDonasi;
import java.sql.SQLException;
import java.util.List;

public interface RiwayatDonasiDAO {

    int insert(RiwayatDonasi donasi) throws SQLException;

    List<RiwayatDonasi> findAll() throws SQLException;

    RiwayatDonasi findById(int idDonasi) throws SQLException;

    boolean update(RiwayatDonasi donasi) throws SQLException;

    boolean delete(int idDonasi) throws SQLException;

    int count() throws SQLException;

    int countByDonor(int idDonor) throws SQLException;

    int sumKantong() throws SQLException;
}