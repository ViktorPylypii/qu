package com.bot.quotes.controller;

import com.bot.quotes.constant.Constants;
import com.bot.quotes.dto.RequestQuoteDto;
import com.bot.quotes.dto.ResponseQuoteDto;
import com.bot.quotes.service.QuoteService;
import com.bot.quotes.service.RyanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/quotes") //http://localhost:8080/api/quotes
public class QuoteController {
    private final QuoteService quoteService;
    private final RyanService rayanService;

    @GetMapping(value = "/size") //http://localhost:8080/api/quotes/size
    public Long getSize(){
       return quoteService.getSize();
    }

    @GetMapping(value = "/all") //http://localhost:8080/api/quotes/all
    public List<ResponseQuoteDto> getAll(){
       return quoteService.getAll();
    }

    @PostMapping(value = "/save") //http://localhost:8080/api/quotes/save
    public ResponseQuoteDto save(@RequestBody @Valid RequestQuoteDto requestQuoteDto){
        return quoteService.saveQuote(requestQuoteDto);
    }

    @GetMapping(value = "/ryan") // http://localhost:8080/api/quotes/ryan
    @ResponseStatus(code = HttpStatus.OK)
    public String ryan() {
        rayanService.sendRyanGosling();
        return Constants.Ryan_Gosling.name(); // name() - перетворює enum в String
    }
}
