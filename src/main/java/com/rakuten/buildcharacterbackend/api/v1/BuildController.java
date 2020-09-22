package com.rakuten.buildcharacterbackend.api.v1;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.rakuten.buildcharacterbackend.domain.dto.request.CharacterRequest;
import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.service.SessionService;
import com.rakuten.buildcharacterbackend.util.Mapper;
import com.rakuten.buildcharacterbackend.util.TtlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/build")
public class BuildController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private TtlUtils ttlUtils;

	@ApiOperation(value = "Get a Build by a given token..", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),

	})
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public Session getBuildByToken(@RequestHeader(value = "ID", required = true) final String token,
			@RequestHeader(value = "X-Set-Ttl", required = false) Long ttl) {
		configureTTL(token,ttl);
		return sessionService.get(token);
	}

	@ApiOperation(value = "Post a Build for a given token.", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),

	})
	@PostMapping
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public Session postBuildForToken(@RequestHeader(value = "ID", required = true) final String token,
			@RequestHeader(value = "X-Set-Ttl", required = false) Long ttl,
			@Valid @RequestBody final CharacterRequest req) {
		configureTTL(token,ttl);
		return sessionService.set( Mapper.convertCharacterDTOtoCurrentEntitySession(req, sessionService.get(token)));
	}

	@ApiOperation(value = "Get all Builds.", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),

	})
	@GetMapping("all")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Session> getAllBuilds() {
		return sessionService.getAll();
	}

	@ApiOperation(value = "Delete a Build for a given token.", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),

	})
	@DeleteMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public void deleteABuildByToken(@RequestHeader(value = "ID", required = true) final String token ) {
		sessionService.delete(sessionService.get(token));
	}

	@ApiOperation(value = "Delete all Builds.", notes = ".")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),

	})
	@DeleteMapping("all")
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public void deleteAllBuilds() {
		sessionService.delete();
	}

	private void configureTTL(String token, Long ttl) {
		if (Optional.ofNullable(ttl).isPresent()) {
			ttlUtils.verifyRange(ttl);
			log.info(String.format("Session %s is refreshing your TTL for %d",token,ttl));
			sessionService.setTtl(token, ttl);
		}
    }
}
