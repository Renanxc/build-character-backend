package com.rakuten.buildcharacterbackend.infrastructure.client.Response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RpgCharacterClientResponse {
    String count;
    List<RPGCharacterDataStructure> results;
}
