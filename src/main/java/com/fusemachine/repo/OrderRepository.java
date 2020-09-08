package com.fusemachine.repo;

import com.fusemachine.entity.UserOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Integer> {
    List<UserOrder> findAllByUserId(int id);
    List<UserOrder> findAllByUserIdAndScheduledAtBetween(int userId, Date startTime, Date endTime, Sort sort);
    List<UserOrder> findAllByScheduledAtBetween(Date startTime, Date endTime, Sort sort);
}
