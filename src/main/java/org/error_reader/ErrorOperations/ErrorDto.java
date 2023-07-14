package org.error_reader.ErrorOperations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDto {
    private String error;


    public ErrorDto(Error error) {
        this.error =  error.getDate() + " " + error.getTime() + " " + error.getType() + " " + error.getPackageName() + " - " + error.getErrorLine();
    }
}
