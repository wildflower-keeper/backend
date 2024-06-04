package org.wildflowergardening.backend.core.wildflowergardening.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomelessTermsIdEssentialDto {

  private Long id;
  private Boolean isEssential;
}