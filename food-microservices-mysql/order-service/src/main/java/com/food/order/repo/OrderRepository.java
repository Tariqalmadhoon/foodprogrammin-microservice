package com.food.order.repo;
import com.food.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<OrderEntity, String> {}
