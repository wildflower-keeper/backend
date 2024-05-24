package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessPageRequest;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NumberPageResponse;

public interface HomelessFilterPager {

  NumberPageResponse<HomelessResponse> getPage(HomelessPageRequest pageRequest);
}
