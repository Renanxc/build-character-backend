package com.rakuten.buildcharacterbackend.domain.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionRequest {
	@NotEmpty(message = "{SessionRequest.name.required}")
	@Pattern(regexp = "^\\p{L}{1,12}\\s?\\p{L}{1,12}$" , message = "{SessionRequest.name.pattern}")
	String name;
	@Pattern(regexp = "^\\p{L}{1,12}(\\s?\\p{L}{0,12}){0,11}$", message = "{SessionRequest.surname.pattern}")
	String surname;
	@Pattern(regexp = "^\\p{L}{1,24}@(\\w{1,12}\\.?){1,5}$", message = "{SessionRequest.email.pattern}")
	String email;
}

