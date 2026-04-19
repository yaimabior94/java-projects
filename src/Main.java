import java.util.List;

public class Main {
    public static void main(String[] args) {

        POSSystem pos = new POSSystem();

        // --- Notifiers (reused across transactions)
        Notifiable email = new EmailNotifier("customer@email.com");
        Notifiable sms   = new SMSNotifier("+62-812-3456-7890");
        Notifiable push  = new PushNotifier("device_token_abc123");

        System.out.println("===== Transaction 1: Cash =====");
        pos.processTransaction(
            new CashPayment(),
            List.of(email, sms),
            50000
        );

        System.out.println("\n===== Transaction 2: E-Wallet =====");
        pos.processTransaction(
            new EWalletPayment("GoPay-001", 200000),
            List.of(email, push),
            75000
        );

        System.out.println("\n===== Transaction 3: Bank Transfer =====");
        pos.processTransaction(
            new BankTransfer("BCA-1234567"),
            List.of(sms, push),
            150000
        );

        System.out.println("\n===== Transaction 4: E-Wallet FAIL =====");
        pos.processTransaction(
            new EWalletPayment("OVO-999", 10000),  // balance too low
            List.of(email),
            99000
        );

        pos.printHistory();
    }
}