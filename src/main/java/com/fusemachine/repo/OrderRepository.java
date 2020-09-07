package com.fusemachine.repo;

import com.fusemachine.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Integer> {
    List<UserOrder> findAllByUserId(int id);
    List<UserOrder> findAllByUserIdAndDateEquals(int userId, @Temporal(TemporalType.DATE) Date date);
    List<UserOrder> findAllByUserIdAndDateBetween(int userId, Date startTime, Date endTime);
    List<UserOrder> findAllByDateBetween(Date startTime, Date endTime);
}
