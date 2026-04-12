public class CashPayment extends PaymentMethod {
    public CashPayment() {
        paymentType = "Cash";
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("[Cash] Accepting Rp" + amount);
        return true;
    }
}
    

