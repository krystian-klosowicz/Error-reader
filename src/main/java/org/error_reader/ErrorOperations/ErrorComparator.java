package org.error_reader.ErrorOperations;

import java.util.Comparator;

public class ErrorComparator implements Comparator<Error> {

    @Override
    public int compare(Error e1, Error e2) {
        return CharSequence.compare(e1.getErrorLine(),e2.getErrorLine());
    }
}
