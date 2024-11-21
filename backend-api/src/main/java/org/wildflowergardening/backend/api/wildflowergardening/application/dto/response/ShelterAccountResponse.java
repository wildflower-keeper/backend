package org.wildflowergardening.backend.api.wildflowergardening.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShelterAccountResponse {
    private Long id;

    @Schema(description = "생성 일시", example = "2024-11-01 13:33:06.292907")
    private LocalDateTime createdAt;

    @Schema(description = "이름", example = "김박사")
    private String name;

    @Schema(description = "기타 사항", example = "~~한 특이사항이 있음")
    private String remark;

    @Schema(description = "관리자 개인 전화번호", example = "01012341234")
    private String phoneNumber;

    @Schema(description = "관리자 유무", example = "false")
    private boolean hasAdminRole;
}
