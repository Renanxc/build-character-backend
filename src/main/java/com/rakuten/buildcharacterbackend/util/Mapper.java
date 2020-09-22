package com.rakuten.buildcharacterbackend.util;

import java.util.List;
import java.util.stream.Collectors;

import com.rakuten.buildcharacterbackend.domain.dto.request.CharacterRequest;
import com.rakuten.buildcharacterbackend.domain.dto.request.SessionRequest;
import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
public class Mapper {

    public static List<RPGCharacterDataStructure> convertToRPGCharacterDataStructure(
            ResponseEntity<RpgCharacterClientResponse> apiResponse) {
        return apiResponse.getBody().getResults().stream().map(
                result -> RPGCharacterDataStructure.builder().name(result.getName()).index(result.getIndex()).build())
                .collect(Collectors.toList());
    }

    public static Session convertSessionDTOtoEntitySession(SessionRequest dto, Long ttl) {
        return Session.builder().name(dto.getName()).surname(dto.getSurname()).email(dto.getEmail()).ttl(ttl).build();
    }

    public static Session convertCharacterDTOtoCurrentEntitySession(CharacterRequest dto, Session currentSession) {
        currentSession.setAge(dto.getAge());
        currentSession.setCharClass(dto.getCharClass());
        currentSession.setCharSubclass(dto.getCharSubclass());
        currentSession.setCharRace(dto.getCharRace());
        currentSession.setCharEquipments(dto.getCharEquipments());
        currentSession.setCharSpells(dto.getCharSpells());

        return currentSession;
    }
}