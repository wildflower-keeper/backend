package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.SleepoverExcelDto;

@Service
@RequiredArgsConstructor
public class SleepoverExcelService {

  private final SleepoverService sleepoverService;
  private final SleepoverExcelCreator sleepoverExcelCreator;

  public void create(
      Long shelterId, LocalDateTime createdAtStart, LocalDateTime createdAtEnd,
      OutputStream outputStream
  ) throws IOException {

    List<SleepoverExcelDto> dtoList = sleepoverService.findAllByCreatedAtIn(
            shelterId, createdAtStart, createdAtEnd
        ).stream().map(sleepover -> SleepoverExcelDto.builder()
            .homelessName(sleepover.getHomeless().getName())
            .startDate(sleepover.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .endDate(sleepover.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .reason(sleepover.getReason())
            .emergencyContact(sleepover.getEmergencyContact())
            .createdAt(sleepover.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build())
        .toList();
    sleepoverExcelCreator.writeOnOutputStream(dtoList, outputStream);
  }
}
