package br.infnet.at.q5;

public class EmailNotificationChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending EMAIL: " + message);
    }
}
