package com.rakuten.buildcharacterbackend.domain.dto.response.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    public String errorMessage;
    public ErrorCause cause;

    public static ResponseEntity<Object> build(HttpStatus httpStatus, ErrorCause errorCause, String errorMessage) {
        if(httpStatus == null) httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if(errorCause == null) errorCause = ErrorCause.ERROR_CAUSE_UNSPECIFIED;
        if(errorMessage == null || errorMessage.isEmpty()) errorMessage = "error unknown";

        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(errorMessage, errorCause));
    }
}
