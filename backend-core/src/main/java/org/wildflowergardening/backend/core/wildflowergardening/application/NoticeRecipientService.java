package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRecipient;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRecipientRepository;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;
import org.wildflowergardening.backend.core.wildflowergardening.domain.dto.NoticeRecipientReadDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeRecipientService {

    private final NoticeRecipientRepository noticeRecipientRepository;

    @Transactional
    public Long save(NoticeRecipient noticeRecipient) {
        return noticeRecipientRepository.save(noticeRecipient).getId();
    }

    @Transactional
    public void updateReadStatus(Long noticeId, Long homelessId, boolean status) {
        List<NoticeRecipient> noticeRecipientList = noticeRecipientRepository.findByNoticeIdAndHomelessId(noticeId, homelessId);
        for (NoticeRecipient noticeRecipient : noticeRecipientList) {
            noticeRecipient.setRead(status);
            noticeRecipient.setReadAt(LocalDateTime.now());
        }
    }

    @Transactional(readOnly = true)
    public Map<Long, Boolean> getAllHomelessIdAndReadByNoticeId(Long noticeId, Long shelterId) {
        return noticeRecipientRepository.findAllHomelessIdAndReadByNoticeId(noticeId, shelterId).stream()
                .collect(Collectors.toMap(
                        NoticeRecipientReadDto::getHomelessId,
                        NoticeRecipientReadDto::isRead
                ));
    }


    @Transactional(readOnly = true)
    public List<NoticeRecipient> getRecentNoticeIdByHomelessId(Long homelessId) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(3);
        return noticeRecipientRepository.findRecentNoticeIds(homelessId, startDate, endDate);
    }

    @Transactional
    public void deleteAllByHomelessId(Long homelessId) {
        List<NoticeRecipient> noticeRecipients = noticeRecipientRepository.findAllByHomelessId(homelessId);
        noticeRecipientRepository.deleteAll(noticeRecipients);
    }

    @Transactional(readOnly = true)
    public Long getCountByNoticeIdAndReadStatus(Long noticeId, boolean readStatus) {
        return noticeRecipientRepository.findCountByNoticeIdAndReadStatus(noticeId, readStatus);
    }
}

