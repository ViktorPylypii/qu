package com.bot.quotes.mapper;

import com.bot.quotes.config.MapperConfig;
import com.bot.quotes.dto.RequestQuoteDto;
import com.bot.quotes.dto.ResponseQuoteDto;
import com.bot.quotes.model.Quote;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface QuoteMapper {
    Quote toModel(RequestQuoteDto requestQuoteDto);
    Quote toModel(String quote, String characterName, String animeName);

    //    @Mapping(target = "record", ignore = true)
    ResponseQuoteDto toResponseQuoteDto(Quote quote);

//    @AfterMapping
//    default void afterMapping(Quote quote, @MappingTarget  ResponseQuoteDto responseQuoteDto) {
//        responseQuoteDto.record = "Igor";
//    }
}
