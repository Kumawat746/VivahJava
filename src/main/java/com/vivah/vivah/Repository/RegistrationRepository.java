package com.vivah.vivah.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vivah.vivah.modeltwo.Feedback;
import com.vivah.vivah.modeltwo.PhoneBook;
import com.vivah.vivah.modeltwo.User;
import com.vivah.vivah.modeltwo.Visitor;

import jakarta.transaction.Transactional;

@Repository
@EnableJpaRepositories
public interface RegistrationRepository extends CrudRepository<User, Long> {

	// register api email and phoneNumber search the data
	@Query(value = "SELECT u.email FROM User u WHERE u.email = :email", nativeQuery = true)
	String SearchMail(String email);
	
	



	
	

	@Query(value = "SELECT u.phone_Number FROM User u WHERE u.phone_Number = :phoneNumber", nativeQuery = true)
	String SearchPhoneNumber(String phoneNumber);

	// this code method for login method
	public User findByEmailAndPassword(String email, String password);

	public User findByPhoneNumberAndPassword(String phoneNumber, String password);

	// update the user data 
	@Modifying
	@Transactional
	@Query(value = "UPDATE User u SET " + "u.password = :#{#user.password}, " + "u.incomeRange = :#{#user.incomeRange}, "
			+ "u.city = :#{#user.city}, " + "u.state = :#{#user.state}, " + "u.diet = :#{#user.diet}, "
			+ "u.education = :#{#user.education}, " + "u.occupation = :#{#user.occupation}, "
			+ "u.profilePhoto = :#{#user.profilePhoto}, " + "u.employedIn = :#{#user.employedIn}, "
			+ "u.familyBasedOut = :#{#user.familyBasedOut}, " + "u.familySize = :#{#user.familySize}, "
			+ "u.manglikStatus = :#{#user.manglikStatus}, " + "u.activityOnSite = :#{#user.activityOnSite}, "
			+ "u.maritalStatus = :#{#user.maritalStatus}, " + "u.casteGroup = :#{#user.casteGroup}, "
			+ "u.subCaste = :#{#user.subCaste}, " + "u.isVerifiedProfile = :#{#user.isVerifiedProfile}, "
			+ "u.casteNoBar = :#{#user.casteNoBar}, " + "u.postedBy = :#{#user.postedBy}, "
			+ "u.isRecentlyJoined = :#{#user.isRecentlyJoined}, " + "u.isOnline = :#{#user.isOnline}, "
			+ "u.viewedProfiles = :#{#user.viewedProfiles}, " + "u.notViewedProfiles = :#{#user.notViewedProfiles}, "
			+ "u.faceCut = :#{#user.faceCut}, " + "u.bodyType = :#{#user.bodyType}, "
			+ "u.transferLocation = :#{#user.transferLocation}, " + "u.hobbies = :#{#user.hobbies}, "
			+ "u.habits = :#{#user.habits}, "
			+ "u.isInterestedInSettlingAbroad = :#{#user.isInterestedInSettlingAbroad}, "
			+ "u.personLookingFor = :#{#user.personLookingFor}, " + "u.healthIssues = :#{#user.healthIssues}, "
			+ "u.eyesightRequirement = :#{#user.eyesightRequirement}, " + "u.sistersCount = :#{#user.sistersCount}, "
			+ "u.brothersCount = :#{#user.brothersCount}, " + "u.preferredCity = :#{#user.preferredCity}, "
			+ "u.familyBackground = :#{#user.familyBackground}, " + "u.message = :#{#user.healthIssues}, "
			+ "u.familyBackground = :#{#user.familyBackground} ," + "u.religion = :#{#user.religion} ,"
			+ "u.country = :#{#user.country} ," + "u.confirmPassword = :#{#user.confirmPassword} ,"
			+ "u.motherTongue = :#{#user.motherTongue} "
			+ "WHERE u.userid = :userid")
		void updateProfile(@Param("user") User user, @Param("userid") String userid);
	
	
	// SEARCH THE ALL DATABASE  value and provide AND SHOW
	@Modifying
	@Transactional
	@Query("SELECT u FROM User u WHERE " +
			   "(:#{#criteria.userid} IS NULL OR u.userid = :#{#criteria.userid}) " 
			+ "and (:#{#criteria.country} IS NULL OR u.country = :#{#criteria.country}) "
			+ "AND (:#{#criteria.city} IS NULL OR u.city = :#{#criteria.city}) "
			+ "AND (:#{#criteria.state} IS NULL OR u.state = :#{#criteria.state}) "
			+ "AND (:#{#criteria.diet} IS NULL OR u.diet = :#{#criteria.diet}) "
			+ "AND (:#{#criteria.education} IS NULL OR u.education = :#{#criteria.education}) "
			+ "AND (:#{#criteria.occupation} IS NULL OR u.occupation = :#{#criteria.occupation}) "
			+ "AND (:#{#criteria.religion} IS NULL OR u.religion = :#{#criteria.religion}) "
			+ "AND (:#{#criteria.motherTongue} IS NULL OR u.motherTongue = :#{#criteria.motherTongue}) "
			+ "AND (:#{#criteria.isOnline} IS NULL OR u.isOnline = :#{#criteria.isOnline}) "
			+ "AND (:#{#criteria.isVerifiedProfile} IS NULL OR u.isVerifiedProfile = :#{#criteria.isVerifiedProfile}) "
			+ "AND (:#{#criteria.heightMin} IS NULL OR u.heightMin = :#{#criteria.heightMin}) "
			+ "AND (:#{#criteria.heightMax} IS NULL OR u.heightMax = :#{#criteria.heightMax}) "
			+ "AND (:#{#criteria.ageMin} IS NULL OR u.ageMin = :#{#criteria.ageMin}) "
			+ "AND (:#{#criteria.ageMax} IS NULL OR u.ageMax = :#{#criteria.ageMax}) "
			+ "AND (:#{#criteria.incomeRange} IS NULL OR u.incomeRange = :#{#criteria.incomeRange}) "
			+ "AND (:#{#criteria.profilePhoto} IS NULL OR u.profilePhoto = :#{#criteria.profilePhoto}) "
			+ "AND (:#{#criteria.employedIn} IS NULL OR u.employedIn = :#{#criteria.employedIn}) "
			+ "AND (:#{#criteria.familyBasedOut} IS NULL OR u.familyBasedOut = :#{#criteria.familyBasedOut}) "
			+ "AND (:#{#criteria.familySize} IS NULL OR u.familySize = :#{#criteria.familySize}) "
			+ "AND (:#{#criteria.manglikStatus} IS NULL OR u.manglikStatus = :#{#criteria.manglikStatus}) "
			+ "AND (:#{#criteria.activityOnSite} IS NULL OR u.activityOnSite = :#{#criteria.activityOnSite}) "
			+ "AND (:#{#criteria.maritalStatus} IS NULL OR u.maritalStatus = :#{#criteria.maritalStatus}) "
			+ "AND (:#{#criteria.casteGroup} IS NULL OR u.casteGroup = :#{#criteria.casteGroup}) "
			+ "AND (:#{#criteria.subCaste} IS NULL OR u.subCaste = :#{#criteria.subCaste}) "
			+ "AND (:#{#criteria.casteNoBar} IS NULL OR u.casteNoBar = :#{#criteria.casteNoBar}) "
			+ "AND (:#{#criteria.postedBy} IS NULL OR u.postedBy = :#{#criteria.postedBy}) "
			+ "AND (:#{#criteria.isRecentlyJoined} IS NULL OR u.isRecentlyJoined = :#{#criteria.isRecentlyJoined}) "
			+ "AND (:#{#criteria.viewedProfiles} IS NULL OR u.viewedProfiles = :#{#criteria.viewedProfiles}) "
			+ "AND (:#{#criteria.notViewedProfiles} IS NULL OR u.notViewedProfiles = :#{#criteria.notViewedProfiles}) "
			+ "AND (:#{#criteria.faceCut} IS NULL OR u.faceCut = :#{#criteria.faceCut}) "
			+ "AND (:#{#criteria.bodyType} IS NULL OR u.bodyType = :#{#criteria.bodyType}) "
			+ "AND (:#{#criteria.transferLocation} IS NULL OR u.transferLocation = :#{#criteria.transferLocation}) "
			+ "AND (:#{#criteria.hobbies} IS NULL OR u.hobbies = :#{#criteria.hobbies}) "
			+ "AND (:#{#criteria.habits} IS NULL OR u.habits = :#{#criteria.habits}) "
			+ "AND (:#{#criteria.isInterestedInSettlingAbroad} IS NULL OR u.isInterestedInSettlingAbroad = :#{#criteria.isInterestedInSettlingAbroad}) "
			+ "AND (:#{#criteria.personLookingFor} IS NULL OR u.personLookingFor = :#{#criteria.personLookingFor}) "
			+ "AND (:#{#criteria.healthIssues} IS NULL OR u.healthIssues = :#{#criteria.healthIssues}) "
			+ "AND (:#{#criteria.eyesightRequirement} IS NULL OR u.eyesightRequirement = :#{#criteria.eyesightRequirement}) "
			+ "AND (:#{#criteria.sistersCount} IS NULL OR u.sistersCount = :#{#criteria.sistersCount}) "
			+ "AND (:#{#criteria.brothersCount} IS NULL OR u.brothersCount = :#{#criteria.brothersCount}) "
			+ "AND (:#{#criteria.preferredCity} IS NULL OR u.preferredCity = :#{#criteria.preferredCity}) "
			+ "AND (:#{#criteria.familyBackground} IS NULL OR u.familyBackground = :#{#criteria.familyBackground})")
	List<User> searchByCriteria(@Param("criteria") User criteria);

// ACCEPTE  MAPPING INSERT THE DATA
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_mapping (user_id, accepted_mem) VALUES (:userId, :accepted_mem)", nativeQuery = true)
	void acceptMem(@Param("userId") String userId, @Param("accepted_mem") int accepted_mem);

//DECLINE MAPPING INSERT THE DATA
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_mapping (user_id, declined_mem) VALUES (:userId, :declined_Mem)", nativeQuery = true)
	void declinedMem(@Param("userId") String userId, @Param("declined_Mem") int declined_Mem);

// SHORT MAPPING INSERT THE DATA
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_mapping (user_id, shortlisted_mem) VALUES (:userId, :shortlisted_mem)", nativeQuery = true)
	void shortlistedmem(@Param("userId") String userId, @Param("shortlisted_mem") int shortlisted_mem);

//SEND  MAPPING INSERT THE DATA
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_mapping (user_id, send_mem) VALUES (:userId, :send_mem)", nativeQuery = true)
	void sendmem(@Param("userId") String userId, @Param("send_mem") int send_mem);

// RECEIVED MAPPING INSERT THE DATA
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_mapping (user_id, received_mem) VALUES (:userId, :received_mem)", nativeQuery = true)
	void receivedmem(@Param("userId") String userId, @Param("received_mem") int received_mem);

//BLOCKED MAPPING INSERT THE DATA
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO user_mapping (user_id, blocked_mem) VALUES (:userId, :blocked_mem)", nativeQuery = true)
	void blockedmem(@Param("userId") String userId, @Param("blocked_mem") int blocked_mem);

//ACCEPTED MEMBER ALL LIST FIND 
	@Query(value = "SELECT ac.accepted_mem FROM user_mapping ac WHERE ac.user_id IN (:userid)", nativeQuery = true)
	List<Long> acceptedIdList(@Param("userid") String userid);

