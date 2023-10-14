package com.vivah.vivah.Repository;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vivah.vivah.modeltwo.MyOrder;


@Repository
@EnableJpaRepositories

public interface OrderRepository extends CrudRepository<MyOrder,Long>{
	
	
	
public MyOrder findByOrderId(String orderId);
}
