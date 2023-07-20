package org.error_reader.ErrorOperations;

import org.error_reader.FileOperations.FileReadAndSave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class ErrorService {

    public static Error createErrorFromString(String line) {
        Error e1 = Error.builder().build();
        if (line != null) {
            String date = ErrorAnalyzer.getValueFromString(line, 1);
            String time = ErrorAnalyzer.getValueFromString(line, 2);
            String type = ErrorAnalyzer.getValueFromString(line, 3);
            String packageName = ErrorAnalyzer.getValueFromString(line, 4);
            String errorInfo = ErrorAnalyzer.getValueFromString(line, 5);
            String stackInfo = "";
            e1 = Error.builder().date(date).time(time).type(type).packageName(packageName).errorLine(errorInfo).stackInfo(stackInfo).build();
        }
        return e1;
    }


    //Tu się zapisze HashMap do json po wczytaniu wszystkich błędów z pliku errorList.json
    public void saveHashMap() throws FileNotFoundException {
        Map<String, List<Error>> errorMap = generateHashMap();


        try {
            FileReadAndSave.saveToJson(errorMap);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    public void saveHashMapStats() throws IOException {
        HashMap<String, Integer> errorMapStats = generateHashMapNumberOfOccurrencesByError();

        try {
            FileReadAndSave.saveToJson(errorMapStats);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }


    }

    private Map<String, List<Error>> generateHashMap() throws FileNotFoundException {
        try {
            // Read error list from json file
            List<Error> errorList = FileReadAndSave.loadFromJson();

            // Return map using streams and group by error key
            return errorList.stream().collect(Collectors.groupingBy(ErrorAnalyzer::getKey));

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String, Integer> generateHashMapNumberOfOccurrencesByError() throws FileNotFoundException {
        try {
            // Read error list from json file
            List<Error> errorList = FileReadAndSave.loadFromJson();

            // Return hashMap using streams and group by error key
            return (HashMap<String, Integer>) errorList.stream().collect(Collectors.groupingBy(ErrorAnalyzer::getKey, Collectors.summingInt(error -> 1)));

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private HashMap<String, Integer> generateHashMapNumberOfOccurrencesByDate() throws FileNotFoundException {
        try {
            // Read error list from json file
            List<Error> errorList = FileReadAndSave.loadFromJson();

            // Return hashMap using streams and group by date
            return (HashMap<String, Integer>) errorList.stream().collect(Collectors.groupingBy(Error::getDate, Collectors.summingInt(error -> 1)));

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Wczytywanie wszystkich błędów z domyślnego pliku tzn errorList.json
    public void saveJson(String filePath) throws FileNotFoundException {
        try {
            List<Error> newErrorList = FileReadAndSave.readAndParseErrorsFromFile(filePath);

            if (new File("errorList.json").exists()) {
                List<Error> errorList = FileReadAndSave.loadFromJson();
                List<Error> uniqueErrors;
                uniqueErrors = newErrorList.stream().distinct().filter(newError -> !errorList.contains(newError)).collect(Collectors.toList());

                errorList.addAll(uniqueErrors);
                FileReadAndSave.saveToJson(errorList);

            } else {
                FileReadAndSave.saveToJson(newErrorList);
            }


        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Wypisanie wszystkich błędów z domyślnego pliku tzn errorList.json (jeśli istnieje)
    public void showJson() throws IOException {
        try {
            List<Error> errorList = FileReadAndSave.loadFromJson();
            for (Error error : errorList) {
                System.out.println("---------------------------------------------------------------------------------");
                System.out.println("date : " + error.getDate());
                System.out.println("time : " + error.getTime());
                System.out.println("type : " + error.getType());
                System.out.println("packageName : " + error.getPackageName());
                System.out.println("errorLine : " + error.getErrorLine());
                System.out.println("---------------------------------------------------------------------------------");
            }
            System.out.println("Ilość błędów : " + errorList.size());
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    // Generowanie wykresu przy pomocy biblioteki JFreeChart
    public void showChart() throws FileNotFoundException {
        // Chart 1
        HashMap<String, Integer> errorMapStats = generateHashMapNumberOfOccurrencesByError();

        TreeMap<String, Integer> sortedMap = new TreeMap<>(new HashMapComparator(errorMapStats));
        sortedMap.putAll(errorMapStats);

        // Tworzenie zestawu danych kategorii z posortowanymi danymi
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            dataset1.addValue(entry.getValue(), "Błąd", entry.getKey());
        }

        int columnCount = dataset1.getColumnCount();
        for (int i = columnCount - 1; i >= 0; i--) {
            if (i >= 50) {
                Comparable<?> columnKey = dataset1.getColumnKey(i);
                dataset1.removeColumn(columnKey);
            }
        }

        JFreeChart chart = ChartFactory.createBarChart("Wykres błędów", "Kategorie błędów", "Ilość wystąpień", dataset1, PlotOrientation.HORIZONTAL, true, true, false);

        // Chart 2

        errorMapStats = generateHashMapNumberOfOccurrencesByDate();

        sortedMap = new TreeMap<>(errorMapStats);

        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            dataset2.addValue(entry.getValue(), "Data", entry.getKey());
        }

        // cutting
        columnCount = dataset2.getColumnCount();
        for (int i = columnCount - 1; i >= 0; i--) {
            if (i >= 50) {
                Comparable<?> columnKey = dataset2.getColumnKey(i);
                dataset2.removeColumn(columnKey);
            }
        }

        JFreeChart chart2 = ChartFactory.createBarChart("Wykres błędów", "Kategorie błędów", "Ilość wystąpień", dataset2, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        ChartPanel chartPanel2 = new ChartPanel(chart2);
        chartPanel2.setPreferredSize(new java.awt.Dimension(800, 600));

        // Create a TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Wykres 1", chartPanel);
        tabbedPane.addTab("Wykres 2", chartPanel2);

        // Create the main frame
        JFrame frame = new JFrame("Wykresy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);
    }

}
