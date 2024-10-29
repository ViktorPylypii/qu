package com.bot.quotes.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuoteClient {
    private static final String ANIME_API_URL = "https://animechan.io/api/v1/quotes/random";

    private final ObjectMapper objectMapper;

}
