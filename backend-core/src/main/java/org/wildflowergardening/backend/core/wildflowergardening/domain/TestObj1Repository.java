package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TestObj1Repository {

  private final List<TestObj1> list = new ArrayList<>();

  public Long generateId() {
    return (long) list.size() + 1;
  }

  public TestObj1 save(TestObj1 testObj1) {
    list.add(testObj1);
    return testObj1;
  }

  public List<TestObj1> getListOrderByCreatedDesc(
      ZonedDateTime lastCreatedAt, Long lastId, int size
  ) {
    Comparator<TestObj1> createdDescComparator = (o1, o2) -> {
      int compareResult = o2.getCreatedAt().compareTo(o1.getCreatedAt());

      if (compareResult != 0) {
        return compareResult;
      }
      return o2.getId().compareTo(o1.getId());
    };

    if (Objects.isNull(lastCreatedAt) && Objects.isNull(lastId)) {
      return list.stream()
          .sorted(createdDescComparator)
          .limit(size)
          .collect(Collectors.toList());
    }
    return list.stream()
        .filter(testObj1 -> testObj1.getCreatedAt().isBefore(lastCreatedAt)
            || (testObj1.getCreatedAt().equals(lastCreatedAt)) && testObj1.getId() < lastId)
        .sorted(createdDescComparator)
        .limit(size)
        .collect(Collectors.toList());
  }
}
