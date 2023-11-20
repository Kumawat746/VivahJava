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
import com.vivah.vivah.Repository.RepositorytwoRepo;
import com.vivah.vivah.Repository.SettingRepository;
import com.vivah.vivah.model.Delete;
import com.vivah.vivah.model.Registration;
import com.vivah.vivah.model.ShowProfile;
import com.vivah.vivah.model.User;

@RestController
@RequestMapping("/api")
public class SettingController {

	@Autowired
	private RegistrationRepository repo;

	@Autowired
	private SettingRepository settingRepo;

	@Autowired
	private RepositorytwoRepo regtwo;

	// change password after email , password match then newPassword create
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("email") String userEmail,
			@RequestParam("newPassword") String newPassword, @RequestParam("oldPassword") String oldPassword) {
		// Print old and new passwords for debugging purposes
		System.out.println("OLD PASSWORD: " + oldPassword);
		System.out.println("NEW PASSWORD: " + newPassword);

		// Assuming there's a method to get the user by email from the repository
		Registration currentUser = this.regtwo.getUserByEmail(userEmail);

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
			Optional<Registration> optionalUser = regtwo.findByUserId(userId);

			if (optionalUser.isPresent()) {
				// User found, get user details
				Registration reg = optionalUser.get();

				// Save show profile details for the hidden user
				ShowProfile showProfile = new ShowProfile();
				showProfile.setUserId(userId);
				showProfile.setUserName(reg.getUsername()); // Assuming User has a getUsername() method
				showProfile.setProfileVisible(true);
				showProfile.setHiddenUntil(LocalDate.now().plusDays(days)); // Set the hiddenUntil date
				showProfile.setLastHideDate(LocalDateTime.now()); // Record the date when hidden
				settingRepo.save(showProfile);

				// Update the registration's profile visibility in the main repository
				reg.setProfileVisible(true); // Update profileVisible field

				regtwo.save(reg);
				// Return a response indicating successful profile hiding
				return ResponseEntity.ok("Profile hidden successfully for " + days + " days");
			} else {
				// User not found, return a not found response
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			}
		}
	}

	// Endpoint to unhide a registration's profile
	@PostMapping("/unhide")
	public ResponseEntity<String> unhideProfile(@RequestParam String userId) {
		return settingRepo.findByUserId(userId).map(profile -> {
			// Set the profile visibility to true and reset the hiddenUntil field
			profile.setProfileVisible(true);
			profile.setHiddenUntil(null);
			profile.setLastUnhideDate(LocalDateTime.now());

			// Save the updated profile entity to the settingRepo
			settingRepo.save(profile);

			// Find the user by userId in the main repository
			regtwo.findByUserId(userId).ifPresent(user -> {
				// Update the user's profile visibility in the main repository
				user.setProfileVisible(true);
				regtwo.save(user);
			});

			// Delete the profile entry from the settingRepo (optional, depending on your
			// requirements)
			settingRepo.delete(profile);

			// Return a response indicating successful profile unhiding
			return ResponseEntity.ok("Profile unhid successfully");
		}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found"));
	}

	@PostMapping("/hidePhoneNumber")
	public ResponseEntity<String> hidePhoneNumber(@RequestParam String userId, @RequestParam int hideStage) {
		// Find the user by userId
		Optional<Registration> optionalUser = regtwo.findByUserId(userId);

		if (optionalUser.isPresent()) {
			// User found, get user details
			Registration reg = optionalUser.get();

			switch (hideStage) {
			case 1:
				// Stage 1: Hide phone number for all members
				reg.setHidePhoneNumber(0);
				regtwo.save(reg);
				return ResponseEntity.ok("Phone number hidden for all members");

			case 2:
				// Stage 2: Show phone number only for interested and accepted members
				reg.setHidePhoneNumber(1);
				regtwo.save(reg);

				// Additional logic for case 2
				// You may want to save specific data for case 2 in your user entity
				// For example, user.setSomeField(value);
				// repo.save(user);

				return ResponseEntity.ok("Phone number shown only for interested and accepted members");

			case 3:
				// Stage 3: Unhide phone number for all members
				reg.setHidePhoneNumber(2);
				regtwo.save(reg);
				return ResponseEntity.ok("Phone number visible for all members");

			default:
				return ResponseEntity.badRequest().body("Invalid hide stage");
			}
		} else {
			// User not found, return a not found response
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}

	@PostMapping("/profileVisibility")
	public ResponseEntity<String> profileVisibility(@RequestParam String userId, @RequestParam int ProfileVisibility) {
		// Find the user by userId
		Optional<Registration> optionalUser = regtwo.findByUserId(userId);

		if (optionalUser.isPresent()) {
			// User found, get user details
			Registration reg = optionalUser.get();

			switch (ProfileVisibility) {
			case 1:
				// Stage 1: Set album privacy for all members
				reg.setProfileVisibility(0);
				regtwo.save(reg);
				return ResponseEntity.ok("Profile Visibility hide to all members");

			case 2:
				// Stage 2: Set album privacy for paid members and send and accepted members
				reg.setProfileVisibility(1);
				regtwo.save(reg);

				// Additional logic for case 2
				// You may want to save specific data for case 2 in your user entity
				// For example, user.setSomeField(value);
				// repo.save(user);

				return ResponseEntity.ok("Profile visible  to fit My criteria");

			case 3:
				// Stage 3: Set album privacy for paid members only
				reg.setProfileVisibility(2);
				regtwo.save(reg);
				return ResponseEntity.ok("Profile visible to all member");

			default:
				return ResponseEntity.badRequest().body("Invalid privacy stage");
			}
		} else {
			// User not found, return a not found response
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}

	@PostMapping("/albumPrivacy")
	public ResponseEntity<String> createAlbumPrivacy(@RequestParam String userId, @RequestParam int privacyStage) {
		// Find the user by userId
		Optional<Registration> optionalUser = regtwo.findByUserId(userId);

		if (optionalUser.isPresent()) {
			// User found, get user details
			Registration reg = optionalUser.get();

			switch (privacyStage) {
			case 1:
				// Stage 1: Set album privacy for all members
				reg.setAlbumPrivacy(0);
				regtwo.save(reg);
				return ResponseEntity.ok("Album hide to all members");

			case 2:
				// Stage 2: Set album privacy for paid members and send and accepted members
				reg.setAlbumPrivacy(2);
				regtwo.save(reg);

				// Additional logic for case 2
				// You may want to save specific data for case 2 in your user entity
				// For example, user.setSomeField(value);
				// repo.save(user);

				return ResponseEntity.ok("Album visible to send/accepted members");

			case 3:
				// Stage 3: Set album privacy for paid members only
				reg.setAlbumPrivacy(3);
				regtwo.save(reg);
				return ResponseEntity.ok("Album visible to paid members only");

			default:
				return ResponseEntity.badRequest().body("Invalid privacy stage");
			}
		} else {
			// User not found, return a not found response
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}

}
