package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HomelessRepository extends JpaRepository<Homeless, Long> {

  Optional<Homeless> findByPhoneNumberOrDeviceId(String phoneNumber, String deviceId);

  Page<Homeless> findAllByShelterId(Long shelterId, Pageable pageable);

  @Query(" select h from Homeless h where h.shelter.id = :shelterId and h.name like %:name%")
  Page<Homeless> findAllByShelterIdAndNameLike(
      @Param("shelterId") Long shelterId, @Param("name") String name, Pageable pageable
  );

  long countByShelterId(Long shelterId);

  Optional<Homeless> findByIdAndShelterId(Long id, Long shelter_id);
}
