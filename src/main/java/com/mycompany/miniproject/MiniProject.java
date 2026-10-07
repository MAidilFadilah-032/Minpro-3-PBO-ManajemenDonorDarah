package com.mycompany.miniproject;

import com.mycompany.miniproject.controller.DonasiDarahController;
import com.mycompany.miniproject.model.DonasiDarahManager;
import com.mycompany.miniproject.view.ConsoleView;
import java.util.NoSuchElementException;

public class MiniProject {

    public static void main(String[] args) {
        final DonasiDarahManager model = new DonasiDarahManager();
        final ConsoleView view = new ConsoleView();
        final DonasiDarahController controller = new DonasiDarahController(model, view);

        try {
            controller.jalankan();
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("Input ditutup. Program dihentikan.");
        }
    }
}