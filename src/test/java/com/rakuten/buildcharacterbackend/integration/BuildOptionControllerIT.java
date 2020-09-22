package com.rakuten.buildcharacterbackend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClient;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptions;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptionsToClasses;
import com.rakuten.buildcharacterbackend.infrastructure.client.Response.RpgCharacterClientResponse;
import com.rakuten.buildcharacterbackend.testUtil.OptionsStrategyRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.RpgCharacterClientResponseCreator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BuildOptionControllerIT {

	@Autowired
	@Qualifier(value = "restClientRoleUser")
	private TestRestTemplate restClientUser;

	@LocalServerPort
	private int port;

	@MockBean
	private RpgCharacterClient clientMock;

	@Lazy
	@TestConfiguration
	static class Config {

		@Bean(name = "restClientRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port + "/api/v1/build-option")
                .basicAuthentication("test", "test");

            return new TestRestTemplate(restTemplateBuilder);
        }
	}

	@BeforeEach
	public void setUp() {
		ResponseEntity<RpgCharacterClientResponse> classesClientResponseStub = RpgCharacterClientResponseCreator.createValidClassesClientResponse();
		ResponseEntity<RpgCharacterClientResponse> racesClientResponseStub = RpgCharacterClientResponseCreator.createValidRacesClientResponse();
		ResponseEntity<RpgCharacterClientResponse> equipmentsClientResponseStub = RpgCharacterClientResponseCreator.createValidEquipmentsClientResponse();
		ResponseEntity<RpgCharacterClientResponse> subClassesClientResponseStub = RpgCharacterClientResponseCreator.createValidSubClassesClientResponse();
		ResponseEntity<RpgCharacterClientResponse> spellsClientResponseStub = RpgCharacterClientResponseCreator.createValidSpellsClientResponse();			


		when(clientMock.getClasses()).thenReturn(classesClientResponseStub);
		when(clientMock.getRaces()).thenReturn(racesClientResponseStub);
		when(clientMock.getEquipments()).thenReturn(equipmentsClientResponseStub);
		when(clientMock.getSubClasses(anyString())).thenReturn(subClassesClientResponseStub);
		when(clientMock.getSpells(anyString())).thenReturn(spellsClientResponseStub);
	}

	@Test
	@DisplayName("getBuildOption returns a Map of all RpgCharacterClientOptions and List of RPGCharacterDataStructure when all options or no one are sended")
	public void getBuildOption_returnsMapAllRpgCharacterClientOptionsAndListRPGCharacterDataStructure_whenAllOptionsOrNoOneSended() {
		// Given
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator
				.createValidResponse();
			// Requests Params
		RpgCharacterClientOptions[] requestWithAllOptions = OptionsStrategyRequestCreator.createValidRequest();
		String options = Arrays.stream(requestWithAllOptions).map(RpgCharacterClientOptions::toString).collect(Collectors.joining(","));

		// When
		ResponseEntity<Map<String, List<RPGCharacterDataStructure>>> actualResponseWithAllOptions = restClientUser
		.exchange("/?options=" + options, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<RPGCharacterDataStructure>>>(){	
		});
		ResponseEntity<Map<String, List<RPGCharacterDataStructure>>> actualResponseWithNoOptions = restClientUser
		.exchange("/", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<RPGCharacterDataStructure>>>(){	
		});


		// Then
			// WithAllOptions
		assertThat(actualResponseWithAllOptions.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponseWithAllOptions.getBody()).isNotNull();
		// WithNoOptions
		assertThat(actualResponseWithNoOptions.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponseWithNoOptions.getBody()).isNotNull();
			// Compare
		assertThat(actualResponseWithAllOptions.getBody())
			.isEqualTo(actualResponseWithNoOptions.getBody())
				.isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("getBuildOption returns a Map of selected RpgCharacterClientOptions and List of RPGCharacterDataStructure when just some options are sended")
	public void getBuildOption_returnsMapSelectedRpgCharacterClientOptionsListOfRPGCharacterDataStructure_whenJustSomeOptionsSended() {
		// Given
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator.createValidPartialResponse();
			// Requests Params
		RpgCharacterClientOptions[] request = OptionsStrategyRequestCreator.createValidPartialRequest();
		String options = Arrays.stream(request).map(RpgCharacterClientOptions::toString).collect(Collectors.joining(","));
		
		// When		
		ResponseEntity<Map<String, List<RPGCharacterDataStructure>>> actualResponse = restClientUser
		.exchange("/?options=" + options, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<RPGCharacterDataStructure>>>(){	
		});

		// Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isNotNull();
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("getBuildOptionByBuildClass returns a Map of all RpgCharacterClientOptionsToClasses and List of RPGCharacterDataStructure when all options or no one are sended")
	public void getBuildOptionByBuildClass_returnsMapAllRpgCharacterClientOptionsToClassesAndListRPGCharacterDataStructure_whenAllOptionsOrNoOneSended() {
		// Given
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator
				.createValidWithSearchValueResponse();
			// Requests Params
		RpgCharacterClientOptionsToClasses[] requestWithAllOptions = OptionsStrategyRequestCreator.createValidWithSearchValueRequest();
		String options = Arrays.stream(requestWithAllOptions).map(RpgCharacterClientOptionsToClasses::toString).collect(Collectors.joining(","));
		String searchValue = "random_class";

		// When
		ResponseEntity<Map<String, List<RPGCharacterDataStructure>>> actualResponseWithAllOptions = restClientUser
		.exchange("/" + searchValue + "?options=" + options, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<RPGCharacterDataStructure>>>(){	
		});
		ResponseEntity<Map<String, List<RPGCharacterDataStructure>>> actualResponseWithNoOptions = restClientUser
		.exchange("/" + searchValue, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<RPGCharacterDataStructure>>>(){
		});

		// Then
			// WithAllOptions
		assertThat(actualResponseWithAllOptions.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponseWithAllOptions.getBody()).isNotNull();
			// WithNoOptions
		assertThat(actualResponseWithNoOptions.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponseWithNoOptions.getBody()).isNotNull();
			// Compare
		assertThat(actualResponseWithAllOptions.getBody())
			.isEqualTo(actualResponseWithNoOptions.getBody())
				.isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("getBuildOptionByBuildClass returns a Map of selected RpgCharacterClientOptionsToClasses and List of RPGCharacterDataStructure when just some options are sended")
	public void getBuildOptionByBuildClass_returnsMapSelectedRpgCharacterClientOptionsToClassesListOfRPGCharacterDataStructure_whenJustSomeOptionsSended() {
		// Given
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator.createValidPartialWithSearchValueResponse();
			// Requests Params
		RpgCharacterClientOptionsToClasses[] request = OptionsStrategyRequestCreator.createValidPartialWithSearchValueRequest();
		String options = Arrays.stream(request).map(RpgCharacterClientOptionsToClasses::toString).collect(Collectors.joining(","));
		String searchValue = "random_class";

		// When
		ResponseEntity<Map<String, List<RPGCharacterDataStructure>>> actualResponse = restClientUser
		.exchange("/" + searchValue + "?options=" + options, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, List<RPGCharacterDataStructure>>>(){	
		});
		// Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isNotNull();
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
	}
}