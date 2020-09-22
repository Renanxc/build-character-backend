package com.rakuten.buildcharacterbackend.api.v1;

import com.rakuten.buildcharacterbackend.domain.dto.request.SessionRequest;
import com.rakuten.buildcharacterbackend.domain.dto.response.SessionResponse;
import com.rakuten.buildcharacterbackend.service.SessionService;
import com.rakuten.buildcharacterbackend.testUtil.SessionRequestCreator;
import com.rakuten.buildcharacterbackend.testUtil.SessionResponseCreator;
import com.rakuten.buildcharacterbackend.util.TtlUtils;

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
public class SessionControllerTest {

	@InjectMocks
	private SessionController controller;
	@Mock
	private SessionService serviceMock;
	@Mock
	private TtlUtils utilsMock;

	@BeforeEach
	public void setUp() {
		String sessionID = SessionResponseCreator.creatValidResponse().getSessionID();
		when(serviceMock.create(any())).thenReturn(sessionID);
	}

	@Test
	@DisplayName("generateSessionByName returns a SessionID when successfull")
	public void generateSessionByName_returnsSessionID_whenSuccessfull() {
		//Given
			//Expected
		SessionResponse expectedResponse = SessionResponseCreator.creatValidResponse();
			//Resquest Params
		SessionRequest request = SessionRequestCreator.createValidRequest();
		Long validTTLRequest = 1000L;

		//When
		SessionResponse actualResponse = controller.generateSessionByName(request, validTTLRequest);

		//Then
		assertThat(actualResponse).isNotNull();
		assertThat(actualResponse).isEqualTo(expectedResponse);
		verify(utilsMock,times(1)).verifyRange(anyLong());
	}

}