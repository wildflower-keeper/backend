package org.wildflowergardening.backend.core.wildflowergardening.application;

import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.UserContext;

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
