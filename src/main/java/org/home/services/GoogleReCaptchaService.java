package org.home.services;

import org.home.dto.CaptchaResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class GoogleReCaptchaService {
    @Value("${google.captcha.secret}")
    private String secret;
    @Value("${google.captcha.url}")
    private String url;

    private final RestTemplate restTemplate;

    public GoogleReCaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CaptchaResponseDto checkCaptcha(String captchaRes) {
        String queryUrl = String.format(url + "?secret=%s&response=%s", secret, captchaRes);
        return restTemplate.postForObject(queryUrl, Collections.emptyList(), CaptchaResponseDto.class);
    }
}
