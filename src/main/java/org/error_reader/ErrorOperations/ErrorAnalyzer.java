package org.error_reader.ErrorOperations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorAnalyzer {

    private static final String regex = "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2},\\d+ [a-zA-Z]+ \\[(.*?)\\]:\\d+ ";
    private static final String regexGroup = "^(\\d{2}-\\d{2}-\\d{4}) (\\d{2}:\\d{2}:\\d{2},\\d{3}) (\\w+) \\[\\]:\\d+ (.*?)\\s-\\s(.*)$";

    static String getDateFromString(String line) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(line);
        String date = "";
        if (matcher.matches()) {
            date = matcher.group(1);
        }

        return date;
    }

    static String getTimeFromString(String line) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(line);
        String time = "";
        if (matcher.matches()) {
            time = matcher.group(2);
        }

        return time;
    }

    static String getTypeFromString(String line) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(line);
        String type = "";
        if (matcher.matches()) {
            type = matcher.group(3);
        }

        return type;
    }

    static String getPackageNameFromString(String line) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(line);
        String packageName = "";
        if (matcher.matches()) {
            packageName = matcher.group(4);
        }

        return packageName;
    }

    static String getErrorInfoFromString(String line) {
        Pattern pattern = Pattern.compile(regexGroup);
        Matcher matcher = pattern.matcher(line);
        String errorInfo = "";
        if (matcher.matches()) {
            errorInfo = matcher.group(5);
        }

        return errorInfo;
    }

    public static boolean isError(String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }


    public static String getKey(Error e1) {
        String packageNamePlusErrorLine = e1.getPackageName() + " - " + e1.getErrorLine();
        return replaceToXXX(packageNamePlusErrorLine);
    }

    public static String replaceToXXX(String errorLog) {
        String modifiedErrorLog = errorLog.replaceAll("\\[(.*?)\\]", "[XXX]");
        String modifiedErrorLog1 = modifiedErrorLog.replaceAll("=([^,\\s]+)", "=XXX");
        String modifiedErrorLog2 = modifiedErrorLog1.replaceAll("(?i)ID: \\d+", "ID: XXX");
        String modifiedErrorLog3 = modifiedErrorLog2.replaceAll("(?i)Code: \\d+", "Code: XXX");
        String modifiedErrorLog4 = modifiedErrorLog3.replaceAll("(?i)Code: null", "Code: XXX");
        String modifiedErrorLog5 = modifiedErrorLog4.replaceAll("(?i)interrupting: \\d+", "interrupting: XXX");

        String s1 = "REPORTS-INTERRUPTED-POSTFUNCTIONS D&T:";
        if (modifiedErrorLog5.contains(s1)) {
            int id = modifiedErrorLog5.lastIndexOf(s1);
            modifiedErrorLog5 = modifiedErrorLog5.substring(0, (id + s1.length())) + " XXX";
        }

        String s2 = "Invalid float number:";
        if (modifiedErrorLog5.contains(s2)) {
            int id = modifiedErrorLog5.lastIndexOf(s2);
            modifiedErrorLog5 = modifiedErrorLog5.substring(0, (id + s2.length())) + " XXX";
        }

        String s3 = "ERR in DBDataReader.fill: Too many records returned by the query:";
        if (modifiedErrorLog5.contains(s3)) {
            int id = modifiedErrorLog5.lastIndexOf("FROM");
            modifiedErrorLog5 = modifiedErrorLog5.substring(0, (id + "FROM".length()));
        }

        String s4 = "Exception in checkIfURLExists";
        if (modifiedErrorLog5.contains(s4)) {
            int id = modifiedErrorLog5.lastIndexOf(s4);
            modifiedErrorLog5 = modifiedErrorLog5.substring(0, (id + s4.length())) + "(XXX)";
        }
        return modifiedErrorLog5;
    }
}
