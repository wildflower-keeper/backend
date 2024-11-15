package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShelterAccountRepository extends JpaRepository<ShelterAccount, Long> {
    @Query("SELECT sa FROM ShelterAccount sa WHERE (sa.email = :email OR sa.phoneNumber = :phoneNumber) AND sa.deletedAt IS NULL")
    Optional<ShelterAccount> findByEmailOrPhoneNumberAndDeletedAtIsNull(@Param("email") String email, @Param("phoneNumber") String phoneNumber);


    Optional<ShelterAccount> findShelterAccountByEmail(String email);

    Optional<ShelterAccount> findShelterAccountByShelterIdAndId(Long shelterId, Long id);
}
