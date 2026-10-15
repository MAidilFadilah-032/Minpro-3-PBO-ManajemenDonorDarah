package dao;

import model.Donor;
import java.sql.SQLException;
import java.util.List;

public interface DonorDAO {

    int insert(Donor donor) throws SQLException;

    List<Donor> findAll() throws SQLException;

    Donor findById(int id) throws SQLException;

    List<Donor> findByNama(String kataKunci) throws SQLException;

    boolean update(Donor donor) throws SQLException;

    boolean delete(int id) throws SQLException;

    int count() throws SQLException;
}