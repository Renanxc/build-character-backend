package com.rakuten.buildcharacterbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.infrastructure.repository.SessionRepository;
import com.rakuten.buildcharacterbackend.testUtil.SessionRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionResponseCreator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class SessionServiceTest {

	@InjectMocks
	private SessionService service;

	@Mock
    SessionRepository repo;
	@Mock
    RedisTemplate<String, String> redis;

	@Test
	@DisplayName("create returns a inserted SessionID when a valid Session is sended")
	public void create_returnsInsertedSessionID_whenValidSession() {
        // Given
            // Prepare
        Session sessionResponseStub = SessionResponseCreator.creatValidSessionResponse();
			// Expected
		String expectedResponse = sessionResponseStub.getId();
			// Requests Params
		Session request = SessionResponseCreator.creatValidSessionResponse();

		// When
		when(repo.save(any(Session.class))).thenReturn(sessionResponseStub);
		String actualResponse = service.create(request);

		// Then
			// WithAllOptions
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("get returns a Session when a valid SessionID is sended")
	public void get_returnsSession_whenValidSessionID() {
        // Given
            // Prepare
        Optional<Session> sessionResponseStub = Optional.of(SessionResponseCreator.creatValidSessionResponse());
			// Expected
		Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
			// Requests Params
		String request = SessionResponseCreator.creatValidResponse().getSessionID();

		// When
		when(repo.findById(anyString())).thenReturn(sessionResponseStub);
		Session actualResponse = service.get(request);

		// Then
			// WithAllOptions
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("set returns a inserted Session when a valid Session is sended")
	public void set_returnsInsertedSession_whenValidSession() {
        // Given
            // Prepare
        Session sessionResponseStub = SessionResponseCreator.creatValidSessionResponse();
			// Expected
		Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
			// Requests Params
        Session request = SessionResponseCreator.creatValidSessionResponse();

		// When
		when(repo.save(any(Session.class))).thenReturn(sessionResponseStub);
		Session actualResponse = service.set(request);

		// Then
			// WithAllOptions
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}
    
    @Test
	@DisplayName("delete should call delete Repository when a valid Session is sended")
	public void delete_shouldCallDeleteRepository_whenValidSession() {
        // Given
            // Prepare
        ArgumentCaptor<Session> sessionToDelete = ArgumentCaptor.forClass(Session.class);
            // Expected
        Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
			// Requests Params
        Session request = SessionResponseCreator.creatValidSessionResponse();

		// When
		doNothing().when(repo).delete(sessionToDelete.capture());
		service.delete(request);

        // Then
        assertThat(sessionToDelete.getValue()).isEqualTo(expectedResponse);
        verify(repo,times(1)).delete(any(Session.class));
	}
    
    @Test
	@DisplayName("delete should call deleteAll Repository when no Session is sended")
	public void delete_shouldCallDeleteAllRepository_whenNoSession() {
        // Given

		// When
		doNothing().when(repo).deleteAll();
		service.delete();

        // Then
        verify(repo,times(1)).deleteAll();
	}

    @Test
	@DisplayName("getAll returns a SessionList when no Session is sended")
	public void getAll_returnsSessionList_whenNoSession() {
        // Given
            // Prepare
        Iterable<Session> sessionResponseStub = SessionResponseCreator.creatValidListSessionResponse();
			// Expected
        List<Session> expectedResponse = new ArrayList<>();
        sessionResponseStub.forEach(expectedResponse::add);

		// When
		when(repo.findAll()).thenReturn(sessionResponseStub);
		List<Session> actualResponse = service.getAll();

		// Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
	}

    @Test
	@DisplayName("setTtl should call expire Repository when Valid SessionID and ttl is sended")
	public void setTtl_shouldCallExpireRepository_whenValidSessionIDandTTLisSended() {
        // Given
            // Prepare
        ArgumentCaptor<String> keyToSet = ArgumentCaptor.forClass(String.class);
            // Expected
        String expectedResponse = SessionResponseCreator.creatValidHashKeyResponse();
            // Request Params
        String sessionID = SessionRequestCreator.createValidTokenRequest();
        Long ttl = 600L;

		// When
        when(redis.expire(keyToSet.capture(),anyLong(),any(TimeUnit.class))).thenReturn(true);
		service.setTtl(sessionID,ttl);

		// Then
        verify(redis,times(1)).expire(anyString(),anyLong(),any(TimeUnit.class));
        assertThat(keyToSet.getValue()).isEqualTo(expectedResponse);
	}
}
