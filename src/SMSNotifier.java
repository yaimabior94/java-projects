public class SMSNotifier implements Notifiable {
    private String phone;

    public SMSNotifier(String phone) {
        this.phone = phone;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("[SMS → " + phone + "] " + message);
    }

    @Override
    public String getChannel() { return "SMS"; }
}