package com.vivah.vivah.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vivah.vivah.modeltwo.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

	Optional<Report> findByUserIdAndReportUserId(String userId, String reportUserId);
	
	

	
	
}
