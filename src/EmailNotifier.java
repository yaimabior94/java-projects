public class EmailNotifier implements Notifiable {
    private String email;

    public EmailNotifier(String email) {
        this.email = email;
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("[Email → " + email + "] " + message);
    }

    @Override
    public String getChannel() { return "Email"; }
}