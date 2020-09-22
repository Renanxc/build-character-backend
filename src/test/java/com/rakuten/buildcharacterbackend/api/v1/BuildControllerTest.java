package com.rakuten.buildcharacterbackend.api.v1;

import java.util.List;

import com.rakuten.buildcharacterbackend.domain.dto.request.CharacterRequest;
import com.rakuten.buildcharacterbackend.domain.entity.Session;
import com.rakuten.buildcharacterbackend.service.SessionService;
import com.rakuten.buildcharacterbackend.testUtil.CharacterRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionResponseCreator;
import com.rakuten.buildcharacterbackend.util.TtlUtils;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BuildControllerTest {

	@InjectMocks
	private BuildController controller;
	@Mock
	private SessionService serviceMock;
	@Mock
	private TtlUtils utilsMock;

	@BeforeEach
	public void setUp() {
		Session sessionValid = SessionResponseCreator.creatValidSessionResponse();
		List<Session> sessionListValid = SessionResponseCreator.creatValidListSessionResponse();

		when(serviceMock.getAll()).thenReturn(sessionListValid);
		when(serviceMock.get(anyString())).thenReturn(sessionValid);
		when(serviceMock.set(any(Session.class))).thenReturn(sessionValid);
	}

	@Test
	@DisplayName("getBuildByToken returns a Session when successfull")
	public void getBuildByToken_returnsSession_whenSuccessfull() {
		//Given
			//Expected
		Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
			//Requests Params
		String token = SessionRequestCreator.createValidTokenRequest();
		Long validTTLRequest = 1000L;

		//When
		Session actualResponse = controller.getBuildByToken(token, validTTLRequest);

		//Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
		verify(serviceMock, times(1)).setTtl(anyString(), anyLong());
		verify(utilsMock, times(1)).verifyRange( anyLong());	
	}

	@Test
	@DisplayName("getBuildByToken not call ttlUtils and sessionService when ttl is not passed")
	public void getBuildByToken_notCallServices_whenTTLnotPassed() {
		//Given
			//Requests Params
		String token = SessionRequestCreator.createValidTokenRequest();

		//When
		controller.getBuildByToken(token,null);

		//Then
		verify(serviceMock, never()).setTtl(anyString(), anyLong());
		verify(utilsMock, never()).verifyRange( anyLong());
	}

	@Test
	@DisplayName("postBuildForToken creates a Session when successfull")
	public void postBuildForToken_createsSession_whenSuccessfull() {
		//Given
			//Expected
		Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
			//Requests Params
		String token = SessionRequestCreator.createValidTokenRequest();
		Long validTTLRequest = 1000L;
		CharacterRequest characterRequest = CharacterRequestCreator.createValidRequest();

		//When
		Session actualResponse = controller.postBuildForToken(token, validTTLRequest, characterRequest);

		//Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
		verify(serviceMock, times(1)).setTtl(anyString(), anyLong());
		verify(utilsMock, times(1)).verifyRange( anyLong());
	}

	@Test
	@DisplayName("postBuildForToken not call ttlUtils and sessionService when ttl is not passed")
	public void postBuildForToken_notCallServices_whenTTLnotPassed() {
		//Given
			//Requests Params
		CharacterRequest characterRequest = CharacterRequestCreator.createValidRequest();
		String token = SessionRequestCreator.createValidTokenRequest();

		//When
		controller.postBuildForToken(token,null,characterRequest);

		//Then
		verify(serviceMock, never()).setTtl(anyString(), anyLong());
		verify(utilsMock, never()).verifyRange( anyLong());
	}

	@Test
	@DisplayName("getAllBuilds returns a List of Session when successfull")
	public void getAllBuilds_returnsListSession_whenSuccessfull() {
		//Given
			//Expected
		List<Session> expectedResponse = SessionResponseCreator.creatValidListSessionResponse();

		//When
		List<Session> actualResponse = controller.getAllBuilds();

		//Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);		
	}

	@Test
	@DisplayName("deleteABuildByToken deletes a Session when successfull")
	public void deleteABuildByToken_deletesSession_whenSuccessfull() {
		//Given
			//Prepare
		ArgumentCaptor<Session> sessionToDelete = ArgumentCaptor.forClass(Session.class);
			//Expected
		Session expectedResponse = SessionResponseCreator.creatValidSessionResponse();
			//Requests Params
		String token = SessionRequestCreator.createValidTokenRequest();
		
		//When
		doNothing().when(serviceMock).delete(sessionToDelete.capture());
		controller.deleteABuildByToken(token);

		//Then
		assertThat(sessionToDelete.getValue()).isEqualTo(expectedResponse);
	}

	@Test
	@DisplayName("deleteAllBuilds deletes all Session when successfull")
	public void deleteAllBuilds_deletesAllSession_whenSuccessfull() {
		//Given
		
		//When
		doNothing().when(serviceMock).delete();
		controller.deleteAllBuilds();

		//Then
		verify(serviceMock, times(1)).delete();
	}

}
