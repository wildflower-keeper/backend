package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HomelessRepository extends JpaRepository<Homeless, Long> {

    Optional<Homeless> findByPhoneNumberOrDeviceId(String phoneNumber, String deviceId);

    Optional<Homeless> findByDeviceId(String deviceId);

    Optional<Homeless> findByNameAndRoom(String name, String room);

    Page<Homeless> findAllByShelterId(Long shelterId, Pageable pageable);

    Optional<Homeless> findByNameAndShelterIdAndRoom(String name, Long shelterId, String room);

    @Query(" select h from Homeless h where h.shelter.id = :shelterId and h.name like %:name%")
    Page<Homeless> findAllByShelterIdAndNameLike(
            @Param("shelterId") Long shelterId, @Param("name") String name, Pageable pageable
    );

    long countByShelterId(Long shelterId);

    Optional<Homeless> findByIdAndShelterId(Long id, Long shelter_id);

    Page<Homeless> findByIdIn(Set<Long> homelessIds, Pageable pageable);

    @Query("select h.id from Homeless h where h.shelter.id = :shelterId")
    Set<Long> findIdsByShelterId(@Param("shelterId") Long shelterId);
}
