package com.bot.quotes.bot;

import com.bot.quotes.constant.Constants;
import com.bot.quotes.model.Quote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AnimeMornBot extends TelegramLongPollingBot {
    private static final String START_COMMAND = "/start";
    private final String botUserName;

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage() == null ? "nothing" : update.getMessage().getText();
        if( text != null && text.equals(START_COMMAND)){
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

    public void sendRyanGoslingToChat(Quote ryanGoslingQuote) {
        byte[] imageDataBytes = ryanGoslingQuote.getImageData();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("tempImage", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(imageDataBytes);
            }

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId("-1002294333131");
            sendPhoto.setPhoto(new InputFile(tempFile));

            String caption = escapeMarkdown(ryanGoslingQuote.getQuote() + System.lineSeparator() + Constants.Ryan_Gosling);
            sendPhoto.setCaption(caption);
            sendPhoto.setParseMode("MarkdownV2");

            execute(sendPhoto);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private String escapeMarkdown(String text) {
        return text.replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
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
