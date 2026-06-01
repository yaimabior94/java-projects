import java.util.ArrayList;
import java.util.List;

public class POSSystem {
    private List<Transaction> history = new ArrayList<>();

    public Transaction processTransaction(
            PaymentMethod payment,
            List<Notifiable> notifiers,
            double amount) {

        Transaction txn = new Transaction(amount, payment.getPaymentType());

        try {
            boolean ok = payment.processPayment(amount);
            txn.setStatus(ok ? "SUCCESS" : "FAILED");

            String msg = "Payment of Rp" + amount
                + " via " + payment.getPaymentType()
                + " — " + txn.getStatus()
                + " [" + txn.getTxnId() + "]";

            for (Notifiable n : notifiers) {
                n.sendNotification(msg);
            }

        } catch (InsufficientBalanceException e) {
            txn.setStatus("FAILED");
            System.out.println("  ERROR: " + e.getMessage());
        }

        history.add(txn);
        return txn;
    }

    public void printHistory() {
        System.out.println("\n===== Transaction History =====");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }
}