public class PushNotifier implements Notifiable {
    private String deviceToken;

    public PushNotifier(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("[Push → " + deviceToken + "] " + message);
    }

    @Override
    public String getChannel() { return "Push"; }
}