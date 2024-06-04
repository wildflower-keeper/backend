package org.wildflowergardening.backend.core.wildflowergardening.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HomelessTermsRepository extends JpaRepository<HomelessTerms, Long> {

  @Query(" select t from HomelessTerms t "
      + " where t.startDate <= :targetDate "
      + " and "
      + " (t.deprecatedDate is null or :targetDate < t.deprecatedDate) ")
  List<HomelessTerms> findAll(@Param("targetDate") LocalDate targetDate);
}
