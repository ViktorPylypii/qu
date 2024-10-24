package com.bot.quotes.controller;

import com.bot.quotes.service.QuoteService;
import com.bot.quotes.util.CountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/quotes") //http://localhost:8080/api/quotes
public class QuoteController {
    private final QuoteService quoteService;
    private final CountUtil countUtil;
    @GetMapping(value = "/size") //http://localhost:8080/api/quotes/size
    public Long getSize(){
        countUtil.increase();
       return quoteService.getSize();
    }
}
