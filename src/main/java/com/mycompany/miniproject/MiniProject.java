package com.mycompany.miniproject;

import config.DatabaseConnection;
import controller.DonasiDarahController;
import service.DonasiDarahManager;
import view.ConsoleView;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class MiniProject {

    public static void main(String[] args) {
        try (Connection uji = DatabaseConnection.getConnection()) {
            System.out.println("Koneksi database berhasil.");
        } catch (SQLException e) {
            System.out.println("[GAGAL] Tidak dapat terhubung ke database: " + e.getMessage());
            System.out.println("Pastikan: (1) MySQL/MariaDB sudah berjalan, (2) file sql/donasi_darah.sql");
            System.out.println("sudah dijalankan, (3) user dan password di DatabaseConnection.java sudah benar.");
            return;
        }

        final DonasiDarahManager manager = new DonasiDarahManager();
        final ConsoleView view = new ConsoleView();
        final DonasiDarahController controller = new DonasiDarahController(manager, view);

        try {
            controller.jalankan();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Input ditutup. Program dihentikan.");
        }
    }
}