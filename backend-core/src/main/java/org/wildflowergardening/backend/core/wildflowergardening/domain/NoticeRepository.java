package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> getNoticeByShelterId(Long shelterId, Pageable pageable);

    Optional<Notice> getNoticeById(Long noticeId);
}
