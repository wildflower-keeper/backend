package org.wildflowergardening.backend.api.kernel.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    private void init() throws IOException {
        //FileInputStream serviceAccount = new FileInputStream("firebaseKey.json");
        String fileResourceURL = "firebaseKey.json";
        ClassPathResource resource = new ClassPathResource(fileResourceURL);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .build();

        FirebaseApp.initializeApp(options);
    }

}