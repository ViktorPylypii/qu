package com.bot.quotes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestQuoteDto {

    @NotBlank
    private String quote;

    @NotBlank
    private String characterName;

    @NotBlank
    private String animeName;
}
