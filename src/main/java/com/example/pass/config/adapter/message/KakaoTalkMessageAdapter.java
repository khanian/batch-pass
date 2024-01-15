package com.example.pass.config.adapter.message;

import com.example.pass.config.KakaoTalkMessageConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KakaoTalkMessageAdapter {
    private final WebClient webClient;

    public KakaoTalkMessageAdapter(KakaoTalkMessageConfiguration configuration) {
        webClient = WebClient.builder()
                .baseUrl(configuration.getHost())
                .defaultHeaders(h -> {
                    h.setBearerAuth(configuration.getToken());
                    h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }

    public boolean sendKakaoTalkMessage(final String uuid, final String text) {
        KakaoTalkMessageResponse response = webClient.post().uri("v1/api/talk/friend/message/default/send")
                .body(BodyInserters.fromValue(new KakaoTalkMessageRequest(uuid, text)))
                .retrieve()
                .bodyToMono(KakaoTalkMessageResponse.class)
                .block();

        if (response == null || response.getSuccessfulReceiverUuids() == null) {
            return false;
        }

//        return response.getSuccessfulReceiverUuids().size() > 0;
        return !response.getSuccessfulReceiverUuids().isEmpty();
    }
}
