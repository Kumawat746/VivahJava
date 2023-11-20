package com.vivah.vivah.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String userId;

	private String username;

	private String email;
	private String phoneNumber;

	private boolean profileVisible;
	private Integer hidePhoneNumber = 0;
	private Integer albumPrivacy = 0;
	private Integer profileVisibility = 0;
	private LocalDate joinDate;

	private String religion;
	private String motherTongue;
	private Integer incomeRange;
	private String country;
	private String city;
	private String state;
	private String diet;
	private String education;

	private Integer heightMin;
	private Integer heightMax;
	private Integer ageMin;
	private Integer ageMax;
	private Integer age;
	private Integer height;

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
	private int matchScore;

	private String message;

}