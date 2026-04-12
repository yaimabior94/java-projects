public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String walletId) {
        super("Insufficient balance in wallet: " + walletId);
    }
}