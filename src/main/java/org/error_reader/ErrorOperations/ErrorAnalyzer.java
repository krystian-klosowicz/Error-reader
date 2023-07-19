package org.error_reader.ErrorOperations;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorAnalyzer {

    private static final String regexGroup = "^(\\d{2}-\\d{2}-\\d{4}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) (\\w+) \\[\\]:\\d+ (.*?)\\s-\\s(.*)$";

    private static final List<Integer> groupNumbers = Arrays.asList(1, 2, 3, 4, 5);

    static String getValueFromString(String line, int number) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(line);
        String value = "";
        if (matcher.matches() && groupNumbers.contains(number)) {
            value = matcher.group(number);
        }

        return value;
    }

    public static boolean isError(String input) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    public static String getKey(Error e1) {
        return replaceToXXX(new StringBuilder(e1.getPackageName()+ " - " + e1.getErrorLine()).toString());
    }

    public static String replaceToXXX(String errorLog) {
        String modifiedErrorLog = errorLog.replaceAll("\\[(.*?)\\]", "[XXX]");
        String modifiedErrorLog1 = modifiedErrorLog.replaceAll("=([^,\\s]+)", "=XXX");
        String modifiedErrorLog2 = modifiedErrorLog1.replaceAll("(?i)ID: \\d+", "ID: XXX");
        String modifiedErrorLog3 = modifiedErrorLog2.replaceAll("(?i)Code: \\d+", "Code: XXX")
                .replaceAll("(?i)Code: null", "Code: XXX");
        String modifiedErrorLog4 = modifiedErrorLog3.replaceAll("(?i)Job: \\d+", "Job: XXX");
        String modifiedErrorLog5 = modifiedErrorLog4.replaceAll("Exception:\\d+", "Exception:XXX")
                .replaceAll("START: Exception form CLIENT", "START: XXX")
                .replaceAll("Computer User Name:[^,]+", "Computer User Name:XXX")
                .replaceAll("Computer Name:[^,]+", "Computer Name:XXX")
                .replaceAll("Entry from secString:\\d+", "Entry from secString:XXX")
                .replaceAll("Application user login:[^,]+", "Application user login:XXX")
                .replaceAll("Application userID:[^,]+", "Application userID:XXX")
                .replaceAll("END: Exception form CLIENT", "END: XXX");
        String modifiedErrorLog6 = modifiedErrorLog5.replaceAll("(?i)interrupting: \\d+", "interrupting: XXX");

        String s1 = "REPORTS-INTERRUPTED-POSTFUNCTIONS D&T:";
        if (modifiedErrorLog6.contains(s1)) {
            int id = modifiedErrorLog6.lastIndexOf(s1);
            modifiedErrorLog6 = modifiedErrorLog6.substring(0, (id + s1.length())) + " XXX";
        }

        String s2 = "Invalid float number:";
        if (modifiedErrorLog6.contains(s2)) {
            int id = modifiedErrorLog6.lastIndexOf(s2);
            modifiedErrorLog6 = modifiedErrorLog6.substring(0, (id + s2.length())) + " XXX";
        }

        String s3 = "ERR in DBDataReader.fill: Too many records returned by the query:";
        if (modifiedErrorLog6.contains(s3)) {
            int id = modifiedErrorLog6.lastIndexOf("FROM");
            modifiedErrorLog6 = modifiedErrorLog6.substring(0, (id + "FROM".length()));
        }

        String s4 = "Exception in checkIfURLExists";
        if (modifiedErrorLog6.contains(s4)) {
            int id = modifiedErrorLog6.lastIndexOf(s4);
            modifiedErrorLog6 = modifiedErrorLog6.substring(0, (id + s4.length())) + "(XXX)";
        }
        return modifiedErrorLog6;
    }
}
