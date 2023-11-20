package com.vivah.vivah.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vivah.vivah.model.Feedback;

@Repository
@EnableJpaRepositories
public interface FeedbackRepository  extends CrudRepository<Feedback,Long>{
	
	
	
	// find tha all list and show the descending order query 
	
    @Query("SELECT f FROM Feedback f ORDER BY f.rating DESC")
    List<Feedback> findAllByRatingDescending();

	
	

	
	

}
