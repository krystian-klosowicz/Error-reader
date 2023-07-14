package org.error_reader;

import org.error_reader.ErrorOperations.ErrorService;

import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        ErrorService errorService = new ErrorService();
        long startTime, endTime;


        Scanner scanner = new Scanner(System.in);
        String input;
        boolean flag = true;

        try {
            while (flag) {
                System.out.println("""
                        Dostępne operacje:               \s
                        1. Wczytaj błędy do JSON\s
                        2. Wypisz błędy z JSON na konsoli\s
                        3. Zapisz HashMap do pliku JSON\s
                        4. Zapisz statystyki HashMap do JSON\s
                        5. Wyświetl wykres statystyki HasMap\s
                        6. Wyjście""");
                System.out.println("\nWybierz: ");
                input = scanner.nextLine();

                switch (input) {
                    case "1" -> {
                        System.out.println("Podaj nazwę pliku: ");
                        input = scanner.nextLine();
                        startTime = System.currentTimeMillis();
                        errorService.saveJson(input);
                        endTime = System.currentTimeMillis();
                        System.out.println("\nDodano do pliku errorList.json");
                        System.out.println("Czas operacji to: " + (double) (endTime - startTime) / 1000 + "s\n\n");
                    }
                    case "2" -> {
                        startTime = System.currentTimeMillis();
                        errorService.showJson();
                        endTime = System.currentTimeMillis();
                        System.out.println("Czas operacji to: " + (double) (endTime - startTime) / 1000 + "s\n\n");
                    }
                    case "3" -> {
                        startTime = System.currentTimeMillis();
                        errorService.saveHashMap2();
                        endTime = System.currentTimeMillis();
                        System.out.println("\nUtworzono plik errorMap.json");
                        System.out.println("Czas operacji to: " + (double) (endTime - startTime) / 1000 + "s\n\n");
                    }
                    case "4" -> {
                        startTime = System.currentTimeMillis();
                        errorService.saveHashMapStats();
                        endTime = System.currentTimeMillis();
                        System.out.println("\nUtworzono plik hashMapStats.json");
                        System.out.println("Czas operacji to: " + (double) (endTime - startTime) / 1000 + "s\n\n");
                    }
                    case "5" -> {
                        startTime = System.currentTimeMillis();
                        errorService.showChart();
                        endTime = System.currentTimeMillis();
                        System.out.println("\nWyświetlono plik z wykresem");
                        System.out.println("Czas operacji to: " + (double) (endTime - startTime) / 1000 + "s\n\n");
                    }
                    case "6" -> flag = false;
                    default -> System.out.println("Podałeś niepoprawny numer.\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Nie znaleziono pliku");
        }

    }
}