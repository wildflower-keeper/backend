package org.wildflowergardening.backend.api.wildflowergardening.application;

import static org.wildflowergardening.backend.api.wildflowergardening.application.exception.WildflowerExceptionType.SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.api.kernel.application.exception.ApplicationLogicException;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.HomelessResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.SessionResponse;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.ShelterLoginRequest;
import org.wildflowergardening.backend.core.wildflowergardening.application.HomelessService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SessionService;
import org.wildflowergardening.backend.core.wildflowergardening.application.ShelterService;
import org.wildflowergardening.backend.core.wildflowergardening.application.SleepoverService;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.NumberPageResult;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Homeless;
import org.wildflowergardening.backend.core.wildflowergardening.domain.Shelter;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.Session;
import org.wildflowergardening.backend.core.wildflowergardening.domain.auth.UserRole;

@Service
@RequiredArgsConstructor
public class ShelterAdminAppService {

  private final SessionService sessionService;
  private final ShelterService shelterService;
  private final HomelessService homelessService;
  private final SleepoverService sleepoverService;
  private final PasswordEncoder passwordEncoder;

  public SessionResponse login(ShelterLoginRequest dto) {
    Optional<Shelter> shelterOptional = shelterService.getShelterById(dto.getId());

    if (shelterOptional.isEmpty() || !passwordEncoder.matches(
        dto.getPw(), shelterOptional.get().getPassword()
    )) {
      throw new ApplicationLogicException(SHELTER_ADMIN_LOGIN_ID_PASSWORD_INVALID);
    }

    Shelter shelter = shelterOptional.get();
    LocalDateTime now = LocalDateTime.now();

    Session session = sessionService.save(Session.builder()
        .uuid(UUID.randomUUID().toString())
        .userRole(UserRole.SHELTER)
        .userId(shelter.getId())
        .username(shelter.getName())
        .createdAt(now)
        .expiredAt(now.plusHours(1))
        .build());

    return SessionResponse.builder()
        .sessionId(session.getUuid())
        .expiredAt(session.getExpiredAt())
        .build();
  }

  public NumberPageResult<HomelessResponse> getHomelessPage(
      Long shelterId, int pageNumber, int pageSize
  ) {
    NumberPageResult<Homeless> result = homelessService.getPage(
        shelterId, pageNumber, pageSize
    );
    List<Long> homelessIds = result.getItems().stream()
        .map(Homeless::getId)
        .toList();
    Set<Long> sleepoverHomelessIds = sleepoverService.filterSleepoverHomelessIds(
        homelessIds, LocalDate.now()
    );
    return NumberPageResult.<HomelessResponse>builder()
        .items(
            result.getItems().stream()
                .map(homeless -> HomelessResponse.builder()
                    .id(homeless.getId())
                    .name(homeless.getName())
                    .room(homeless.getRoom())
                    .todaySleepover(sleepoverHomelessIds.contains(homeless.getId()))
                    .birthDate(homeless.getBirthDate())
                    .phoneNumber(homeless.getPhoneNumber())
                    .build())
                .collect(Collectors.toList())
        )
        .next(result.getNext())
        .build();
  }
}
