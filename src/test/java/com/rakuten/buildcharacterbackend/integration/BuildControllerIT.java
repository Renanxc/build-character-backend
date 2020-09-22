package com.rakuten.buildcharacterbackend.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.rakuten.buildcharacterbackend.domain.dto.request.CharacterRequest;
import com.rakuten.buildcharacterbackend.domain.dto.response.error.ErrorResponse;
import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.infrastructure.repository.SessionRepository;
import com.rakuten.buildcharacterbackend.testUtil.CharacterRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionResponseCreator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BuildControllerIT {

	@Autowired
	@Qualifier(value = "restClientRoleUser")
	private TestRestTemplate restClientUser;

	@Autowired
	@Qualifier(value = "restClientRoleAdmin")
	private TestRestTemplate restClientAdmin;

	@LocalServerPort
	private int port;

	@MockBean
	private SessionRepository sessionMock;
	@MockBean
	@Qualifier(value = "PrimaryRedis")
	private RedisTemplate<String,String> redisMock;

	@Lazy
	@TestConfiguration
	static class Config {

		@Bean(name = "restClientRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port + "/api/v1/build")
                .basicAuthentication("test", "test");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "restClientRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port + "/api/v1/build")
                .basicAuthentication("admin", "admin");

            return new TestRestTemplate(restTemplateBuilder);
        }
	}

	@BeforeEach
	public void setUp() {
	}

	@Test
	@DisplayName("getBuildByToken returns a Session when successfull")
	public void getBuildByToken_returnsSession_whenSuccessfull() {
		//Given
			// Prepare
		Optional<Session> sessionResponseStub = Optional.of(SessionResponseCreator.creatValidSessionResponse());
			//Expected
		Session expectedResponse = sessionResponseStub.get();
			//Requests Params
		HttpHeaders header = new HttpHeaders();
		header.add("ID", SessionRequestCreator.createValidTokenRequest());
		header.add("X-Set-Ttl", "600");

		//When
		when(sessionMock.findById(anyString())).thenReturn(sessionResponseStub);
		when(redisMock.expire(anyString(), anyLong(),any(TimeUnit.class))).thenReturn(true);
		ResponseEntity<Session> actualResponse = restClientUser
				.exchange("/", HttpMethod.GET, createHttpEntity(null, header), Session.class);

		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isNotNull();
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
		verify(sessionMock, times(1)).findById(anyString());
		verify(redisMock, times(1)).expire(anyString(), anyLong(),any(TimeUnit.class));
	}

	@Test
	@DisplayName("getBuildByToken returns an ErrorResponse when the Id not sended")
	public void getBuildByToken_returnsErrorResponse_whenIdNotSended() {
		//Given
			// Prepare
		Optional<Session> sessionResponseStub = Optional.of(SessionResponseCreator.creatValidSessionResponse());
			//Requests Params
		HttpHeaders header = new HttpHeaders();
		header.add("X-Set-Ttl", "600");

		//When
		when(sessionMock.findById(anyString())).thenReturn(sessionResponseStub);
		when(redisMock.expire(anyString(), anyLong(),any(TimeUnit.class))).thenReturn(true);
		ResponseEntity<ErrorResponse> actualResponse = restClientUser
				.exchange("/", HttpMethod.GET, createHttpEntity(null, header), ErrorResponse.class);

		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actualResponse.getBody()).isNotNull();
		verify(sessionMock, never()).findById(anyString());
		verify(redisMock, never()).expire(anyString(), anyLong(),any(TimeUnit.class));
	}

	@Test
	@DisplayName("postBuildForToken creates a Session when successfull")
	public void postBuildForToken_createsSession_whenSuccessfull() {
		//Given
			// Prepare
		Optional<Session> repositorySessionResponseStub = Optional.of(SessionResponseCreator.createValidSessionFromRepository());
		Session sessionResponseStub = SessionResponseCreator.creatValidSessionResponse();
			//Expected
		Session expectedResponse = sessionResponseStub;
			//Requests Params
		HttpHeaders header = new HttpHeaders();
		header.add("ID", SessionRequestCreator.createValidTokenRequest());
		header.add("X-Set-Ttl", "600");
		CharacterRequest characterRequest = CharacterRequestCreator.createValidRequest();

		//When
		when(redisMock.expire(anyString(), anyLong(),any(TimeUnit.class))).thenReturn(true);
		when(sessionMock.findById(anyString())).thenReturn(repositorySessionResponseStub);
		when(sessionMock.save(any(Session.class))).thenReturn(sessionResponseStub);

		ResponseEntity<Session> actualResponse = restClientUser
				.exchange("/", HttpMethod.POST, createHttpEntity(characterRequest, header), Session.class);

		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(actualResponse.getBody()).isNotNull();
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);
		verify(sessionMock, times(1)).save(any(Session.class));
		verify(redisMock, times(1)).expire(anyString(), anyLong(),any(TimeUnit.class));
	}

	@Test
	@DisplayName("postBuildForToken returns an ErrorResponse when the Id is not sended")
	public void postBuildForToken_returnsErrorResponse_whenIdNotSended() {
		//Given
			// Prepare
		Session sessionResponseStub = SessionResponseCreator.creatValidSessionResponse();
			//Requests Params
		HttpHeaders header = new HttpHeaders();
		header.add("X-Set-Ttl", "600");
		CharacterRequest characterRequest = CharacterRequestCreator.createValidRequest();

		//When
		when(sessionMock.save(any(Session.class))).thenReturn(sessionResponseStub);
		ResponseEntity<ErrorResponse> actualResponse = restClientUser
				.exchange("/", HttpMethod.POST, createHttpEntity(characterRequest, header), ErrorResponse.class);
		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actualResponse.getBody()).isNotNull();
		verify(sessionMock, never()).save(any(Session.class));
		verify(redisMock, never()).expire(anyString(), anyLong(),any(TimeUnit.class));
	}

	@Test
	@DisplayName("getAllBuilds returns a List of Session when successfull")
	public void getAllBuilds_returnsListSession_whenSuccessfull() {
		//Given
			//Prepare
		List<Session> sessionResponseStub = SessionResponseCreator.creatValidListSessionResponse();
			//Expected
		List<Session> expectedResponse = SessionResponseCreator.creatValidListSessionResponse();

		//When
		when(sessionMock.findAll()).thenReturn(sessionResponseStub);

		ResponseEntity<List<Session>> actualResponse = restClientAdmin
				.exchange("/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Session>>(){});
		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isNotNull();
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse);	
		verify(sessionMock, times(1)).findAll();
	}

	@Test
	@DisplayName("deleteABuildByToken deletes a Session when successfull")
	public void deleteABuildByToken_deletesSession_whenSuccessfull() {
		//Given
			//Prepare
		Optional<Session> sessionResponseStub = Optional.of(SessionResponseCreator.creatValidSessionResponse());
		ArgumentCaptor<String> sessionToGet = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Session> sessionToDelete = ArgumentCaptor.forClass(Session.class);
			//Requests Params
		HttpHeaders header = new HttpHeaders();
		String token = SessionRequestCreator.createValidTokenRequest();
		header.add("ID", token);
		
		//When
		when(sessionMock.findById(sessionToGet.capture())).thenReturn(sessionResponseStub);
		doNothing().when(sessionMock).delete(sessionToDelete.capture());
		
		ResponseEntity<Void> actualResponse = restClientAdmin
				.exchange("/", HttpMethod.DELETE, createHttpEntity(null, header), Void.class);

		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(sessionToGet.getValue()).isEqualTo(token);
		assertThat(sessionToDelete.getValue()).isEqualTo(sessionResponseStub.get());
		verify(sessionMock, times(1)).delete(any(Session.class));
	}

	@Test
	@DisplayName("deleteABuildByToken returns an ErrorResponse when Id is not sended")
	public void deleteABuildByToken_returnsErrorResponse_whenIdNotSended() {
		//Given
		
		//When
		doNothing().when(sessionMock).delete(any());
		
		ResponseEntity<Void> actualResponse = restClientAdmin
				.exchange("/", HttpMethod.DELETE, null, Void.class);

		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		verify(sessionMock, never()).delete(any(Session.class));
	}

	@Test
	@DisplayName("deleteAllBuilds deletes all Session when successfull")
	public void deleteAllBuilds_deletesAllSession_whenSuccessfull() {
		//Given
		
		//When
		doNothing().when(sessionMock).deleteAll();

		ResponseEntity<Void> actualResponse = restClientAdmin
				.exchange("/all", HttpMethod.DELETE, null, Void.class);
		//Then
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		verify(sessionMock, times(1)).deleteAll();
	}

	private <A> HttpEntity<A> createHttpEntity(A request, HttpHeaders header) {
		HttpHeaders createdHeaders = createHeader();
		createdHeaders.addAll(header);
		return new HttpEntity<>(request, createdHeaders);
	}

    private HttpHeaders createHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
