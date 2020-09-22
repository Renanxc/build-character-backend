package com.rakuten.buildcharacterbackend.infrastructure.client;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;

import org.springframework.http.ResponseEntity;

public enum RpgCharacterClientOptionsToClasses implements RpgCharacterClientStrategy {

    subclasses((client, searchValue) -> client.getSubClasses(searchValue.get())),
    spells((client, searchValue) -> client.getSpells(searchValue.get()));

    private BiFunction<RpgCharacterClient, Supplier<String>, ResponseEntity<RpgCharacterClientResponse>> strategy;

    RpgCharacterClientOptionsToClasses(
            BiFunction<RpgCharacterClient, Supplier<String>, ResponseEntity<RpgCharacterClientResponse>> strategy) {
        this.strategy = strategy;
    }

    public ResponseEntity<RpgCharacterClientResponse> get(RpgCharacterClient client, Supplier<String> searchValue) {
        return this.strategy.apply(client, searchValue);
    }

}