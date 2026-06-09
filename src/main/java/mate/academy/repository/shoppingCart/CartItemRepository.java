package mate.academy.repository.shoppingcart;

import java.util.Optional;
import mate.academy.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    @Query("SELECT ci FROM CartItem ci JOIN ci.shoppingCart sc"
                + " WHERE ci.id = :cartItemId AND sc.user.id = :userId")
    Optional<CartItem> findByIdAndUserId(Long cartItemId, Long userId);
}

