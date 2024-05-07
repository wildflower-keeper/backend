package org.wildflowergardening.backend.api.wildflowergardening.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wildflowergardening.backend.core.wildflowergardening.application.TestObj1Service;
import org.wildflowergardening.backend.core.wildflowergardening.application.dto.TestObj1CreateDto;

@Service
@RequiredArgsConstructor
public class TestCommandService {

  private final TestObj1Service testObj1Service;

  public long createTestObj1(TestObj1CreateDto createDto) {
    return testObj1Service.create(createDto);
  }
}
