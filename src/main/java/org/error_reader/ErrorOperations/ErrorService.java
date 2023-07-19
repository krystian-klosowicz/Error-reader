package org.error_reader.ErrorOperations;

import org.error_reader.FileOperations.FileReadAndSave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
        Map<String, List<Error>> errorMap = new HashMap<>();
        try {
            //Tu ładnie wczytuje wszystkie dostępne błędy
            List<Error> errorList = FileReadAndSave.loadFromJson();
            for (Error error : errorList) {
                if (errorMap.containsKey(ErrorAnalyzer.getKey(error))) {
                    List<Error> l1 = errorMap.get(ErrorAnalyzer.getKey(error));
                    l1.add(error);
                    errorMap.put(ErrorAnalyzer.getKey(error), l1);
                } else {
                    List<Error> l1 = new ArrayList<>();
                    l1.add(error);
                    errorMap.put(ErrorAnalyzer.getKey(error), l1);
                }
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMap;
    }

    private HashMap<String, Integer> generateHashMapNumberOfOccurrencesByError() throws FileNotFoundException {

        HashMap<String, Integer> errorMapStats = new HashMap<>();

        try {
            //Tu ładnie wczytuje wszystkie dostępne błędy
            List<Error> errorList = FileReadAndSave.loadFromJson();
            int counter;
            for (Error error : errorList) {
                if (errorMapStats.containsKey(ErrorAnalyzer.getKey(error))) {
                    counter = errorMapStats.get(ErrorAnalyzer.getKey(error)) + 1;
                    errorMapStats.put(ErrorAnalyzer.getKey(error), counter);
                } else {
                    errorMapStats.put(ErrorAnalyzer.getKey(error), 1);
                }
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMapStats;
    }

    private HashMap<String, Integer> generateHashMapNumberOfOccurrencesByDate() throws FileNotFoundException {

        HashMap<String, Integer> errorMapStats = new HashMap<>();

        try {
            //Tu ładnie wczytuje wszystkie dostępne błędy
            List<Error> errorList = FileReadAndSave.loadFromJson();
            int counter;
            for (Error error : errorList) {
                if (errorMapStats.containsKey(error.getDate())) {
                    counter = errorMapStats.get(error.getDate()) + 1;
                    errorMapStats.put(error.getDate(), counter);
                } else {
                    errorMapStats.put(error.getDate(), 1);
                }
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMapStats;
    }

    //Wczytywanie wszystkich błędów z domyślnego pliku tzn errorList.json
    public void saveJson(String filePath) throws FileNotFoundException {
        List<Error> errorList;
        try {
            if (new File("errorList.json").exists()) {
                errorList = FileReadAndSave.loadFromJson();
                List<Error> newErrorList  = FileReadAndSave.readAndParseErrorsFromFile(filePath);
                for (Error newError : newErrorList) {
                    if(!errorList.contains(newError)) {
                        errorList.add(newError);
                    }
                }
            } else {
                errorList = FileReadAndSave.readAndParseErrorsFromFile(filePath);
            }

            FileReadAndSave.saveToJson(errorList);
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

        JFreeChart chart = ChartFactory.createBarChart(
                "Wykres błędów",
                "Kategorie błędów",
                "Ilość wystąpień",
                dataset1,
                PlotOrientation.HORIZONTAL,
                true, true, false
        );

        // Chart 2

        errorMapStats = generateHashMapNumberOfOccurrencesByDate();

        sortedMap = new TreeMap<>();
        sortedMap.putAll(errorMapStats);

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

        JFreeChart chart2 = ChartFactory.createBarChart(
                "Wykres błędów",
                "Kategorie błędów",
                "Ilość wystąpień",
                dataset2,
                PlotOrientation.VERTICAL,
                true, true, false
        );
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
