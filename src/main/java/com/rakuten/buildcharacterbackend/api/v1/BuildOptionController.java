package com.rakuten.buildcharacterbackend.api.v1;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.rakuten.buildcharacterbackend.domain.dto.response.RPGCharacterDataStructure;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptions;
import com.rakuten.buildcharacterbackend.infrastructure.client.RpgCharacterClientOptionsToClasses;
import com.rakuten.buildcharacterbackend.service.RpgCharacterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/v1/build-option")
public class BuildOptionController {

	@Autowired
	private RpgCharacterService rpgCharacterService;

	@ApiOperation(value = "Get a Build Option.", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),
	})
	@GetMapping()
	@ResponseStatus(code = HttpStatus.OK)
	public Map<String, List<RPGCharacterDataStructure>> getBuildOption(
			@RequestParam(name = "options", required = false) RpgCharacterClientOptions[] opt) {
		return rpgCharacterService.getBuildOption(
			Optional.ofNullable(opt).orElse(RpgCharacterClientOptions.values()), null);
	}

	@ApiOperation(value = "Get a Build Option by a given class.", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),
	})
	@GetMapping(value = "/{buildClass}")
	@ResponseStatus(code = HttpStatus.OK)
	public Map<String, List<RPGCharacterDataStructure>> getBuildOptionByBuildClass(
			@RequestParam(name = "options", required = false) RpgCharacterClientOptionsToClasses[] opt,
			@PathVariable(value = "buildClass", required = true) String buildClass) {
		return rpgCharacterService.getBuildOption(
				Optional.ofNullable(opt).orElse(RpgCharacterClientOptionsToClasses.values()), buildClass);
	}

}