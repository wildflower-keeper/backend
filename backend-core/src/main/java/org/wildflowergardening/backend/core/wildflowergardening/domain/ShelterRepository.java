package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Optional<Shelter> findByName(String name);

    Optional<Shelter> findByEmail(String email);
}
