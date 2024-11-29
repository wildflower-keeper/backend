package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.NoticeFilterType;

import java.util.Map;

@Component
public class NoticeFilterPagerProvider {

    private final Map<NoticeFilterType, NoticeFilterPager> filterPagers;

    public NoticeFilterPagerProvider(
            NoticeNoFilterPager noFilterPager
    ) {
        this.filterPagers = Map.of(
                NoticeFilterType.NONE, noFilterPager);
    }

    public NoticeFilterPager from(NoticeFilterType noticeFilterType) {
        if (!filterPagers.containsKey(noticeFilterType)) {
            throw new IllegalStateException("NoticeFilterType="
                    + noticeFilterType.name() + "에 대한 FilterPage 가 등록되어있지 않습니다.");
        }
        return this.filterPagers.get(noticeFilterType);
    }
}
