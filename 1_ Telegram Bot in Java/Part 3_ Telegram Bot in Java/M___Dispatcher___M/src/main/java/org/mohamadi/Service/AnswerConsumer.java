package org.mohamadi.Service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AnswerConsumer {


    void consume(SendMessage sendMessage);

}
