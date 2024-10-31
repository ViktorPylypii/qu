package com.bot.quotes.client;

import com.bot.quotes.dto.animeInfoDto.AnimeInfoDto;
import com.bot.quotes.mapper.QuoteMapper;
import com.bot.quotes.model.Quote;
import com.bot.quotes.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuoteInitializerClient {

    private final QuoteClient quoteClient;
    private final Logger logger;
    private final QuoteMapper quoteMapper;
    private final QuoteRepository repository;

    @Scheduled(initialDelay = 5000, fixedRate = 3600000)
    private void initializeQuote(){
        logger.info("Дістаєм п'ять цитат");
        for (int i=0; i<5; i++){
            Optional<AnimeInfoDto> randomQuote = quoteClient.getRandomQuote();
            AnimeInfoDto animeInfoDto = randomQuote.orElseThrow(() -> new RuntimeException("Can't get random quote"));
            Quote model = quoteMapper.toModel(
                    animeInfoDto.getData().getContent(),
                    animeInfoDto.getData().getCharacter().getName(),
                    animeInfoDto.getData().getAnime().getName());
            Optional<Quote> byQuote = repository.findByQuote(model.getQuote());
            if (!byQuote.isPresent() && model.getQuote().length() <= 255) {
                    repository.save(model);
            }
        }
        logger.info("Дістали п'ять цитат");
    }
}
