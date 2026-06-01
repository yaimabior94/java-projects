public abstract class PaymentMethod {
    protected String paymentType;

    public abstract boolean processPayment(double amount);

    public String getPaymentType() {
        return paymentType;
    }
}