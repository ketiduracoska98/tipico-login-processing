package org.example.loginprocessingservice.service.tracklogin;

import org.example.loginprocessingservice.model.LoginEvent;
import org.example.loginprocessingservice.model.LoginResult;

public interface TrackLoginService {

	LoginResult trackLogin(LoginEvent loginEvent);
}
