package com.fusemachine.repo;

import com.fusemachine.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RequestItemRepository extends JpaRepository<UserRequest, Integer> {
    UserRequest findByUserIdAndDateEquals(int id, Date date);
    List<UserRequest> findAllByDateEquals(Date date);

    @Query(value = "select reqItem.food_id from user_request r INNER join request_item reqItem ON r.id = reqItem.request_id where r.date = :myDate group by reqItem.food_id order by count(reqItem.request_id) desc", nativeQuery = true)
    List<Integer> findAllRequestedFoodByDateEquals(@Param("myDate") Date myDate);
}
