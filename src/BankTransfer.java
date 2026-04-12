public class BankTransfer extends PaymentMethod {
    private String accountNo;

    public BankTransfer(String accountNo) {
        paymentType = "Bank Transfer";
        this.accountNo = accountNo;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("[Bank Transfer] Rp" + amount + " → account " + accountNo);
        return true;
    }
}