package com.example.testaktualizacji;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Nowa wersja aplikacji !!!!!!!!!!!!!!");
    }

    @FXML
    public void handleUpdate() {

        if (isUpdateAvailable()) {

            updateApplication();
        } else {
            showAlert("Brak dostępnych aktualizacji.");
        }
    }

    private boolean isUpdateAvailable() {


        return true;
    }

    private void updateApplication() {

        showAlert("Rozpoczynam aktualizację...");

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> showAlert("Aktualizacja zakończona pomyślnie!"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}