package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult.PageInfoResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless.LocationStatus;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessRepository;

@Service
@RequiredArgsConstructor
public class HomelessService {

  private final HomelessRepository homelessRepository;

  @Transactional
  public Long create(Homeless homeless) {
    Optional<Homeless> homelessOptional = homelessRepository.findByDeviceId(
        homeless.getDeviceId()
    );
    if (homelessOptional.isPresent()) {
      throw new IllegalArgumentException("해당 디바이스로 이미 등록된 계정이 있습니다.");
    }
    return homelessRepository.save(homeless).getId();
  }

  @Transactional(readOnly = true)
  public Optional<Homeless> getOneByDeviceId(String deviceId) {
    return homelessRepository.findByDeviceId(deviceId);
  }

  @Transactional(readOnly = true)
  public Optional<Homeless> getOneById(Long id) {
    return homelessRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public NumberPageResult<Homeless> getPage(
      Long shelterId, int pageNumber, int pageSize
  ) {
    PageRequest pageRequest = PageRequest.of(
        pageNumber - 1,
        pageSize,
        Sort.by(Order.asc("name"), Order.desc("id"))
    );
    Page<Homeless> homelessPage = homelessRepository.findAllByShelterId(shelterId, pageRequest);
    int totalPages = homelessPage.getTotalPages();

    return NumberPageResult.<Homeless>builder()
        .items(homelessPage.getContent())
        .pagination(PageInfoResult.builder()
            .currentPageNumber(pageNumber)
            .nextPageNumber(pageNumber < totalPages ? pageNumber + 1 : null)
            .pageSize(pageSize)
            .lastPageNumber(totalPages)
            .build())
        .build();
  }

  @Transactional
  public void updateLocationStatus(
      Long homelessId, LocationStatus lastLocationStatus, LocalDateTime lastLocationTrackedAt
  ) {
    Homeless homeless = homelessRepository.findById(homelessId)
        .orElseThrow(() -> new IllegalArgumentException("Homeless not found"));

    homeless.setLastLocationStatus(lastLocationStatus);
    homeless.setLastLocationTrackedAt(lastLocationTrackedAt);
  }
}