	@Query(value = "SELECT * FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
	List<User> allAccepted(@Param("allids") List<Long> allids);

// DECLINED MEMBER ALL LIST FIND 
	@Query(value = "SELECT ac.declined_mem FROM user_mapping ac WHERE ac.user_id IN (:userid)", nativeQuery = true)
	List<Long> declineIdList(@Param("userid") String userid);

	@Query(value = "SELECT * FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
	List<User> alldeclined(@Param("allids") List<Long> allids);

//SHORTLISTED MEMBER ALL LIST FIND 
	@Query(value = "SELECT ac.shortlisted_mem FROM user_mapping ac WHERE ac.user_id IN (:userid)", nativeQuery = true)
	List<Long> shortIdList(@Param("userid") String userid);

	@Query(value = "SELECT * FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
	List<User> allshortlisted(@Param("allids") List<Long> allids);

//SEND MEMBER ALL LIST FIND
	@Query(value = "SELECT ac.send_mem FROM user_mapping ac WHERE ac.user_id IN (:userid)", nativeQuery = true)
	List<Long> sendIdList(@Param("userid") String userid);

	@Query(value = "SELECT * FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
	List<User> allsendlisted(@Param("allids") List<Long> allids);

//RECEIVE MEMBER ALL LIST FIND
	@Query(value = "SELECT ac.received_mem FROM user_mapping ac WHERE ac.user_id IN (:userid)", nativeQuery = true)
	List<Long> receivedIdList(@Param("userid") String userid);

	@Query(value = "SELECT * FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
	List<User> allreceivedlisted(@Param("allids") List<Long> allids);

// BLOCKED MEMBER ALL LIST FIND
	@Query(value = "SELECT ac.blocked_mem FROM user_mapping ac WHERE ac.user_id IN (:userid)", nativeQuery = true)
	List<Long> blockedIdList(@Param("userid") String userid);

	@Query(value = "SELECT * FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
	List<User> allblockedlisted(@Param("allids") List<Long> allids);

	User getUserByName(String name);

//		@Query(value = "SELECT u.name,u.city FROM user u WHERE u.id IN (:allids)", nativeQuery = true)
//		List<UserMapping> allaccepted(@Param("allids") List<Integer> allids);	

	Visitor save(Visitor visitor);

// create the feedback user save the data
	Feedback save(Feedback feedback);

	// feedback list find and post order by descending order

	@Query("SELECT f FROM Feedback f ORDER BY f.rating DESC")
	List<Feedback> findAllByRatingDescending();


	User getUserByEmail(String email);
	
	
   //phonebook list find
	@Query(nativeQuery = true, value = "SELECT userid, name, phone_number FROM user")
	List<Object[]> PhonebookList();

	   // according to the userid phonebook   find
	 @Query("SELECT u.phoneNumber , u.name FROM User u WHERE u.userid = :userid")
	    String findPhoneNumberByUserid(@Param("userid") String userid);


	@Query(value = "SELECT * FROM user u WHERE u.userid IN (:idList)", nativeQuery = true)

	List<User> getUsersByIds(List<Integer> idList);




	@Query(value = "SELECT * FROM user u WHERE u.userid IN (:userid)", nativeQuery = true)

	Optional<User> getUserByUserid(String userid);
	
	@Query(value = "SELECT u.userid FROM user u ORDER BY u.userid DESC LIMIT 1", nativeQuery = true)
	String getLastUserId();
	
	
	
	@Transactional
	void deleteByUserid(String userid);





}