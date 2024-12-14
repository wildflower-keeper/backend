package org.wildflowergardening.backend.core.kernel.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmMessageRequest;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmMultiSendDto;
import org.wildflowergardening.backend.core.kernel.config.dto.FcmSendDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {
    public int sendMessageTo(FcmSendDto fcmSendDto) throws IOException {
        try {
            String message = makeMessage(fcmSendDto);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getAccessToken());

            HttpEntity<String> entity = new HttpEntity<>(message, headers);

            String API_URL = "https://fcm.googleapis.com/v1/projects/wildflower-gardening/messages:send";
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);


            return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
        } catch (Exception e) {
            String errorResponse = e.getMessage();
            log.error(errorResponse);
            return 0;
        }
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebaseKey.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();

        FcmMessageRequest.Message.Notification notification = FcmMessageRequest.Message.Notification.builder()
                .title(fcmSendDto.getTitle())
                .body(fcmSendDto.getBody())
                .build();

        Map<String, String> dataMap = new HashMap<>();
        if (fcmSendDto.getData() != null) {
            if (fcmSendDto.getData().getScreen() != null) {
                dataMap.put("screen", fcmSendDto.getData().getScreen());
            }
            if (fcmSendDto.getData().getNoticeId() != null) {
                dataMap.put("noticeId", String.valueOf(fcmSendDto.getData().getNoticeId()));
            }
        }

        FcmMessageRequest.Message message = FcmMessageRequest.Message.builder()
                .token(fcmSendDto.getToken())
                .notification(notification)
                .data(dataMap)
                .build();

        FcmMessageRequest request = FcmMessageRequest.builder()
                .message(message)
                .validateOnly(false)
                .build();

        return om.writeValueAsString(request);
    }

    public int sendMessageToMultiple(FcmMultiSendDto fcmMultiSendDto) {
        List<String> tokens = fcmMultiSendDto.getTokens();
        String title = fcmMultiSendDto.getTitle();
        String body = fcmMultiSendDto.getBody();
        FcmSendDto.Data data = fcmMultiSendDto.getData();

        List<CompletableFuture<Integer>> futures = tokens.stream()
                .map(token -> {
                    FcmSendDto fcmSendDto = FcmSendDto.builder()
                            .token(token)
                            .title(title)
                            .body(body)
                            .data(data).build();
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            return sendMessageTo(fcmSendDto);
                        } catch (IOException e) {
                            log.error("전송 실패 토큰: {}", token, e);
                            return 0;
                        }
                    });
                })
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        int successCount = futures.stream()
                .mapToInt(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("전송 실패", e);
                        return 0;
                    }
                })
                .sum();

        log.info("성공 : {} 개성공 {} 개중", successCount, tokens.size());
        return successCount;
    }
}