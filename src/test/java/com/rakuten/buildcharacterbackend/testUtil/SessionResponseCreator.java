package com.rakuten.buildcharacterbackend.testUtil;

import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.AGE;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_CLASS;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_EQUIPMENTS;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_RACE;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.CHAR_SUBCLASS;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.EMAIL;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.NAME;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.SESSION_ID;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.SESSION_ID2;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.SURNAME;
import static com.rakuten.buildcharacterbackend.testUtil.SessionMockConstants.TTL;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rakuten.buildcharacterbackend.domain.dto.response.SessionResponse;
import com.rakuten.buildcharacterbackend.domain.entity.Session;

import org.springframework.data.redis.core.RedisHash;

public class SessionResponseCreator {


    public static SessionResponse creatValidResponse() {
        return new SessionResponse(SESSION_ID);
    } 
    
    public static String creatValidHashKeyResponse() {
        return String.format("%s:%s", Session.class.getAnnotation(RedisHash.class).value(), SESSION_ID);
    } 

    public static Session createValidSessionFromRepository() {
        return Session.builder().id(SESSION_ID).name(NAME).surname(SURNAME).email(EMAIL).ttl(TTL).build();
    } 

    public static Session createValidSessionMapped() {
        return Session.builder().name(NAME).surname(SURNAME).email(EMAIL).ttl(TTL).build();
    } 

    public static Session creatValidSessionResponse() {
        return creatValidListSessionResponse().stream().findFirst().get();
    } 

    public static List<Session> creatValidListSessionResponse() {
        return Stream.of(        
                Session.builder()
                    .id(SESSION_ID)
                    .name(NAME)
                    .surname(SURNAME)
                    .email(EMAIL)
                    .age(AGE)
                    .charClass(CHAR_CLASS)
                    .charSubclass(CHAR_SUBCLASS)
                    .charRace(CHAR_RACE)
                    .charEquipments(CHAR_EQUIPMENTS)
                    .ttl(TTL)
                    .build(),
                Session.builder()
                    .id(SESSION_ID2)
                    .name("Another Name")
                    .surname("Another Surname")
                    .email("anotheremail@domain.com")
                    .age(25)
                    .charClass("Barbarian")
                    .charSubclass("Berserker")
                    .charRace("Orc")
                    .charEquipments( Stream.of("Big Axe", "Big Axe").collect(Collectors.toList()))
                    .charSpells( Stream.of("Deadly scream", "Thousand years of pain").collect(Collectors.toList()))
                    .ttl(TTL)
                    .build()
                ).collect(Collectors.toList());
    } 
}
