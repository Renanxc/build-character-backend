package com.rakuten.buildcharacterbackend.infrastructure.client;

import javax.validation.Valid;

import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * RpgCharacterClient
 */

@FeignClient(name = "rpgCharacterAPI", url = "${client.rpgcharacter.url}")
public interface RpgCharacterClient {

    @RequestMapping(value = "/classes", method = RequestMethod.GET)
    ResponseEntity<RpgCharacterClientResponse> getClasses();

    @RequestMapping(value = "/races", method = RequestMethod.GET)
    ResponseEntity<RpgCharacterClientResponse> getRaces();

    @RequestMapping(value = "/equipment", method = RequestMethod.GET)
    ResponseEntity<RpgCharacterClientResponse> getEquipments();

    @RequestMapping(value = "/classes/{index}/subclasses", method = RequestMethod.GET)
    ResponseEntity<RpgCharacterClientResponse> getSubClasses(
            @Valid @PathVariable("index") String index);

    @RequestMapping(value = "/classes/{index}/spells", method = RequestMethod.GET)
    ResponseEntity<RpgCharacterClientResponse> getSpells(
            @Valid @PathVariable("index") String index);
}
