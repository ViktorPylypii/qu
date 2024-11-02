package com.bot.quotes.service.impl;

import com.bot.quotes.bot.AnimeMornBot;
import com.bot.quotes.constant.Constants;
import com.bot.quotes.exception.ImageNotFoundException;
import com.bot.quotes.mapper.QuoteMapper;
import com.bot.quotes.model.Quote;
import com.bot.quotes.repository.QuoteRepository;
import com.bot.quotes.service.RyanService;
import com.bot.quotes.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RyanServiceImpl implements RyanService {
    private static final String RYAN_QUOTE = "There’s a hundred-thousand streets in this city. You don’t need to know the route. You give me a time and a place, I give you a five minute window.";
    private final QuoteRepository quoteRepository;
    private final ImageUtil imageUtil;
    private final Logger logger;
    private final QuoteMapper quoteMapper;
    private final AnimeMornBot animeMornBot;

    @Transactional
    @Override
    public void sendRyanGosling() {
        Optional<Quote> ryanGosling = quoteRepository.findRyanGosling();
        if (ryanGosling.isPresent()) {
            animeMornBot.sendRyanGoslingToChat(ryanGosling.get());
        } else {
            try {
                byte[] image = imageUtil.findImage(Constants.Ryan_Gosling.name());
                Quote model = quoteMapper.toModel(RYAN_QUOTE, Constants.Ryan_Gosling.name(), Constants.Ryan_Gosling.name(), image);
                Quote save = quoteRepository.save(model);
                animeMornBot.sendRyanGoslingToChat(save);
            } catch (ImageNotFoundException e) {
                logger.warn("ImageNotFoundException occurred while trying to find image of RyanGosling");
            }
        }
    }
}
