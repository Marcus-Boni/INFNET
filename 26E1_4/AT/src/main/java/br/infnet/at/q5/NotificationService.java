package br.infnet.at.q5;

import java.util.Objects;

public class NotificationService {
    public void notifyUser(NotificationChannel channel, String message) {
        Objects.requireNonNull(channel, "channel nao pode ser nulo");
        channel.send(message);
    }
}
