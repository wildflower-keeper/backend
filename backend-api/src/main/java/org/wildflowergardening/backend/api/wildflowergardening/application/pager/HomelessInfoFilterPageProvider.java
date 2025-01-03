package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessFilterType;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessInfoFilterType;

import java.util.Map;

@Component
public class HomelessInfoFilterPageProvider {

    private final Map<HomelessInfoFilterType, HomelessInfoFilterPager<?>> filterPagers;

    public HomelessInfoFilterPageProvider(
            HomelessInfoOutStateFilterPager outStateFilterPager,
            HomelessInfoOvernightFilterPager overnightFilterPager
    ) {
        this.filterPagers = Map.of(
                HomelessInfoFilterType.OUT, outStateFilterPager,
                HomelessInfoFilterType.OVERNIGHT, overnightFilterPager
        );
    }

    public <T> HomelessInfoFilterPager<T> from(HomelessInfoFilterType homelessInfoFilterType) {
        if (!filterPagers.containsKey(homelessInfoFilterType)) {
            throw new IllegalStateException("homelessInfoFilterType="
                    + homelessInfoFilterType.name() + "에 대한 FilterPage 가 등록되어있지 않습니다.");
        }

        return (HomelessInfoFilterPager<T>) this.filterPagers.get(homelessInfoFilterType);
    }
}
