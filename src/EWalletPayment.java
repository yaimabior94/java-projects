public class EWalletPayment extends PaymentMethod {
    private String walletId;
    private double balance;

    public EWalletPayment(String walletId, double balance) {
        paymentType = "E-Wallet";
        this.walletId = walletId;
        this.balance = balance;
    }

    @Override
    public boolean processPayment(double amount) {
        if (balance < amount) {
            throw new InsufficientBalanceException(walletId);
        }
        balance -= amount;
        System.out.println("[E-Wallet] " + walletId + " charged Rp" + amount);
        return true;
    }
}