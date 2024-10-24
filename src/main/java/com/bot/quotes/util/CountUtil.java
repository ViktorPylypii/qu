package com.bot.quotes.util;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CountUtil {
    private final Logger logger;
    private static int count;
    public void increase(){
        logger.info("increasing...");
        count++;
        logger.info("count size is " + count);
    }
}
