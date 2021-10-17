package com.ahmedsr.task.error;

import com.ahmedsr.task.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ErrorDetails {
    private String message;
    private String uri;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_PATTERN)
    private Date timestamp;

    public ErrorDetails(){
        this.timestamp = new Date();
    }

    public ErrorDetails(String message, String uri) {
        this();
        this.message = message;
        this.uri = uri;
    }
}
