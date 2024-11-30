package org.wildflowergardening.backend.core.wildflowergardening.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomelessNamePhoneDto {
    private Long id;
    private String name;
    private String phoneNumber;
}
