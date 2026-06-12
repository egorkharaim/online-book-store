package mate.academy.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.model.order.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
    
    List<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);
}
