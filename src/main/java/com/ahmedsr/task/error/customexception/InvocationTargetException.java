package com.ahmedsr.task.error.customexception;

import com.ahmedsr.task.error.ApiBaseException;
import org.springframework.http.HttpStatus;

public class InvocationTargetException extends ApiBaseException {
    public InvocationTargetException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_GATEWAY;
    }
}
