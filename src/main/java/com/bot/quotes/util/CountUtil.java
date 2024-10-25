package com.bot.quotes.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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

    @PostConstruct
    public void postConstruct() {
        logger.info("Викликали метод постКонстракт");
    }

    @PreDestroy
    public void preDestroy() {
        logger.info("викликали метод преДестрой");
    }
}
