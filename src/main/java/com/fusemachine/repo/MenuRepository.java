package com.fusemachine.repo;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findByDateEquals(Date date);
}
