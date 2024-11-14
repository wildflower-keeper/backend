package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShelterAccountRepository extends JpaRepository<ShelterAccount, Long> {
    Optional<ShelterAccount> findShelterAccountByEmailOrPhoneNumber(String email, String phoneNumber);

    Optional<ShelterAccount> findShelterAccountByEmail(String email);
}
