package org.wildflowergardening.backend.api.wildflowergardening.presentation;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wildflowergardening.backend.api.wildflowergardening.application.FCMService;
import org.wildflowergardening.backend.api.wildflowergardening.application.dto.FcmSendDto;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "FCM test API")
@Validated
public class FcmController {
    private final FCMService fcmService;

    @PostMapping("/api/fcm/send")
    public ResponseEntity<Integer> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        int result = fcmService.sendMessageTo(fcmSendDto);

        return ResponseEntity.ok(result);
    }
}
