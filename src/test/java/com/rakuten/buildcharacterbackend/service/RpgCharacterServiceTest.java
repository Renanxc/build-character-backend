package com.rakuten.buildcharacterbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClient;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptions;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptionsToClasses;
import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;
import com.rakuten.buildcharacterbackend.testUtil.OptionsStrategyRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.RpgCharacterClientResponseCreator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class RpgCharacterServiceTest {

	@InjectMocks
	private RpgCharacterService service;

	@Mock
	RpgCharacterClient client;

	@Test
	@DisplayName("getBuildOption returns a Map of RPGCharacterDataStructure of RpgCharacterClientOptions when no search value is sended")
	public void getBuildOption_returnsMapRPGCharacterDataStructure_whenNoSearchValueSended() {
		// Given
			// Client Stubs
		ResponseEntity<RpgCharacterClientResponse> classesClientResponseStub = RpgCharacterClientResponseCreator.createValidClassesClientResponse();
		ResponseEntity<RpgCharacterClientResponse> racesClientResponseStub = RpgCharacterClientResponseCreator.createValidRacesClientResponse();
		ResponseEntity<RpgCharacterClientResponse> equipmentsClientResponseStub = RpgCharacterClientResponseCreator.createValidEquipmentsClientResponse();
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator.createValidResponse();
			// Requests Params
		RpgCharacterClientOptions[] request = OptionsStrategyRequestCreator.createValidRequest();

		// When
		when(client.getClasses()).thenReturn(classesClientResponseStub);
		when(client.getRaces()).thenReturn(racesClientResponseStub);
		when(client.getEquipments()).thenReturn(equipmentsClientResponseStub);
		Map<String, List<RPGCharacterDataStructure>> actualResponse = service.getBuildOption(request,null);


		// Then
			// WithAllOptions
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse.values()).isNotEmpty();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("getBuildOption returns a Map of RPGCharacterDataStructure of RpgCharacterClientOptionsToClasses when search value is sended")
	public void getBuildOption_returnsMapRPGCharacterDataStructure_whenSearchValueSended() {
		// Given
			// Client Stubs
		ResponseEntity<RpgCharacterClientResponse> subClassesClientResponseStub = RpgCharacterClientResponseCreator.createValidSubClassesClientResponse();
		ResponseEntity<RpgCharacterClientResponse> spellsClientResponseStub = RpgCharacterClientResponseCreator.createValidSpellsClientResponse();			
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator.createValidWithSearchValueResponse();
			// Requests Params
		RpgCharacterClientOptionsToClasses[] request = OptionsStrategyRequestCreator.createValidWithSearchValueRequest();
		String searchValue = "random_class";

		// When
		when(client.getSubClasses(anyString())).thenReturn(subClassesClientResponseStub);
		when(client.getSpells(anyString())).thenReturn(spellsClientResponseStub);
		Map<String, List<RPGCharacterDataStructure>> actualResponse = service.getBuildOption(request,searchValue);


		// Then
			// WithAllOptions
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse.values()).isNotEmpty();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

}
