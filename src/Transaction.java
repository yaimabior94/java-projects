public class Transaction {
    private static int counter = 1000;
    private final String txnId;
    private final double amount;
    private final String paymentType;
    private String status;

    public Transaction(double amount, String paymentType) {
        this.txnId = "TXN-" + (++counter);
        this.amount = amount;
        this.paymentType = paymentType;
        this.status = "PENDING";
    }

    public void setStatus(String status) { this.status = status; }
    public String getTxnId()       { return txnId; }
    public double getAmount()      { return amount; }
    public String getPaymentType() { return paymentType; }
    public String getStatus()      { return status; }

    @Override
    public String toString() {
        return txnId + " | " + paymentType + " | Rp" + amount + " | " + status;
    }
}
