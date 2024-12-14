package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.NoticePageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.NoticeResponse;
import org.wildflowergardening.backend.core.wildflowergardening.application.NoticeRecipientService;
import org.wildflowergardening.backend.core.wildflowergardening.application.NoticeService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse.PageInfoResponse;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Notice;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NoticeNoFilterPager implements NoticeFilterPager {

    private final NoticeService noticeService;
    private final NoticeRecipientService noticeRecipientService;

    @Override
    public NumberPageResponse<NoticeResponse> getPage(NoticePageRequest pageRequest) {
        NumberPageResult<Notice> result = noticeService.getPage(pageRequest.getShelterId(), pageRequest.getPageNumber(), pageRequest.getPageSize());

        return NumberPageResponse.<NoticeResponse>builder()
                .items(
                        result.getItems().stream()
                                .sorted(Comparator.comparing(Notice::getCreatedAt).reversed())
                                .map(notice -> NoticeResponse.builder()
                                        .noticeId(notice.getId())
                                        .title(notice.getTitle())
                                        .contents(notice.getContents())
                                        .createdAt(notice.getCreatedAt())
                                        .isSurvey(notice.getIsSurvey())
                                        .readCount(noticeRecipientService.getCountByNoticeIdAndReadStatus(notice.getId(), true))
                                        .totalCount(noticeRecipientService.getAllCountByNotice(notice.getId()))
                                        .build())
                                .collect(Collectors.toList())
                )
                .pagination(PageInfoResponse.of(result.getPagination()))
                .build();

    }
}
