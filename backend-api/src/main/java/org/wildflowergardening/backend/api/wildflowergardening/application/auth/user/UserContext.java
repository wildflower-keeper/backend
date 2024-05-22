package org.wildflowergardening.backend.api.wildflowergardening.application.auth.user;

import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

public interface UserContext {

  UserRole getUserRole();

  Long getUserId();

  String getUsername();
}
