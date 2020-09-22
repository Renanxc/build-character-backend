package com.rakuten.buildcharacterbackend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClient;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientStrategy;
import com.rakuten.buildcharacterbackend.util.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RpgCharacterService {

        RpgCharacterClient client;

        @Autowired
        RpgCharacterService(RpgCharacterClient client) {
                this.client = client;
        }

        public Map<String, List<RPGCharacterDataStructure>> getBuildOption(RpgCharacterClientStrategy[] requestList,
                        String searchValue) {
                List<RpgCharacterClientStrategy> clientStrategies = Arrays.asList(requestList);

                Map<String, CompletableFuture<List<RPGCharacterDataStructure>>> apiResponsesFutures = clientStrategies
                                .stream()
                                .collect(Collectors.toMap(strategy -> strategy.name(), strategy -> CompletableFuture
                                                .supplyAsync(() -> Mapper.convertToRPGCharacterDataStructure(
                                                                strategy.get(client, () -> searchValue)))));

                CompletableFuture.allOf(
                                apiResponsesFutures.values().toArray(new CompletableFuture[apiResponsesFutures.size()]))
                                .join();

                return apiResponsesFutures.keySet().stream().collect(Collectors.toMap(keyFuture -> keyFuture,
                                keyFuture -> apiResponsesFutures.get(keyFuture).join()));
        }
}
