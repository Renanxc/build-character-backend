package com.rakuten.buildcharacterbackend.infrastructure.client;

import java.util.function.Function;
import java.util.function.Supplier;

import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;

import org.springframework.http.ResponseEntity;

public enum RpgCharacterClientOptions implements RpgCharacterClientStrategy {

    classes(RpgCharacterClient::getClasses), 
    races(RpgCharacterClient::getRaces),
    equipments(RpgCharacterClient::getEquipments);

    private Function<RpgCharacterClient, ResponseEntity<RpgCharacterClientResponse>> strategy;

    RpgCharacterClientOptions(Function<RpgCharacterClient, ResponseEntity<RpgCharacterClientResponse>> strategy) {
        this.strategy = strategy;
    }

    public ResponseEntity<RpgCharacterClientResponse> get(RpgCharacterClient client, Supplier<String> searchValue) {
        return this.strategy.apply(client);
    }
}
