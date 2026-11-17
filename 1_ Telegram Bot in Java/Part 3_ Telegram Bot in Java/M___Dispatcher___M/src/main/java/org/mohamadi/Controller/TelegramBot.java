package org.mohamadi.Controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.mohamadi.BotInfo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotInfo botInfo;
    private UpdateController updateController;

    public TelegramBot(BotInfo botInfo, UpdateController updateController) {
        this.botInfo = botInfo;
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {

        updateController.registerBot(this);
    }


    @Override
    public void onUpdateReceived(Update update) {

        log.info("Update received: {}", update);
        updateController.processUpdate(update);



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

    public void sendAnswerMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send answer message", e);
        }
    }
}
