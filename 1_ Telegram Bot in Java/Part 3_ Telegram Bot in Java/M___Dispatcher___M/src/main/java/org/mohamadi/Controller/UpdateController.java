package org.mohamadi.Controller;


import lombok.extern.slf4j.Slf4j;
import org.mohamadi.Service.UpdateProducer;
import org.mohamadi.model.RabbitQueue;
import org.mohamadi.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class UpdateController {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;


    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }


    public void registerBot(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;

    }


    public void processUpdate(Update update) {
        if (update.getMessage() != null)
            distributeMessageByType(update);
    }

    public void distributeMessageByType(Update update) {

        var message = update.getMessage();

        if (message.getText() != null)
            processTextMessage(update);
        else if (message.getDocument() != null)
            processDocumentMessage(update);
        else if (message.getPhoto() != null)
            processPhotoMessage(update);
        else
            setUnsupportedMessageTypeView(update);

    }

    private void processTextMessage(Update update) {

        updateProducer.produce(RabbitQueue.TEXT_MESSAGE_UPDATE, update);
    }

    private void processDocumentMessage(Update update) {
        updateProducer.produce(RabbitQueue.DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(RabbitQueue.PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);

    }

    private void setUnsupportedMessageTypeView(Update update) {

        var sendMessage = messageUtils
                .generateSendMessageWithText(update, "داده ای که شما فرستادید پشتیبانی نمیشود !");
        setView(sendMessage);

    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils
                .generateSendMessageWithText(update, "فایل دریافت شد!");
        setView(sendMessage);
    }
}
