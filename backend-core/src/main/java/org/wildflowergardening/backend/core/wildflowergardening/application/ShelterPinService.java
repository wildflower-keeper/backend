package org.wildflowergardening.backend.core.wildflowergardening.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPin;
import org.wildflowergardening.backend.core.wildflowergardening.domain.ShelterPinRepository;

@Service
@RequiredArgsConstructor
public class ShelterPinService {

  private final ShelterPinRepository shelterPinRepository;

  @Transactional
  public ShelterPin getShelterPin(Long shelterId) {
    Optional<ShelterPin> shelterPinOptional = shelterPinRepository.findByShelterId(shelterId);

    if (shelterPinOptional.isEmpty()) {
      return shelterPinRepository.save(ShelterPin.builder()
          .shelterId(shelterId)
          .pin(ShelterPin.generatePin())
          .build());
    }
    ShelterPin shelterPin = shelterPinOptional.get();
    if (shelterPin.isExpired()) {
      shelterPin.setPin(ShelterPin.generatePin());
      return shelterPinRepository.save(shelterPin);
    }
    return shelterPin;
  }

  @Transactional(readOnly = true)
  public boolean matches(Long shelterId, String pin) {
    Optional<ShelterPin> shelterPinOptional = shelterPinRepository.findByShelterId(shelterId);

    if (shelterPinOptional.isEmpty()) {
      return false;
    }
    ShelterPin shelterPin = shelterPinOptional.get();

    if (shelterPin.isExpired()) {
      return false;
    }
    return shelterPin.getPin().equals(pin);
  }
}
