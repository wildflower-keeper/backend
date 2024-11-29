package org.wildflowergardening.backend.core.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Notice;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult.PageInfoResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.NoticeRepository;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private static final Sort SORT_CREATED_AT_DESC = Sort.by(
            Sort.Order.desc("createdAt"), Sort.Order.desc("id")
    );


    private final NoticeRepository noticeRepository;

    @Transactional
    public Long save(Notice notice) {
        return noticeRepository.save(notice).getId();
    }

    @Transactional(readOnly = true)
    public NumberPageResult<Notice> getPage(Long shelterId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, SORT_CREATED_AT_DESC);
        Page<Notice> noticePage = noticeRepository.getNoticeByShelterId(shelterId, pageRequest);

        return NumberPageResult.<Notice>builder()
                .items(noticePage.getContent())
                .pagination(PageInfoResult.of(noticePage))
                .build();
    }
}
