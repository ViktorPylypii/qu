package com.bot.quotes.dto.animeInfoDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AnimeInfoDto {
    private DataDto data;

}
