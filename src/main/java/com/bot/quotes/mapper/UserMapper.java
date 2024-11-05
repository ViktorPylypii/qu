package com.bot.quotes.mapper;

import com.bot.quotes.config.MapperConfig;
import com.bot.quotes.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(Long chatId, String userName);
}
