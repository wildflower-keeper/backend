package org.wildflowergardening.backend.core.wildflowergardening.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByShelterId(Long shelterId, Pageable pageable);

    List<Notice> findByIdIn(List<Long> noticeIds);
}
