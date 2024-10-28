package com.example.testaktualizacji;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class UpdateController {

    private static final String VERSION_FILE_URL = "https://drive.google.com/file/d/1d27Ga-zrQ8x1VDrbd1uBvzvTnjYeR8jT/view?usp=drive_link"; // link do pliku z wersją
    private static final String UPDATE_FILE_URL = "https://drive.google.com/file/d/1d27Ga-zrQ8x1VDrbd1uBvzvTnjYeR8jT/view?usp=drive_link"; // link do pliku aktualizacji

    @FXML
    private Button updateButton;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Nowa wersja aplikacji");
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
        try {
            // Sprawdzanie wersji aplikacji
            String currentVersion = "1.0"; // aktualna wersja aplikacji
            String latestVersion = getLatestVersionFromDrive();

            return !currentVersion.equals(latestVersion);
        } catch (Exception e) {
            showAlert("Błąd sprawdzania aktualizacji: " + e.getMessage());
            return false;
        }
    }

    private String getLatestVersionFromDrive() throws Exception {
        URL url = new URL(VERSION_FILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.readLine().trim(); // Zakładam, że wersja jest w pierwszej linii
        }
    }

    private void updateApplication() {
        showAlert("Rozpoczynam aktualizację...");

        new Thread(() -> {
            try {
                downloadUpdateFile(UPDATE_FILE_URL);
                // Możesz tutaj uruchomić instalator aktualizacji, jeśli to konieczne
                Platform.runLater(() -> showAlert("Aktualizacja zakończona pomyślnie!"));
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Błąd podczas aktualizacji: " + e.getMessage()));
            }
        }).start();
    }

    private void downloadUpdateFile(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Sprawdzenie, czy odpowiedź jest poprawna
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Błąd podczas pobierania pliku: " + connection.getResponseCode());
        }

        // Zapisz plik aktualizacji
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream("update.zip")) { // Zapisz plik lokalnie
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // Rozpakowywanie pliku zip, jeśli to konieczne
        unzip("update.zip", "update_directory");
        Files.delete(new File("update.zip").toPath()); // Usuń plik zip po rozpakowaniu
    }

    private void unzip(String zipFilePath, String destDir) {
        // Tutaj dodaj logikę rozpakowywania pliku zip
        // Możesz użyć biblioteki Java z `java.util.zip` lub innej zewnętrznej biblioteki
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}