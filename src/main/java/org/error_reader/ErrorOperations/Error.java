package org.error_reader.ErrorOperations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    @JsonProperty
    private String date;
    @JsonProperty
    private String time;
    @JsonProperty
    private String type;
    @JsonProperty
    private String packageName;
    @JsonProperty
    private String errorLine;
//    @JsonProperty
    @JsonIgnore
    private String stackInfo;


    @Override
    public String toString() {
        return date + " " + time + " " + type + " " + packageName + " - " + errorLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error)) return false;
        Error error = (Error) o;
        return Objects.equals(date, error.date) &&
                Objects.equals(time, error.time) &&
                Objects.equals(type, error.type) &&
                Objects.equals(packageName, error.packageName) &&
                Objects.equals(errorLine, error.errorLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, type, packageName, errorLine);
    }
}
