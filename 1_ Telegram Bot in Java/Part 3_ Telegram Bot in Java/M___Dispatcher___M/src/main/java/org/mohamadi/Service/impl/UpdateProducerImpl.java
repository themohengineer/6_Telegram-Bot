package org.mohamadi.Service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.mohamadi.Service.UpdateProducer;
import org.mohamadi.utils.PersianTextUtils;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
@Slf4j
public class UpdateProducerImpl implements UpdateProducer {
    @Override
    public void produce(String rabbitQueue, Update update) {

        var message = update.getMessage();
        String text = message.getText() != null ? message.getText() : message.getCaption();
        log.info("Producer -> queue: {}, text: {}", rabbitQueue, PersianTextUtils.normalize(text));
    }
}
