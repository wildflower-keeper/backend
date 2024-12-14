package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByShelterId(Long shelterId, Pageable pageable);

    Page<Notice> findByShelterIdAndIsGlobal(Long shelterId, Boolean isGlobal, Pageable pageable);

    List<Notice> findByIdIn(List<Long> noticeIds);

    Optional<Notice> findByIdAndShelterId(Long noticeId, Long shelterId);

}
