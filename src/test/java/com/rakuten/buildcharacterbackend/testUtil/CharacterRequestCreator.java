package com.rakuten.buildcharacterbackend.testUtil;

import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.AGE;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_CLASS;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_EQUIPMENTS;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_RACE;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_SUBCLASS;

import com.rakuten.buildcharacterbackend.domain.dto.request.CharacterRequest;

public class CharacterRequestCreator {

    public static CharacterRequest createValidRequest() {
        return CharacterRequest
            .builder().age(AGE)
            .charClass(CHAR_CLASS)
            .charSubclass(CHAR_SUBCLASS)
            .charRace(CHAR_RACE)
            .charEquipments(CHAR_EQUIPMENTS)
            .build();
    } 

    public static CharacterRequest createAnInvalidRequest() {
        return CharacterRequest
            .builder()
            .charClass(CHAR_CLASS)
            .charSubclass(CHAR_SUBCLASS)
            .charRace(CHAR_RACE)
            .charEquipments(CHAR_EQUIPMENTS)
            .build();
    } 
}