package org.wildflowergardening.backend.core.wildflowergardening.domain.auth;

public interface UserContext {

  UserRole getUserRole();

  Long getUserId();

  String getUsername();
}
