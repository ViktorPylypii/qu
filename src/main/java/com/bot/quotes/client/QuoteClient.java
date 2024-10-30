package com.bot.quotes.client;

import com.bot.quotes.dto.animeInfoDto.AnimeInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class QuoteClient {
    private static final String ANIME_API_URL = "https://animechan.io/api/v1/quotes/random";

    private final ObjectMapper objectMapper;
    private final Logger logger;
    public AnimeInfoDto getRandomQuote(){
        AnimeInfoDto animeInfoDto = new AnimeInfoDto();
        try {
        URI uri = URI.create(ANIME_API_URL);
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest build = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            animeInfoDto = objectMapper.readValue(send.body(), AnimeInfoDto.class);

        } catch (IOException | InterruptedException e) {
            logger.warn("Не прийшли дані");
        }
        return animeInfoDto;
    }
}
