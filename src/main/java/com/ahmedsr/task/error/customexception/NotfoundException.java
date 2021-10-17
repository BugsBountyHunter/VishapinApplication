package com.ahmedsr.task.error.customexception;

import com.ahmedsr.task.error.ApiBaseException;
import org.springframework.http.HttpStatus;

public class NotfoundException extends ApiBaseException {

    public NotfoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
