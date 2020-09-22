package com.rakuten.buildcharacterbackend.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.rakuten.buildcharacterbackend.domain.dto.request.CharacterRequest;
import com.rakuten.buildcharacterbackend.domain.dto.request.SessionRequest;
import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;
import com.rakuten.buildcharacterbackend.testUtil.CharacterRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.RpgCharacterClientResponseCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionResponseCreator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class MapperTest {

    @Test
    @DisplayName("convertToRPGCharacterDataStructure converts RpgCharacterClientResponse to RPGCharacterDataStructure when successful")
    public void convertToRPGCharacterDataStructure_convertsRpgCharacterClientResponseToRPGCharacterDataStructure_whenSuccessful() {
      //Given
        //Expected
      List<RPGCharacterDataStructure> expectedResponse = RpgCharacterClientResponseCreator.createValidClassesClientMapped();
        //Requests Params
      ResponseEntity<RpgCharacterClientResponse> clientResponse = RpgCharacterClientResponseCreator.createValidClassesClientResponse();

      //When
      List<RPGCharacterDataStructure> actualResponse = Mapper.convertToRPGCharacterDataStructure(clientResponse);

      //Then
      assertThat(actualResponse).isNotNull();
      assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("convertSessionDTOtoEntitySession converts SessionRequest to Session when successful")
    public void convertSessionDTOtoEntitySession_convertsSessionRequestToSession_whenSuccessful() {
      //Given
        //Expected
      Session expectedResponse = SessionResponseCreator.createValidSessionMapped();
        //Requests Params
      SessionRequest sessionResponse = SessionRequestCreator.createValidRequest();
      long ttl = 600L;

      //When
      Session actualResponse = Mapper.convertSessionDTOtoEntitySession(sessionResponse,ttl);

      //Then
      assertThat(actualResponse).isNotNull();
      assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("convertCharacterDTOtoCurrentEntitySession converts CharacterRequest to Session when successful")
    public void convertCharacterDTOtoCurrentEntitySession_convertsCharacterRequestToSession_whenSuccessful() {
      //Given
        //Expected
      Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
        //Requests Params
      CharacterRequest characterRequest = CharacterRequestCreator.createValidRequest();
      Session sessionRequest = SessionResponseCreator.createValidSessionFromRepository();

      //When
      Session actualResponse = Mapper.convertCharacterDTOtoCurrentEntitySession(characterRequest,sessionRequest);

      //Then
      assertThat(actualResponse).isNotNull();
      assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}