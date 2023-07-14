package org.error_reader.ErrorOperations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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


}
