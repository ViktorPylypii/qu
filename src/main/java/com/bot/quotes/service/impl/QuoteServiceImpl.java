package com.bot.quotes.service.impl;

import com.bot.quotes.repository.QuoteRepository;
import com.bot.quotes.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    @Override
    public Long getSize() {
        return (long) quoteRepository.findAll().size();
    }
}
