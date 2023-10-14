package com.vivah.vivah.modeltwo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

//search commit//////
@Entity
@Table(name = "user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String  userid;

	private String name;
	private String email;
	private String phoneNumber;
	private String password;
	private String confirmPassword;
	private Integer heightMin;
	private Integer heightMax;
	private Integer ageMin;
	private Integer ageMax;

	private Integer age;
private String religion;
	private String motherTongue;
	private Integer incomeRange;
//	private Double maxIncome;
	private String country;
	private String city;
	private String state;
	private String diet;
	private String education;
	private String occupation;
	@Lob
	private byte[] profilePhoto;
	private String employedIn;
	private String familyBasedOut;
	private Integer familySize;
	private String manglikStatus;
	private String activityOnSite;
	private String maritalStatus;
	private String casteGroup;
	private String subCaste;
	private boolean isVerifiedProfile;
	private boolean casteNoBar;
	private String postedBy;
	private boolean isRecentlyJoined;
	private boolean isOnline;
	private Integer viewedProfiles;
	private Integer notViewedProfiles;
	private String faceCut;
	private String bodyType;
	private String transferLocation;
	private String hobbies;
	private String habits;
	private boolean isInterestedInSettlingAbroad;
	private String personLookingFor;
	private String healthIssues;
	private Integer profileCompletionPercentage;
	private String eyesightRequirement;
	private Integer sistersCount;
	private Integer brothersCount;
	private String preferredCity;
	private String familyBackground;
	private String createProfileFor;

	private String message;

}