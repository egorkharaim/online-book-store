package mate.academy.repository.shoppingCart;

import java.util.Optional;
import mate.academy.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long> {
   @Query("SELECT sc FROM ShoppingCart sc " +
           "LEFT JOIN FETCH sc.cartItems ci " +
           "LEFT JOIN FETCH ci.book " +
           "WHERE sc.user.id = :userId")
    Optional<ShoppingCart> findByUserId(Long userId);
}
