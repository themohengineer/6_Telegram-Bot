package org.mohamadi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component
public class BotInfo {

    private final String username;
    private final String token;

    public BotInfo(@Value("${bot.username}") String username,
                   @Value("${bot.token}") String token) {
        this.username = username;
        this.token = token;
    }
}
