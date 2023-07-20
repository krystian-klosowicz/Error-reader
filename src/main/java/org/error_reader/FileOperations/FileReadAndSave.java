package org.error_reader.FileOperations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.error_reader.ErrorOperations.Error;
import org.error_reader.ErrorOperations.ErrorAnalyzer;
import org.error_reader.ErrorOperations.ErrorDto;
import org.error_reader.ErrorOperations.ErrorService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileReadAndSave {
    public static List<Error> readAndParseErrorsFromFile(String fileName) throws FileNotFoundException {

        List<Error> errorList = new ArrayList<>();


        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (ErrorAnalyzer.isError(line)) {
                    Error error = ErrorService.createErrorFromString(line);
                    errorList.add(error);

                } else if (!errorList.isEmpty()) {
                    int lastErrorIndex = errorList.size() - 1;
                    Error lastError = errorList.get(lastErrorIndex);
                    String errorInfo = lastError.getStackInfo();
                    StringBuilder stringBuilderErrorInfo = new StringBuilder(errorInfo);
                    stringBuilderErrorInfo.append("\n").append(line);
                    lastError.setStackInfo(stringBuilderErrorInfo.toString());
                }

            }

            br.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return errorList;
    }


    public static void saveToJson(List<Error> errorList) throws FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File file = new File("errorList.json");

        try (FileWriter fileWriter = new FileWriter(file)) {
            objectMapper.writeValue(fileWriter, errorList);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveToJson(Map<String, List<Error>> errorMap) throws FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File file = new File("errorMap.json");

        Map<String, List<ErrorDto>> errorDtoMap = new HashMap<>();
        errorMap.forEach((key, value) -> errorDtoMap.put(key, value.stream()
                .map(ErrorDto::new)
                .collect(Collectors.toList())));

        try (FileWriter fileWriter = new FileWriter(file)) {
            objectMapper.writeValue(fileWriter, errorDtoMap);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToJson(HashMap<String, Integer> errorMapStats) throws FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File file = new File("errorMapStats.json");


        try (FileWriter fileWriter = new FileWriter(file)) {
            objectMapper.writeValue(fileWriter, errorMapStats);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Error> loadFromJson() throws IOException {
        File file = new File("errorList.json");
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        if (file.length() == 0) {
            throw new IOException();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

}
