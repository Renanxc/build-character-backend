package com.rakuten.buildcharacterbackend.api.v1;

import java.util.List;
import java.util.Map;

import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptions;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptionsToClasses;
import com.rakuten.buildcharacterbackend.service.RpgCharacterService;
import com.rakuten.buildcharacterbackend.testUtil.OptionsStrategyRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.RpgCharacterClientResponseCreator;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BuildOptionControllerTest {
	@InjectMocks
	private BuildOptionController controller;
	@Mock
	private RpgCharacterService serviceMock;

	@BeforeEach
	public void setUp() {
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
		RpgCharacterClientOptions[] requestWithNoOptions = null;

		// When
		when(serviceMock.getBuildOption(any(RpgCharacterClientOptions[].class),any())).thenReturn(expectedResponse);
		Map<String, List<RPGCharacterDataStructure>> actualResponseWithAllOptions = controller
				.getBuildOption(requestWithAllOptions);
		Map<String, List<RPGCharacterDataStructure>> actualResponseWithNoOptions = controller
				.getBuildOption(requestWithNoOptions);

		// Then
			// WithAllOptions
		assertThat(actualResponseWithAllOptions).isNotNull();
		assertThat(actualResponseWithAllOptions.values()).isNotEmpty();
			// WithNoOptions
		assertThat(actualResponseWithNoOptions).isNotNull();
		assertThat(actualResponseWithNoOptions.values()).isNotEmpty();
			// Compare
		assertThat(actualResponseWithAllOptions).isEqualTo(actualResponseWithNoOptions).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("getBuildOption returns a Map of selected RpgCharacterClientOptions and List of RPGCharacterDataStructure when just some options are sended")
	public void getBuildOption_returnsMapSelectedRpgCharacterClientOptionsListOfRPGCharacterDataStructure_whenJustSomeOptionsSended() {
		// Given
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator.createValidPartialResponse();
			// Requests Params
		RpgCharacterClientOptions[] request = OptionsStrategyRequestCreator.createValidPartialRequest();
		
		// When
		when(serviceMock.getBuildOption(same(request),any())).thenReturn(expectedResponse);
		Map<String, List<RPGCharacterDataStructure>> actualResponse = controller.getBuildOption(request);

		// Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse.values()).isNotEmpty();
		assertThat(actualResponse).isEqualTo(expectedResponse);
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
		RpgCharacterClientOptionsToClasses[] requestWithNoOptions = null;
		String searchValue = "random_class";

		// When
		when(serviceMock.getBuildOption(any(RpgCharacterClientOptionsToClasses[].class), anyString())).thenReturn(expectedResponse);
		Map<String, List<RPGCharacterDataStructure>> actualResponseWithAllOptions = controller.getBuildOptionByBuildClass(requestWithAllOptions,searchValue);
		Map<String, List<RPGCharacterDataStructure>> actualResponseWithNoOptions = controller.getBuildOptionByBuildClass(requestWithNoOptions,searchValue);

		// Then
			// WithAllOptions
		assertThat(actualResponseWithAllOptions).isNotNull();
		assertThat(actualResponseWithAllOptions.values()).isNotEmpty();
			// WithNoOptions
		assertThat(actualResponseWithNoOptions).isNotNull();
		assertThat(actualResponseWithNoOptions.values()).isNotEmpty();
			// Compare
		assertThat(actualResponseWithAllOptions).isEqualTo(actualResponseWithNoOptions).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("getBuildOptionByBuildClass returns a Map of selected RpgCharacterClientOptionsToClasses and List of RPGCharacterDataStructure when just some options are sended")
	public void getBuildOptionByBuildClass_returnsMapSelectedRpgCharacterClientOptionsToClassesListOfRPGCharacterDataStructure_whenJustSomeOptionsSended() {
		// Given
			// Expected
		Map<String, List<RPGCharacterDataStructure>> expectedResponse = RpgCharacterClientResponseCreator.createValidPartialWithSearchValueResponse();
			// Requests Params
		RpgCharacterClientOptionsToClasses[] request = OptionsStrategyRequestCreator.createValidPartialWithSearchValueRequest();
		String searchValue = "random_class";

		// When
		when(serviceMock.getBuildOption(same(request),anyString())).thenReturn(expectedResponse);
		Map<String, List<RPGCharacterDataStructure>> actualResponse = controller.getBuildOptionByBuildClass(request,searchValue);

		// Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse.values()).isNotEmpty();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}
}