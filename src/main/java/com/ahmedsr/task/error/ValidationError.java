package com.ahmedsr.task.error;

import com.ahmedsr.task.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ValidationError {

    private List<String> errors;
    private String uri;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_PATTERN)
    private Date timestamp;

    public ValidationError(){
        timestamp = new Date();
        errors = new ArrayList<>();
    }

    public void addError(String error){
        this.errors.add(error);
    }

}
