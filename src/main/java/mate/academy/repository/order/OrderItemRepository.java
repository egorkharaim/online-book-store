package mate.academy.repository.order;

import java.util.Optional;
import mate.academy.model.order.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);

    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id, Long orderId, Long userId);

    Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);
}
