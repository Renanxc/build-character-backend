package com.rakuten.buildcharacterbackend.testUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;

import org.springframework.http.ResponseEntity;

public class RpgCharacterClientResponseCreator {
    public static Map<String, List<RPGCharacterDataStructure>> createValidResponse() {
        Map<String,List<RPGCharacterDataStructure>> validResponse = new HashMap<String,List<RPGCharacterDataStructure>>();
        validResponse.put(
            "classes", 
            getClassesClientMapped()
        );
        validResponse.put(
            "races", 
            getRacesClientMapped()
        );
        validResponse.put(
            "equipments", 
            getEquipmentsClientMapped()
        );
        return validResponse;
    } 

    public static Map<String, List<RPGCharacterDataStructure>> createValidPartialResponse() {
        Map<String, List<RPGCharacterDataStructure>> partialResponse = createValidResponse();
        partialResponse.remove("equipments");
        return partialResponse;
    } 

    public static Map<String, List<RPGCharacterDataStructure>> createValidWithSearchValueResponse() {
        Map<String,List<RPGCharacterDataStructure>> validResponse = new HashMap<String,List<RPGCharacterDataStructure>>();
        validResponse.put(
            "subclasses", 
            getSubClassesClientMapped()
        );
        validResponse.put(
            "spells", 
            getSpellsClientMapped()
        );
        return validResponse;
    }

    public static Map<String, List<RPGCharacterDataStructure>> createValidPartialWithSearchValueResponse() {
        Map<String, List<RPGCharacterDataStructure>> partialResponse = createValidWithSearchValueResponse();
        partialResponse.remove("subclasses");
        return partialResponse;
    }

    public static List<RPGCharacterDataStructure> createValidClassesClientMapped() {
        return getClassesClientMapped();
    } 

    public static ResponseEntity<RpgCharacterClientResponse> createValidClassesClientResponse() {
        RpgCharacterClientResponse validResponse = new RpgCharacterClientResponse(
            "1",
            getClassesClientMapped()
        );
        return ResponseEntity.ok(validResponse);
    } 

    public static ResponseEntity<RpgCharacterClientResponse> createValidRacesClientResponse() {
        RpgCharacterClientResponse validResponse = new RpgCharacterClientResponse(
            "1",
            getRacesClientMapped()
        );
        return ResponseEntity.ok(validResponse);
    } 

    public static ResponseEntity<RpgCharacterClientResponse> createValidEquipmentsClientResponse() {
        RpgCharacterClientResponse validResponse = new RpgCharacterClientResponse(
            "1",
            getEquipmentsClientMapped()
        );
        return ResponseEntity.ok(validResponse);
    } 

    public static ResponseEntity<RpgCharacterClientResponse> createValidSubClassesClientResponse() {
        RpgCharacterClientResponse validResponse = new RpgCharacterClientResponse(
            "1",
            getSubClassesClientMapped()
        );
        return ResponseEntity.ok(validResponse);
    } 

    public static ResponseEntity<RpgCharacterClientResponse> createValidSpellsClientResponse() {
        RpgCharacterClientResponse validResponse = new RpgCharacterClientResponse(
            "1",
            getSpellsClientMapped()
        );
        return ResponseEntity.ok(validResponse);
    } 

    //Lists

    private static List<RPGCharacterDataStructure> getClassesClientMapped() {
        return Arrays.asList(
            RPGCharacterDataStructure.builder().name("Warlock").index("walock").build(),
            RPGCharacterDataStructure.builder().name("Sage").index("sage").build(),
            RPGCharacterDataStructure.builder().name("Warrior").index("warrior").build()
        );
    }

    private static List<RPGCharacterDataStructure> getRacesClientMapped() {
        return Arrays.asList(
            RPGCharacterDataStructure.builder().name("Human").index("human").build(),
            RPGCharacterDataStructure.builder().name("Orc").index("orc").build(),
            RPGCharacterDataStructure.builder().name("Efl").index("elf").build()
        );
    }

    private static List<RPGCharacterDataStructure> getEquipmentsClientMapped() {
        return Arrays.asList(
            RPGCharacterDataStructure.builder().name("Short Sword").index("short_sword").build(),
            RPGCharacterDataStructure.builder().name("Long Sword").index("long_sword").build(),
            RPGCharacterDataStructure.builder().name("Spear").index("spear").build()
        );
    }

    private static List<RPGCharacterDataStructure> getSubClassesClientMapped() {
        return Arrays.asList(
            RPGCharacterDataStructure.builder().name("Warlock2").index("walock2").build(),
            RPGCharacterDataStructure.builder().name("Sage2").index("sage2").build(),
            RPGCharacterDataStructure.builder().name("Warrior2").index("warrior2").build()
        );
    }

    private static List<RPGCharacterDataStructure> getSpellsClientMapped() {
        return Arrays.asList(
            RPGCharacterDataStructure.builder().name("Fire Ball").index("fire_ball").build(),
            RPGCharacterDataStructure.builder().name("Ice Block").index("ice_block").build(),
            RPGCharacterDataStructure.builder().name("Shadow Hole of Nigthmare").index("shadow_hole_of_nightmare").build()
        );
    }


}
