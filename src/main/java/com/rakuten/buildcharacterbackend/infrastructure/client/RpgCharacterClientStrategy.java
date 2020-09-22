package com.rakuten.buildcharacterbackend.infrastructure.client;

import java.util.function.Supplier;

import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;

import org.springframework.http.ResponseEntity;

public interface RpgCharacterClientStrategy {

    public ResponseEntity<RpgCharacterClientResponse> get(RpgCharacterClient client, Supplier<String> searchValue);

	public String name();
}
