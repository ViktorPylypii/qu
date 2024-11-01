package com.bot.quotes.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AnimeMornBot extends TelegramLongPollingBot {
    private static final String START_COMMAND = "/start";
    private String botUserName;

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        if(text.equals(START_COMMAND)){
            handleStartCommand(update);
        }
    }
    private void handleStartCommand(Update update){
        SendMessage sendMessage = new SendMessage();
        Long chatId = update.getMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setText("To see the morning quotes, follow the link: https://t.me/+a0mnlseve8M0MzYy");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't handle start command");
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
    public AnimeMornBot(@Value("${bot.token}") String token,
                        @Value("${bot.name}") String botName){
        super(token);
        this.botUserName = botName;

    }
}
