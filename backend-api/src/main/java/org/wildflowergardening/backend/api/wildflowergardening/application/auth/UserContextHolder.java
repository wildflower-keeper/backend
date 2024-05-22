package org.wildflowergardening.backend.api.wildflowergardening.application.auth;

import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.auth.user.UserContext;

@Component
public class UserContextHolder {

  public static final ThreadLocal<UserContext> contextHolder = new ThreadLocal<>();

  public void setUserContext(UserContext userContext) {
    contextHolder.set(userContext);
  }

  public UserContext getUserContext() {
    return contextHolder.get();
  }
}
