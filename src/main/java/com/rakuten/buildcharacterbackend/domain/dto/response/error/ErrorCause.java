package com.rakuten.buildcharacterbackend.domain.dto.response.error;

import lombok.Getter;

@Getter
public enum ErrorCause {

    ERROR_CAUSE_UNSPECIFIED, // The cause of the error is unknown or is not specified below.

    BAD_REQUEST, // Original request was malformed.

    NOT_FOUND,

    UNAUTHORIZED,

    BACKEND_FAILURE, 

    TOO_MANY_REQUESTS, // GTAF is making too many requests to the DPA.

    SERVICE_UNAVAILABLE, // Requested service is unavailable. Please retry later

}
