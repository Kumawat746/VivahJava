package com.vivah.vivah.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vivah.vivah.modeltwo.ShowProfile;

@Repository
@EnableJpaRepositories
public interface SettingRepository  extends CrudRepository<ShowProfile,Long>{

	Optional<ShowProfile> findByUserId(String userId);

//	ShowProfile save(ShowProfile showProfile);
	
	
	
	// find tha all list and show the descending order query 

	
	

	
	

}
