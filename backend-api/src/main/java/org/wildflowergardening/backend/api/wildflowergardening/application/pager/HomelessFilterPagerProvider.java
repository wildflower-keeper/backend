package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessFilterType;

@Component
public class HomelessFilterPagerProvider {

  private final Map<HomelessFilterType, HomelessFilterPager> filterPagers;

  public HomelessFilterPagerProvider(
      HomelessNoFilterPager homelessNoFilterPager,
      HomelessLocationStatusFilterPager homelessLocationStatusFilterPager
  ) {
    this.filterPagers = Map.of(
        HomelessFilterType.NONE, homelessNoFilterPager,
        HomelessFilterType.LOCATION_STATUS, homelessLocationStatusFilterPager
    );
  }

  public HomelessFilterPager from(HomelessFilterType homelessFilterType) {
    if (!filterPagers.containsKey(homelessFilterType)) {
      throw new IllegalStateException("HomelessFilterType="
          + homelessFilterType.name() + "에 대한 FilterPage 가 등록되어있지 않습니다.");
    }
    return this.filterPagers.get(homelessFilterType);
  }
}
