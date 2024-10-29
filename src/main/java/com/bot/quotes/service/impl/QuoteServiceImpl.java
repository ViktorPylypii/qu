package com.bot.quotes.service.impl;

import com.bot.quotes.dto.RequestQuoteDto;
import com.bot.quotes.dto.ResponseQuoteDto;
import com.bot.quotes.mapper.QuoteMapper;
import com.bot.quotes.model.Quote;
import com.bot.quotes.repository.QuoteRepository;
import com.bot.quotes.service.QuoteService;
import com.bot.quotes.util.CountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final CountUtil countUtil;
    private final QuoteMapper quoteMapper;


    @Override
    public Long getSize() {
        countUtil.increase();
        return quoteRepository.count();
    }

    @Override
    public ResponseQuoteDto saveQuote(RequestQuoteDto quote) {
        return quoteMapper.toResponseQuoteDto(quoteRepository.save(quoteMapper.toModel(quote)));
    }

    @Override
    public List<ResponseQuoteDto> getAll() {
        return quoteRepository.findAll().stream().map(model -> quoteMapper.toResponseQuoteDto(model)).toList();
    }
}
