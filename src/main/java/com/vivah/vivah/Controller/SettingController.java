package com.vivah.vivah.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vivah.vivah.Repository.RegistrationRepository;
import com.vivah.vivah.Repository.SettingRepository;
import com.vivah.vivah.modeltwo.Delete;
import com.vivah.vivah.modeltwo.ShowProfile;
import com.vivah.vivah.modeltwo.User;

@RestController
@RequestMapping("/api")
public class SettingController {

	@Autowired
	private RegistrationRepository repo;

	@Autowired
	private SettingRepository settingRepo;

	//change password after email , password match then newPassword create
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("email") String userEmail,
			@RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword) {
		// Print old and new passwords for debugging purposes
		System.out.println("OLD PASSWORD: " + oldPassword);
		System.out.println("NEW PASSWORD: " + newPassword);

		// Assuming there's a method to get the user by email from the repository
		User currentUser = this.repo.getUserByEmail(userEmail);

		if (currentUser != null && oldPassword.equals(currentUser.getPassword())) {
			// Old password matches the current password for the user

			// Implement logic to update the password
			currentUser.setPassword(newPassword);
			currentUser.setConfirmPassword(newPassword);

			// Save the updated user entity to the repository
			this.repo.save(currentUser);

			// Return a success message
			return "Password changed successfully";
		} else {
			// Old password doesn't match or user not found
			return "Failed to change password. Please check your old password and try again.";
		}
	}

//	 DELETE THE DATA BY ID
	@PostMapping("/delete")
	public String deleteUserByUserId(@RequestParam("userId") String userId) {
		// Assuming repo.findById returns the user entity
		Optional<User> optionalUser = repo.findByUserId(userId);

		if (optionalUser.isPresent()) {
			User deletedUser = optionalUser.get();

			// Save the user information to another table
			saveToDeleteTable(deletedUser);

			// Delete the user from the original table
			repo.deleteByUserId(userId);

			String message = "User with ID " + userId + " has been deleted";
			return message;
		} else {
			// Handle the case where the user with the given ID was not found
			return "User with ID " + userId + " not found";
		}
	}

	private void saveToDeleteTable(User deletedUser) {
		// Assuming AnotherEntity is the entity for the new table
		Delete obj = new Delete();
		obj.setUserId(deletedUser.getUserId());
		obj.setName(deletedUser.getName());
		obj.setEmail(deletedUser.getEmail());
		obj.setPhoneNumber(deletedUser.getPhoneNumber());

		// Save to another table
		repo.save(obj);
	}

	// Endpoint to hide a user's profile for a specified number of days
	@PostMapping("/hide")
	public ResponseEntity<String> hideProfile(@RequestParam String userId, @RequestParam Long days) {
		// Check if the user's profile is already hidden
		Optional<ShowProfile> optionalShowProfile = settingRepo.findByUserId(userId);

		if (optionalShowProfile.isPresent()) {
			// User's profile is already hidden, return a message
			ShowProfile showProfile = optionalShowProfile.get();
			LocalDate hiddenUntil = showProfile.getHiddenUntil();

			// Return a response indicating that the profile is already hidden
			return ResponseEntity.ok("Profile for user " + userId + " is already hidden until " + hiddenUntil);
		} else {
			// Find the user by userId
			Optional<User> optionalUser = repo.findByUserId(userId);

			if (optionalUser.isPresent()) {
				// User found, get user details
				User user = optionalUser.get();

				// Save show profile details for the hidden user
				ShowProfile showProfile = new ShowProfile();
				showProfile.setUserId(userId);
				showProfile.setName(user.getName()); // Assuming User has a getUsername() method
				showProfile.setProfileVisible(false);
				showProfile.setHiddenUntil(LocalDate.now().plusDays(days)); // Set the hiddenUntil date
				showProfile.setLastHideDate(LocalDateTime.now()); // Record the date when hidden
				settingRepo.save(showProfile);

				// Update the user's profile visibility in the main repository
				user.setProfileVisible(false);
				repo.save(user);

				// Return a response indicating successful profile hiding
				return ResponseEntity.ok("Profile hidden successfully for " + days + " days");
			} else {
				// User not found, return a not found response
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
		}
	}

	// UNHIDE YOUR PROFILE FOR AFETR CREATE HIDE
	@PostMapping("/unhide")
	public ResponseEntity<String> unhideProfile(@RequestParam String userId) {
		// Check if the user's profile entry exists in the ShowProfile table
		Optional<ShowProfile> optionalProfile = settingRepo.findByUserId(userId);
		if (optionalProfile.isPresent()) {
			// User's profile found in the ShowProfile table
			ShowProfile profile = optionalProfile.get();

			// Set the profile visibility to true and reset the hiddenUntil field
			profile.setProfileVisible(true);
			profile.setHiddenUntil(null);

			// Save additional details, e.g., log the unhide operation and update the last
			// unhide date
			profile.setLastUnhideDate(LocalDateTime.now());
			settingRepo.save(profile);

			// Delete the profile entry from the settingRepo (optional, depending on your
			// requirements)
			settingRepo.delete(profile);

			// Return a response indicating successful profile unhiding
			return ResponseEntity.ok("Profile unhid successfully");
		} else {
			// User's profile not found, return a not found response
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
		}
	}

}
