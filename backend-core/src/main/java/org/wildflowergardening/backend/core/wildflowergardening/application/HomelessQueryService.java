package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult.PageInfoResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.HomelessRepository;

@Service
@RequiredArgsConstructor
public class HomelessQueryService {

  private static final Sort SORT_NAME_ASC = Sort.by(
      Order.asc("name"), Order.desc("id")
  );

  private final HomelessRepository homelessRepository;

  @Transactional(readOnly = true)
  public Optional<Homeless> getOneById(Long id) {
    return homelessRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public NumberPageResult<Homeless> getPage(
      Long shelterId, int pageNumber, int pageSize
  ) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, SORT_NAME_ASC);
    Page<Homeless> homelessPage = homelessRepository.findAllByShelterId(
        shelterId, pageRequest
    );
    return NumberPageResult.<Homeless>builder()
        .items(homelessPage.getContent())
        .pagination(PageInfoResult.of(homelessPage))
        .build();
  }

  @Transactional(readOnly = true)
  public NumberPageResult<Homeless> getPage(
      Long shelterId, String name, int pageNumber, int pageSize
  ) {
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize,
        Sort.by(Direction.DESC, "id"));
    Page<Homeless> homelessPage = homelessRepository.findAllByShelterIdAndNameLike(
        shelterId, name, pageRequest
    );
    return NumberPageResult.<Homeless>builder()
        .items(homelessPage.getContent())
        .pagination(PageInfoResult.of(homelessPage))
        .build();
  }

  @Transactional(readOnly = true)
  public long count(Long shelterId) {
    return homelessRepository.countByShelterId(shelterId);
  }
}
