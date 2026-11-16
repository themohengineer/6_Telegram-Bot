package org.mohamadi.M___Controller___M;

import org.mohamadi.BotInfo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotInfo botInfo;

    public TelegramBot(BotInfo botInfo) {
        this.botInfo = botInfo;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            System.out.println("    ****** Updates received! ******\n            " + text + "\n\n");

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText("سلام از ربات به شما کاربر عزیز");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                System.out.println("    ****** Error sending message! ******\n            " + e.getMessage());
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return botInfo.getUsername();
    }

    @Override
    public String getBotToken() {
        return botInfo.getToken();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }
}
