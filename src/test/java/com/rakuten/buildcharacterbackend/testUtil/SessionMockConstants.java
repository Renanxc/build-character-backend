package com.rakuten.buildcharacterbackend.testUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SessionMockConstants {
    final static String SESSION_ID = "ca23b449-7b5c-4381-baba-e390da93ca50";
    final static String SESSION_ID2 = "ca23b449-7b5c-4381-baby-e390da93ca50";

    static final String NAME = "Test Name";
    static final String SURNAME = "A Big Surname To Persist";
    static final String EMAIL = "thisisnotanemail@anemail.rakuten.com";
    static final long TTL = 600L;
    
    static final int AGE = 25;
    static final String CHAR_CLASS = "Warrior";
    static final String CHAR_SUBCLASS = "Warrior2";
    static final String CHAR_RACE = "Human";
    static final List<String> CHAR_EQUIPMENTS = Stream.of("Short Sword", "Long Sword").collect(Collectors.toList());
}
