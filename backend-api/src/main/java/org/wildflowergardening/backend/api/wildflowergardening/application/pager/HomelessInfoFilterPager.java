package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import org.wildflowergardening.backend.api.wildflowergardening.application.dto.request.HomelessInfoPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoPageResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.response.HomelessInfoOutStatusData;

public interface HomelessInfoFilterPager<T> {
    HomelessInfoPageResponse<T> getPage(HomelessInfoPageRequest pageRequest);
}
