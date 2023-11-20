package com.vivah.vivah.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//search commit

@Entity
@Table(name = "PartnerPreference")
@Data
public class PartnerPreference {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userid;
    private Integer heightMin;
    private Integer heightMax;
    private Integer ageMin;
    private Integer ageMax;
    private String religion;
    private String motherTongue;
    private Integer incomeRange;
    private String country;
    private String city;
    private String state;
    private String diet;
    private String education;
    private String occupation;
    private String employedIn;
    private String familyBasedOut;
    private Integer familySize;
    private String manglikStatus;
    private String maritalStatus;
    private String casteGroup;
    private String subCaste;
    private Integer casteNoBar;
    private String postedBy;
    private String faceCut;
    private String bodyType;
    private String transferLocation;
    private String hobbies;
    private String habits;
    private Integer isInterestedInSettlingAbroad;
    private String personLookingFor;
    private String healthIssues;
    private Integer profileCompletionPercentage;
    private String eyesightRequirement;
    private Integer sistersCount;
    private Integer brothersCount;
    private String preferredCity;
    private String familyBackground;

    private String profileHandleBY;

    // Getters and setters for the fields (omitted for brevity)
}
