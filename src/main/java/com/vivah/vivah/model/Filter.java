package com.vivah.vivah.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data

public class Filter{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id ;
	    private String country;
	    private String city;
	    private String state;
	    private String diet;
	    private String education;
	    private String occupation;
	    private String religion;
	    private String motherTongue;
	    private Boolean isOnline;
	    private Boolean isVerifiedProfile;
	    private Integer heightMin;
	    private Integer heightMax;
	    private Integer ageMin;
	    private Integer ageMax;
	    private String incomeRange;
	    private String employedIn;
	    private String familyBasedOut;
	    private String familySize;
	    private String manglikStatus;
	    private String activityOnSite;
	    private String maritalStatus;
	    private String casteGroup;
	    private String subCaste;
	    private Boolean casteNoBar;
	    private String postedBy;
	    private Boolean isRecentlyJoined;
	    private String viewedProfiles;
	    private String notViewedProfiles;
	    private String faceCut;
	    private String bodyType;
	    private String transferLocation;
	    private String hobbies;
	    private String habits;
	    private Boolean isInterestedInSettlingAbroad;
	    private String personLookingFor;
	    private String healthIssues;
	    private String eyesightRequirement;
	    private Integer sistersCount;
	    private Integer brothersCount;
	    private String preferredCity;

	    // Constructors, getters, setters, and other methods can be added as needed.
	}

	
	
	
	
	


