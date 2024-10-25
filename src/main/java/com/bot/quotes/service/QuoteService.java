package com.bot.quotes.service;

import com.bot.quotes.dto.RequestQuoteDto;
import com.bot.quotes.dto.ResponseQuoteDto;
import com.bot.quotes.model.Quote;

import java.util.List;

public interface QuoteService {
     Long getSize();

     ResponseQuoteDto saveQuote(RequestQuoteDto quote);

     List<ResponseQuoteDto> getAll();
}
