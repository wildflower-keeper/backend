package org.wildflowergardening.backend.core.wildflowergardening.application;

import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.SessionContext;

@Component
public class SessionContextHolder {

  public static final ThreadLocal<SessionContext> contextHolder = new ThreadLocal<>();

  public void setSessionContext(SessionContext sessionContext) {
    contextHolder.set(sessionContext);
  }

  public SessionContext getSessionContext() {
    return contextHolder.get();
  }
}
