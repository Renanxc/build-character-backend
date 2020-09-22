package com.rakuten.buildcharacterbackend.api.v1;

import javax.validation.Valid;

import com.rakuten.buildcharacterbackend.domain.dto.request.SessionRequest;
import com.rakuten.buildcharacterbackend.domain.dto.response.SessionResponse;
import com.rakuten.buildcharacterbackend.service.SessionService;
import com.rakuten.buildcharacterbackend.util.Mapper;
import com.rakuten.buildcharacterbackend.util.TtlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@RestController
@RequestMapping("api/v1/session")
public class SessionController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private TtlUtils ttlUtils;

	@ApiOperation(value = "Generate a Session Token.", notes = "The Session Token will be the responsible to keep the communication between the client and the API.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success."),
			@ApiResponse(code = 500, message = "Error."),

	})
	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	public SessionResponse generateSessionByName(
			@Valid @RequestBody(required = true) SessionRequest session,
			@RequestHeader(value = "X-Set-Ttl", required = false) Long ttl) {
		ttlUtils.verifyRange(ttl);
		return new SessionResponse(sessionService.create(Mapper.convertSessionDTOtoEntitySession(session, ttl)));
	}
}