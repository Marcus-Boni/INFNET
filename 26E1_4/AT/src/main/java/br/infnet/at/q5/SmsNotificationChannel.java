package br.infnet.at.q5;

public class SmsNotificationChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
