package com.rakuten.buildcharacterbackend.testUtil;

import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.EMAIL;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.NAME;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.SESSION_ID;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.SURNAME;

import com.rakuten.buildcharacterbackend.domain.dto.request.SessionRequest;

public class SessionRequestCreator {

    public static String createValidTokenRequest() {
        return SESSION_ID;
    } 

    public static SessionRequest createValidRequest() {
        return SessionRequest.builder()
            .name(NAME)
            .surname(SURNAME)
            .email(EMAIL)
            .build();
    } 

    public static SessionRequest createAnInvalidRequest() {
        return SessionRequest.builder()
            .name(NAME + " Invalid")
            .surname(SURNAME)
            .email(EMAIL)
            .build();
    } 
}
