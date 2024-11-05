package com.bot.quotes.bot;

import com.bot.quotes.constant.Constants;
import com.bot.quotes.mapper.UserMapper;
import com.bot.quotes.model.Quote;
import com.bot.quotes.model.User;
import com.bot.quotes.repository.QuoteRepository;
import com.bot.quotes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class AnimeMornBot extends TelegramLongPollingBot {
    private static final String START_COMMAND = "/start";
    private static final String HELP_COMMAND = "/help";
    private static final String UNSUBSCRIBE_COMMAND = "/unsubscribe";
    private static final String SUBSCRIBE_COMMAND = "/subscribe";
    private final String botUserName;
    private final QuoteRepository quoteRepository;
    private final Random random;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage() == null ? "nothing" : update.getMessage().getText();
        if( text != null && text.equals(START_COMMAND)){
            handleStartCommand(update);
        } else if( text != null && text.equals(HELP_COMMAND)){
            handleHelpCommand(update);
        }else if( text != null && text.equals(SUBSCRIBE_COMMAND)){
            handleSubscribeCommand(update);
        }
        else if( text != null && text.equals(UNSUBSCRIBE_COMMAND)){
            handleUnsubscribeCommand(update);
        }
    }

    @Scheduled(initialDelay = 19000, fixedRate = 3600000)
    private void usersMailing(){
        List<User> allUsers = userRepository.findAll();
        List<Quote> allQuotes = quoteRepository.findAll();
        SendPhoto sendPhoto = new SendPhoto();
        for (User user : allUsers) {
            int randomNum = random.nextInt(allQuotes.size());
            Quote quote = allQuotes.get(randomNum);
            sendPhoto.setChatId(user.getChatId());
            byte[] imageDataBytes = quote.getImageData();
            File tempFile = null;
            try {
                tempFile = File.createTempFile("tempImage", ".jpg");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(imageDataBytes);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sendPhoto.setPhoto(new InputFile(tempFile));
            sendPhoto.setCaption(quote.getQuote() + System.lineSeparator() + quote.getCharacterName());
            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleHelpCommand(Update update){
        SendMessage sendMessage = new SendMessage();
        Long chatId = update.getMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello to AnimeMornBot:"
                + System.lineSeparator() + "here list of commands:"
                + System.lineSeparator() + "/unsubscribe to unsubscribe from morning quotes"
                + System.lineSeparator() + "/subscribe command to re-subscribe"
                + System.lineSeparator() + "To see more morning quotes, follow the link: https://t.me/+a0mnlseve8M0MzYy"
        );
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't handle help command");
        }
    }

    private void handleStartCommand(Update update){
        String userName = update.getMessage().getFrom().getUserName();
        Long chatId = update.getMessage().getChatId();
        User model = userMapper.toModel(chatId, userName);
        userRepository.save(model);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello thanks for subscribing to ours AnimeMornBot mailing we will send you your personalized " +
                "quote every morning to see more commands write /help");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't handle start command");
        }
    }

    private void handleSubscribeCommand(Update update){
        String userName = update.getMessage().getFrom().getUserName();
        Long chatId = update.getMessage().getChatId();
        Optional<User> byChatId = userRepository.findByChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (byChatId.isPresent()){
            sendMessage.setText("You are already subscribed");
        } else {
            User model = userMapper.toModel(chatId, userName);
            userRepository.save(model);
            sendMessage.setText("You now subscribing to our mailing");
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't handle subscribe command");
        }
    }

    private void handleUnsubscribeCommand(Update update){
        Long chatId = update.getMessage().getChatId();
        Optional<User> byChatId = userRepository.findByChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (byChatId.isPresent()){
            User user = byChatId.get();
            userRepository.deleteById(user.getId());
            sendMessage.setText("You are now unsubscribed, thanks for using our bot");
        } else {
            sendMessage.setText("You are not subscribed to our mailing");
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Can't handle unsubscribe command");
        }
    }

    public void sendRyanGoslingToChat(Quote ryanGoslingQuote) {
        byte[] imageDataBytes = ryanGoslingQuote.getImageData();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("ryanGosling", ".jpg");
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

    @Scheduled(initialDelay = 20000, fixedRate = 3600000)
    public void sendNoonRandomQuotes() {
        long count = quoteRepository.count();
        long randomNumber = random.nextLong(count) + 1; // nextLong() - задає рандомне число в заданому проміжку
        Optional<Quote> byId = quoteRepository.findById(randomNumber);
        Quote quote = byId.orElseThrow(() -> new RuntimeException("Can't get random quote"));
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId("-1002294333131");
        byte[] imageDataBytes = quote.getImageData();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("tempImage", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(imageDataBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendPhoto.setPhoto(new InputFile(tempFile));
        sendPhoto.setCaption(quote.getQuote() + System.lineSeparator() + quote.getCharacterName());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(initialDelay = 15000, fixedRate = 3600000)
    public void sendMorningRandomQuotes() {
        long count = quoteRepository.count();
        long randomNumber = random.nextLong(count) + 1; // nextLong() - задає рандомне число в заданому проміжку
        Optional<Quote> byId = quoteRepository.findById(randomNumber);
        Quote quote = byId.orElseThrow(() -> new RuntimeException("Can't get random quote"));
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId("-1002294333131");
        byte[] imageDataBytes = quote.getImageData();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("tempImage", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(imageDataBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendPhoto.setPhoto(new InputFile(tempFile));
        sendPhoto.setCaption(quote.getQuote() + System.lineSeparator() + quote.getCharacterName());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(initialDelay = 30000, fixedRate = 3600000)
    public void sendEveningRandomQuotes() {
        long count = quoteRepository.count();
        long randomNumber = random.nextLong(count) + 1; // nextLong() - задає рандомне число в заданому проміжку
        Optional<Quote> byId = quoteRepository.findById(randomNumber);
        Quote quote = byId.orElseThrow(() -> new RuntimeException("Can't get random quote"));
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId("-1002294333131");
        byte[] imageDataBytes = quote.getImageData();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("tempImage", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(imageDataBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendPhoto.setPhoto(new InputFile(tempFile));
        sendPhoto.setCaption(quote.getQuote() + System.lineSeparator() + quote.getCharacterName());
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public AnimeMornBot(@Value("${bot.token}") String token,
                        @Value("${bot.name}") String botName,
                        QuoteRepository quoteRepository,
                        Random random,UserMapper userMapper, UserRepository userRepository) {
        super(token);
        this.botUserName = botName;
        this.quoteRepository = quoteRepository;
        this.random = random;
        this.userMapper = userMapper;
        this.userRepository = userRepository;

    }
}
