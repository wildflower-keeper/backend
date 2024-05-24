package org.wildflowergardening.backend.api.wildflowergardening.application.pager;

import java.util.Map;
import org.springframework.stereotype.Component;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessFilterType;

@Component
public class HomelessFilterPagerProvider {

  private final Map<HomelessFilterType, HomelessFilterPager> filterPagers;

  public HomelessFilterPagerProvider(
      HomelessNoFilterPager noFilterPager,
      HomelessLocationStatusFilterPager locationStatusFilterPager,
      HomelessSleepoverFilterPager sleepoverFilterPager, HomelessNameFilterPager nameFilterPager
  ) {
    this.filterPagers = Map.of(
        HomelessFilterType.NONE, noFilterPager,
        HomelessFilterType.LOCATION_STATUS, locationStatusFilterPager,
        HomelessFilterType.SLEEPOVER, sleepoverFilterPager,
        HomelessFilterType.NAME, nameFilterPager
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
