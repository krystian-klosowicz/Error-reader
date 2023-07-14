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

public class FileReadAndSave {
    public static List<Error> readAndParseErrorsFromFile(String fileName) throws FileNotFoundException {

        //Zmienne wyciągnąć niektóre przed pętle żeby cos nie wykonywało sie bezsensownie kilkanaście razy
        List<Error> errorList = new ArrayList<>();
        String line;
        Error error, lastError;
        String errorInfo;
        StringBuilder stringBuilderErrorInfo;


        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(fileName));
            while ((line = br.readLine()) != null) {
                if (ErrorAnalyzer.isError(line)) {
                    error = ErrorService.createErrorFromString(line);
                    errorList.add(error);

                } else if (!errorList.isEmpty()) {
                    int lastErrorIndex = errorList.size() - 1;
                    lastError = errorList.get(lastErrorIndex);
                    errorInfo = lastError.getStackInfo();
                    stringBuilderErrorInfo = new StringBuilder(errorInfo);
                    stringBuilderErrorInfo.append("\n").append(line);
                    lastError.setStackInfo(stringBuilderErrorInfo.toString());
                }

            }

            br.close();
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
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
        for (Map.Entry<String, List<Error>> entry : errorMap.entrySet()) {
            List<ErrorDto> errorDto = new ArrayList<>();
            for (Error error : entry.getValue()) {
                errorDto.add(new ErrorDto(error));
            }
            errorDtoMap.put(entry.getKey(), errorDto);
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            objectMapper.writeValue(fileWriter, errorDtoMap);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToJson2(Map<String, Integer> errorMapStats) throws FileNotFoundException {
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
