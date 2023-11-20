package com.vivah.vivah.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vivah.vivah.model.PartnerPreference;

public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {

	// Custom queries for fetching partner preferences
	@Query(value = "delete from PartnerPreference where userid in ( :userId)", nativeQuery = true)
	void deletePartnerP(@Param("userId") String userId);

	@Query(value = "select * from partner_preference where userid in ( :userId)", nativeQuery = true)
	List<PartnerPreference> getUserPreference(String userId);

}