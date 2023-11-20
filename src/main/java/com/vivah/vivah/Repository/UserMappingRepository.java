package com.vivah.vivah.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vivah.vivah.model.UserMapping;


@Repository
public interface UserMappingRepository extends CrudRepository < UserMapping , Long> {

	

}
