package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.NoticePageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.NoticeResponse;

public interface NoticeFilterPager {
    NumberPageResponse<NoticeResponse> getPage(NoticePageRequest pageRequest);
}
