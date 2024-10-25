package com.bot.quotes.dto;

import lombok.Data;

@Data
public class ResponseQuoteDto {
    private Long id;
    private String quote;
    private String characterName;
    private String animeName;
//    public String record;
}
