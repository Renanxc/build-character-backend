package com.rakuten.buildcharacterbackend.util;

import com.rakuten.buildcharacterbackend.api.v1.exception.TTlOutOfRangeException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class TtlUtilsTest {

    @InjectMocks
    TtlUtils util = new TtlUtils(100, 600);

    @Test
    @DisplayName("verifyRange throws TTlOutOfRangeException when ttl sended is not in range")
    public void verifyRange_throwsTTlOutOfRangeException_whenTtlSendedIsNotInRange() {
        //Given    
            //Requests Params
        Long ttl = 0L;


        //Then
        Assertions.assertThatExceptionOfType(TTlOutOfRangeException.class)
            .isThrownBy( () -> util.verifyRange(ttl));
    }
}