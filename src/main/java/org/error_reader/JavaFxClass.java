package org.error_reader;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.error_reader.ErrorOperations.ErrorService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class JavaFxClass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ErrorReader");

        Image icon = new Image("icon.png");
        primaryStage.getIcons().add(icon);

        ErrorService errorService = new ErrorService();
        AtomicLong startTime = new AtomicLong();
        AtomicLong endTime = new AtomicLong();

        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setPrefRowCount(10);

        // Buttony
        Button loadErrorsBtn = new Button("Wczytaj błędy do JSON");
        Button printErrorsBtn = new Button("Wypisz błędy z JSON na konsoli");
        Button saveHashMapBtn = new Button("Zapisz HashMap do pliku JSON");
        Button saveStatsBtn = new Button("Zapisz statystyki HashMap do JSON");
        Button displayChartBtn = new Button("Wyświetl wykres statystyki HashMap");
        Button exitBtn = new Button("Wyjście");
        loadErrorsBtn.setPrefWidth(210);
        loadErrorsBtn.setPrefHeight(40);
        printErrorsBtn.setPrefWidth(210);
        printErrorsBtn.setPrefHeight(40);
        saveHashMapBtn.setPrefWidth(210);
        saveHashMapBtn.setPrefHeight(40);
        saveStatsBtn.setPrefWidth(210);
        saveStatsBtn.setPrefHeight(40);
        displayChartBtn.setPrefWidth(210);
        displayChartBtn.setPrefHeight(40);
        exitBtn.setPrefWidth(105);
        exitBtn.setPrefHeight(40);
        // Akcje dla buttonów
        loadErrorsBtn.setOnAction(e -> {
            resultTextArea.clear();
            resultTextArea.appendText("Wybrano opcję: Wczytaj błędy do JSON\n");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik .txt");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Pliki txt", "*.txt"),
                    new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                String fileName = selectedFile.getPath();
                try {
                    startTime.set(System.currentTimeMillis());
                    errorService.saveJson(fileName);
                    endTime.set(System.currentTimeMillis());
                    resultTextArea.appendText("\nDodano do pliku errorList.json\n");
                    resultTextArea.appendText("Czas operacji to: " + (double) (endTime.get() - startTime.get()) / 1000 + "s\n\n");
                } catch (IOException ex) {
                    resultTextArea.appendText("Nie znaleziono pliku\n");
                }
            } else {
                resultTextArea.appendText("Nie wybrano pliku\n");
            }
        });

        printErrorsBtn.setOnAction(e -> {
            resultTextArea.clear();
            resultTextArea.appendText("Wybrano opcję: Wypisz błędy z JSON na konsoli\n");
            try {
                startTime.set(System.currentTimeMillis());
                errorService.showJson();
                endTime.set(System.currentTimeMillis());
                resultTextArea.appendText("Czas operacji to: " + (double) (endTime.get() - startTime.get()) / 1000 + "s\n\n");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveHashMapBtn.setOnAction(e -> {
            resultTextArea.clear();
            resultTextArea.appendText("Wybrano opcję: Zapisz HashMap do pliku JSON\n");
            try {
                startTime.set(System.currentTimeMillis());
                errorService.saveHashMap();
                endTime.set(System.currentTimeMillis());
                resultTextArea.appendText("Czas operacji to: " + (double) (endTime.get() - startTime.get()) / 1000 + "s\n\n");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveStatsBtn.setOnAction(e -> {
            resultTextArea.clear();
            resultTextArea.appendText("Wybrano opcję: Zapisz statystyki HashMap do JSON\n");
            try {
                startTime.set(System.currentTimeMillis());
                errorService.saveHashMapStats();
                endTime.set(System.currentTimeMillis());
                resultTextArea.appendText("Czas operacji to: " + (double) (endTime.get() - startTime.get()) / 1000 + "s\n\n");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        displayChartBtn.setOnAction(e -> {
            resultTextArea.clear();
            resultTextArea.appendText("Wybrano opcję: Wyświetl wykres statystyki HashMap\n");
            try {
                startTime.set(System.currentTimeMillis());
                errorService.showChart();
                endTime.set(System.currentTimeMillis());
                resultTextArea.appendText("Czas operacji to: " + (double) (endTime.get() - startTime.get()) / 1000 + "s\n\n");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        exitBtn.setOnAction(e -> {
            primaryStage.close();
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(resultTextArea, loadErrorsBtn, printErrorsBtn, saveHashMapBtn, saveStatsBtn, displayChartBtn, exitBtn);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}