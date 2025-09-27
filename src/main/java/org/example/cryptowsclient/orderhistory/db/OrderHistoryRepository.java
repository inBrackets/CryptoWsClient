package org.example.cryptowsclient.orderhistory.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, String> {

    List<OrderHistoryEntity> findByCreateTimeBetween(Long start, Long end);
}
