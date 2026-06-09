package mate.academy.exception;

public class ShoppingCartDoesNotBelongToUserException extends RuntimeException {
    public ShoppingCartDoesNotBelongToUserException(String message) {
        super(message);
    }
}
