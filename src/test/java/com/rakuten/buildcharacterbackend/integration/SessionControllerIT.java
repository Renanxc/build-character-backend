package com.rakuten.buildcharacterbackend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rakuten.buildcharacterbackend.domain.dto.request.SessionRequest;
import com.rakuten.buildcharacterbackend.domain.dto.response.SessionResponse;
import com.rakuten.buildcharacterbackend.domain.dto.response.error.ErrorResponse;
import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.infrastructure.repository.SessionRepository;
import com.rakuten.buildcharacterbackend.testUtil.SessionRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionResponseCreator;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SessionControllerIT {

	@Autowired
	@Qualifier(value = "restClientRoleUser")
	private TestRestTemplate restClientUser;

	@LocalServerPort
	private int port;

	@MockBean
	private SessionRepository sessionMock;
	@MockBean
	private RedisTemplate<String,String> redisMock;

	@Lazy
	@TestConfiguration
	static class Config {

		@Bean(name = "restClientRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port + "/api/v1/session")
                .basicAuthentication("test", "test");

            return new TestRestTemplate(restTemplateBuilder);
        }
	}

	
	@BeforeEach
	public void setUp() {
		Session sessionStub = SessionResponseCreator.createValidSessionFromRepository();
		when(sessionMock.save(any(Session.class))).thenReturn(sessionStub);
	}

	@Test
	@DisplayName("generateSessionByName returns a SessionID when successfull")
	public void generateSessionByName_returnsSessionID_whenSuccessfull() {
		//Given
			//Expected
		SessionResponse expectedResponse = SessionResponseCreator.creatValidResponse();
			//Resquest Params
		SessionRequest request = SessionRequestCreator.createValidRequest();
		Pair<String,String> header = Pair.of("X-Set-Ttl", "600");

		SessionResponse actualResponse = restClientUser
				.exchange("/", HttpMethod.POST, createHttpEntity(request, header), SessionResponse.class).getBody();

		//Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
		verify(sessionMock,times(1)).save(any(Session.class));
	}

	@Test
	@DisplayName("generateSessionByName returns an ErrorResponse when a property is invalid")
	public void generateSessionByName_returnsErrorResponse_whenPropertyInvalid() {
		//Given
			//Resquest Params
		SessionRequest request = SessionRequestCreator.createAnInvalidRequest();

		//When
		ResponseEntity<ErrorResponse> errorResponse = restClientUser
				.exchange("/", HttpMethod.POST, createHttpEntity(request), ErrorResponse.class);

		//Then
		assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		verify(sessionMock,never()).save(any(Session.class));
	}

	private <A> HttpEntity<A> createHttpEntity(A request) {
        return new HttpEntity<>(request, createHeader());
    }

	private <A> HttpEntity<A> createHttpEntity(A request, Pair<String,String> header) {
		HttpHeaders createdHeaders = createHeader();
		createdHeaders.add(header.getFirst(),header.getSecond());
		return new HttpEntity<>(request, createdHeaders);
	}

    private HttpHeaders createHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}