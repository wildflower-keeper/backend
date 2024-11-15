package org.wildflowergardening.backend.api.wildflowergardening.application.auth.user;

import lombok.Builder;
import lombok.Getter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Getter
@Builder
public class ShelterAdminContext implements UserContext {

    private Long userId;
    private Long shelterId;
    private String userName;
    private UserRole role;

    @Override
    public UserRole getUserRole() {
        return UserRole.SHELTER_ADMIN;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public Long getShelterId() {
        return this.shelterId;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
