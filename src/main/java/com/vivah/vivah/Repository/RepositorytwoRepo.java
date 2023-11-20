package com.vivah.vivah.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.vivah.vivah.model.Registration;
import com.vivah.vivah.model.User;

public interface RepositorytwoRepo extends CrudRepository<Registration, Long> {

//	@Query(value = "SELECT u.email FROM registration u WHERE u.email = :email", nativeQuery = true)
//	String SearchMail(String email);

	public Registration findByEmailAndPassword(String email, String password);

	Registration findByPhoneNumberAndPassword(String phoneNumber, String password);

	// register api email and phoneNumber search the data
	@Query(value = "SELECT u.email FROM registration u WHERE u.email = :email", nativeQuery = true)
	String SearchMail(String email);

	@Query(value = "SELECT u.user_id FROM registration u ORDER BY u.user_id DESC LIMIT 1", nativeQuery = true)
	String getLastUserId();

	@Query(value = "SELECT u.phone_Number FROM registration u WHERE u.phone_Number = :phoneNumber", nativeQuery = true)
	String SearchPhoneNumber(String phoneNumber);

	// recently_joined find data joinDate is greater than or equal to the specified
	// startDate
	@Query("SELECT r FROM Registration r WHERE r.joinDate = :joinDate")
	List<Registration> findRecentlyJoinedUsers(@Param("joinDate") LocalDate joinDate);

	public Optional<Registration> findByUserId(String userId);

	// change password for profile only
	Registration getUserByEmail(String userEmail);

	Registration findByPhoneNumber(String phoneNumber);

}
