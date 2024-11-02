package com.bot.quotes.service.impl;

import com.bot.quotes.dto.RequestQuoteDto;
import com.bot.quotes.dto.ResponseQuoteDto;
import com.bot.quotes.exception.ImageNotFoundException;
import com.bot.quotes.mapper.QuoteMapper;
import com.bot.quotes.model.Quote;
import com.bot.quotes.repository.QuoteRepository;
import com.bot.quotes.service.QuoteService;
import com.bot.quotes.util.CountUtil;
import com.bot.quotes.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final QuoteRepository quoteRepository;
    private final CountUtil countUtil;
    private final QuoteMapper quoteMapper;
    private final ImageUtil imageUtil;
    private final Logger logger;


    @Override
    public Long getSize() {
        countUtil.increase();
        return quoteRepository.count();
    }

    @Override
    public ResponseQuoteDto saveQuote(RequestQuoteDto quote) {
        Quote model = quoteMapper.toModel(quote);
        String searchQuery = model.getCharacterName() + " from " + model.getAnimeName();
        try {
            model.setImageData(imageUtil.findImage(searchQuery));
        } catch (ImageNotFoundException e) {
            logger.warn("ImageNotFoundException occurred while saving quote search param : " + searchQuery);
        }
        return quoteMapper.toResponseQuoteDto(quoteRepository.save(model));
    }

    @Override
    public List<ResponseQuoteDto> getAll() {
        return quoteRepository.findAll().stream().map(quoteMapper::toResponseQuoteDto).toList();
    }
}
