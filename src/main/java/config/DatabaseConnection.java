package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String NAMA_DATABASE = "donasi_darah";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP biasanya kosong

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAMA_DATABASE
            + "?useSSL=false&allowPublicKeyRetrieval=true";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}