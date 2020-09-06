package com.fusemachine.repo;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}
