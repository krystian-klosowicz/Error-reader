package org.error_reader.ErrorOperations;

import org.error_reader.FileOperations.FileReadAndSave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ErrorService {

    public static Error createErrorFromString(String line) {
        Error e1 = Error.builder().build();
        if (line != null) {
            String date = ErrorAnalyzer.getDateFromString(line);
            String time = ErrorAnalyzer.getTimeFromString(line);
            String type = ErrorAnalyzer.getTypeFromString(line);
            String packageName = ErrorAnalyzer.getPackageNameFromString(line);
            String errorInfo = ErrorAnalyzer.getErrorInfoFromString(line);
            String stackInfo = "";
            e1 = Error.builder().date(date).time(time).type(type).packageName(packageName).errorLine(errorInfo).stackInfo(stackInfo).build();
        }
        return e1;
    }


//    public void saveHashMap(String filePath) throws FileNotFoundException {
//        Map<String, List<Error>> errorMap = new HashMap<>();
//        try {
//            List<Error> errorList = FileReadAndSave.readAndParseErrorsFromFile(filePath);
//            for (Error error : errorList) {
//                if (errorMap.containsKey(ErrorAnalyzer.getKey(error))) {
//                    List<Error> l1 = errorMap.get(ErrorAnalyzer.getKey(error));
//                    l1.add(error);
//                    errorMap.put(ErrorAnalyzer.getKey(error), l1);
//                } else {
//                    List<Error> l1 = new ArrayList<>();
//                    l1.add(error);
//                    errorMap.put(ErrorAnalyzer.getKey(error), l1);
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new FileNotFoundException();
//        }
//
//        try {
//            FileReadAndSave.saveToJson(errorMap);
//        } catch (FileNotFoundException e) {
//            throw new FileNotFoundException();
//        }
//
//
//    }

    //Tu się zapisze HashMap do json po wczytaniu wszystkich błędów z pliku errorList.json
    public void saveHashMap() throws FileNotFoundException {
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

        try {
            FileReadAndSave.saveToJson(errorMap);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }

    public void saveHashMapStats() throws IOException {
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

        try {
            FileReadAndSave.saveToJson(errorMapStats);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }


    }

    //Wczytywanie wszystkich błędów z domyślnego pliku tzn errorList.json
    public void saveJson(String filePath) throws FileNotFoundException {
        List<Error> errorList;
        try {
            if (new File("errorList.json").exists()) {
                errorList = FileReadAndSave.loadFromJson();
                errorList.addAll(FileReadAndSave.readAndParseErrorsFromFile(filePath));
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

        TreeMap<String, Integer> sortedMap = new TreeMap<>(new HashMapComparator(errorMapStats));
        sortedMap.putAll(errorMapStats);

        // Tworzenie zestawu danych kategorii z posortowanymi danymi
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Wartości", entry.getKey());
        }

        int columnCount = dataset.getColumnCount();
        for (int i = columnCount - 1; i >= 0; i--) {
            if (i >= 40) {
                Comparable<?> columnKey = dataset.getColumnKey(i);
                dataset.removeColumn(columnKey);
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Wykres błędów",
                "Kategorie błędów",
                "Ilość wystąpień",
                dataset,
                PlotOrientation.HORIZONTAL,
                true, true, false
        );

        ChartFrame frame = new ChartFrame("Wykres błędów", chart);
        frame.setSize(800, 600);  // Ustaw większy rozmiar okna
        frame.setVisible(true);
    }


    private void generateChart(HashMap<String, Integer> dataMap) {




    }

    private static class HashMapComparator implements Comparator<String> {
        HashMap<String, Integer> map;

        public HashMapComparator(HashMap<String, Integer> map) {
            this.map = map;
        }

        public int compare(String a, String b) {
            if (map.get(a) >= map.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
