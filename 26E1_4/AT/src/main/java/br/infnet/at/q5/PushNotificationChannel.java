package br.infnet.at.q5;

public class PushNotificationChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending PUSH: " + message);
    }
}
