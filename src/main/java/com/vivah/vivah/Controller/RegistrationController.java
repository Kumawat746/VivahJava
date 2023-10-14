package com.vivah.vivah.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.security.SecureRandom;
import java.util.Comparator;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.razorpay.*;
import com.vivah.vivah.Repository.FeedbackRepository;
import com.vivah.vivah.Repository.OrderRepository;
import com.vivah.vivah.Repository.RegistrationRepository;
import com.vivah.vivah.modeltwo.Feedback;
import com.vivah.vivah.modeltwo.MyOrder;
import com.vivah.vivah.modeltwo.PhoneBook;
import com.vivah.vivah.modeltwo.User;
import com.vivah.vivah.modeltwo.Visitor;
import com.vivah.vivah.servicetwo.RegistrationService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class RegistrationController {

	@Autowired
	private RegistrationRepository repo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private FeedbackRepository feedbackrepo;

	@Autowired
	private RegistrationService registrationService;

	String message;

	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@RequestBody User user) {
		String tempEmail = user.getEmail();
		String tempPhoneNUmber = user.getPhoneNumber();

		String isEmailExists = repo.SearchMail(tempEmail);
		String isNumberExists = repo.SearchPhoneNumber(tempPhoneNUmber);
		System.out.println(tempEmail);

		if (isEmailExists != null && isNumberExists != null) {
			// Both email and phone number already exist, send a combined message with
			// HttpStatus.CONFLICT (409).
			String message = "Email and phone number already exist";
			return new ResponseEntity<>(message, HttpStatus.CONFLICT);
		} else if (isEmailExists != null) {
			// Email already exists, send a message with HttpStatus.CONFLICT (409).
			String emailMessage = "Email already exists";
			return new ResponseEntity<>(emailMessage, HttpStatus.CONFLICT);
		} else if (isNumberExists != null) {
			// Phone number already exists, send a message with HttpStatus.CONFLICT (409).
			String phoneNumberMessage = "Phone number already exists";
			return new ResponseEntity<>(phoneNumberMessage, HttpStatus.CONFLICT);
		}

//	     long timestamp = System.currentTimeMillis();
		String lastUserId = repo.getLastUserId();

		if (lastUserId != null) {
			String prefix = lastUserId.substring(0, 2); // Extract the prefix "vv"
			String numericPart = lastUserId.substring(2); // Extract the numeric part "12345"

			// Convert the numeric part to an integer and increment it by 1
			int numericValue = Integer.parseInt(numericPart) + 1;

			// Format the incremented numeric value as a string with leading zeros if needed

			// Combine the prefix and the incremented numeric part to form the newUserid
			String newUserid = prefix + numericValue;

			System.out.println(newUserid); // Output: vv1001 (if lastUserId was "vv1000")

			user.setUserid(newUserid);
		} else {
			// Handle the case where lastUserId is null
			System.out.println("lastUserId is null. Handle the null case here.");

			// Generate a unique user ID starting with "vv" and a predefined number (e.g.,
			// "1000")
			String newUserid = "vv" + "1000";

			// Set the generated unique user ID for the user object
			user.setUserid(newUserid);

			System.out.println(newUserid); // Output: vv1000 (if lastUserId was null)
		}

		// Save the user object to the repository
		User userObj = repo.save(user);
		return new ResponseEntity<>(" Your registration successfully", HttpStatus.OK);

	}

	// LOGIN API LOGIN THE DATA EMAIL AND PHONENUMBERR AND PASSWORD
	@PostMapping("/login")
	public ResponseEntity<Object> loginUser(@RequestBody User user) {
		String email = user.getEmail(); // Assuming emailOrPhoneNumber field contains either email or mobile number
		String password = user.getPassword();

		User userData = null;

		if (email.contains("@")) {
			// Try to find a user by email
			userData = repo.findByEmailAndPassword(email, password);
		} else {
			// Try to find a user by phone number
			userData = repo.findByPhoneNumberAndPassword(email, password);
		}

		if (userData != null) {
			// User found, and password matches
			return new ResponseEntity<>(userData, HttpStatus.OK);
		} else {
			// No user found or password doesn't match for both email and mobile number
			String message = "Invalid email/mobile number or password";
			return new ResponseEntity<>(message, HttpStatus.CONFLICT);
		}
	}

	// FIND THE ALL USER DATA
	@PostMapping("/getAlluser")
	@ResponseBody
	public List<User> getAllUser() {
		return (List<User>) repo.findAll();
	}

	// FIND THE DATA ONE BY ONE
	@PostMapping("/getUser")
	public ResponseEntity<Object> getUserById(@RequestParam("userid") String userid) {

		Optional<User> user = repo.getUserByUserid(userid);

		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

	}

	// DELETE THE DATA BY ID
	public String deleteUserById(@RequestParam("userid") String userid) {
		String message = "User with ID " + userid + " has been deleted";
		repo.deleteByUserid(userid);
		return message;
	}

	// UPDATE THE DATA
	@PostMapping("/updateUser")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		String userId = user.getUserid();
		repo.updateProfile(user, userId);

		return ResponseEntity.ok(user);
	}

	// FIND THE DATA BY USER INFORMATION
	@PostMapping("/search")
	public List<User> Search(@RequestBody User user)

	{

		// Call the repository method to perform the search
		List<User> searchResult = repo.searchByCriteria(user);

		// Return the search result
		return searchResult;
	}

	// ACCEPTED MEMBER INSERT THE DATA
	@PostMapping("/accept")
	public void accept(@RequestParam String userid, @RequestParam int accepted_mem) {
		repo.acceptMem(userid, accepted_mem);
	}

	// DECLINED MEMBER INSERT THE DATA
	@PostMapping("/decline")
	public void decline(@RequestParam String userid, @RequestParam int declined_mem) {
		repo.declinedMem(userid, declined_mem);
	}

	// SHORTLIST MEMBER INSERT THE DATA
	@PostMapping("/shortlist")
	public void shortlisted(@RequestParam String userid, @RequestParam int shortlisted_mem) {
		repo.shortlistedmem(userid, shortlisted_mem);
	}

	// SEND MEMBER INSERT THE DATA
	@PostMapping("/send")
	public void send(@RequestParam String userid, @RequestParam int send_mem) {
		repo.sendmem(userid, send_mem);
	}

	// RECEIVED MEMBER INSERT THE DATA
	@PostMapping("/receive")
	public void received(@RequestParam String userid, @RequestParam int received_mem) {
		repo.receivedmem(userid, received_mem);
	}

	// BLOCKED MEMBER INSERT THE DATA
	@PostMapping("/blocked")
	public void blocked(@RequestParam String userid, @RequestParam int blocked_mem) {
		repo.blockedmem(userid, blocked_mem);
	}

	// ACCEPTELIST MEMBER ALL LIST FIND
	@PostMapping("/accepteList")
	public ResponseEntity<List<User>> accepted(@RequestParam String userid) {

		// Fetch accepted user IDs based on the provided userId
		List<Long> allids = repo.acceptedIdList(userid);

		// Remove any null values from the list (if any)
		allids.removeIf(id -> id == null);

		// Fetch users based on the accepted IDs
		List<User> userList = repo.allAccepted(allids);

		// Return the list of accepted users as a ResponseEntity
		return ResponseEntity.ok(userList);

	}

	// DECLINELIST MEMBER ALL LIST FIND
	@PostMapping("/declineList")
	public ResponseEntity<List<User>> declined(@RequestParam String userid) {
		List<Long> allids = repo.declineIdList(userid);
		allids.removeIf(id -> id == null);
		List<User> userList = repo.alldeclined(allids);
		return ResponseEntity.ok(userList);
	}

	// SHORTLISTED MEMBER ALL LIST FIND
	@PostMapping("/shortList")
	public ResponseEntity<List<User>> shortlist(@RequestParam String userid) {
		List<Long> allids = repo.shortIdList(userid);
		allids.removeIf(id -> id == null);
		List<User> userList = repo.allshortlisted(allids);
		return ResponseEntity.ok(userList);
	}

	// SENDLISTED MEMBER ALL LIST FIND
	@PostMapping("/sendList")
	public ResponseEntity<List<User>> sendlist(@RequestParam String userid) {
		List<Long> allids = repo.sendIdList(userid);
		allids.removeIf(id -> id == null);
		List<User> userList = repo.allsendlisted(allids);
		return ResponseEntity.ok(userList);
	}

	// RECEIVE MEMBER ALL LIST FIND
	@PostMapping("/receivdtList")
	public ResponseEntity<List<User>> receivedlist(@RequestParam String userid) {
		List<Long> allids = repo.receivedIdList(userid);
		allids.removeIf(id -> id == null);
		List<User> userList = repo.allreceivedlisted(allids);
		return ResponseEntity.ok(userList);
	}

	// BLOCKED MEMBER ALL LIST FIND
	@PostMapping("/blockList")
	public ResponseEntity<List<User>> blockedlist(@RequestParam String userid) {
		List<Long> allids = repo.blockedIdList(userid);
		allids.removeIf(id -> id == null);
		List<User> userList = repo.allblockedlisted(allids);
		return ResponseEntity.ok(userList);
	}

	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data, User user) {
		System.out.println(data);

		int amt = Integer.parseInt(data.get("amount").toString());
		String orderResponse = null;

		try {
			var client = new RazorpayClient("rzp_test_oGRI5TqiOtsobA", "Hp1QRAo7ylzuoZ5wjwPPD8je");

			// Create an order request
			JSONObject obj = new JSONObject();
			obj.put("amount", amt * 100); // Amount in paise
			obj.put("currency", "INR");
			obj.put("receipt", "txn_7364767262");

			// Create the new order
			Order order = client.orders.create(obj);
			System.out.println(order);

			// Check if order is not null before using it
			if (order != null) {
				// Save the order in the database
				MyOrder myOrder = new MyOrder();
				myOrder.setAmount(order.get("amount") + " ");
				myOrder.setOrderId(order.get("id"));
				myOrder.setPaymentId(null);
				myOrder.setReceipt(order.get("receipt"));
				myOrder.setStatus("created");
				myOrder.setUser(this.repo.getUserByName(user.getName()));

				this.orderRepo.save(myOrder);

				orderResponse = order.toString();
			} else {
				// Handle the case where order is null
				orderResponse = "Failed to create order"; // Provide a meaningful error message
			}

		} catch (RazorpayException e) {
			e.printStackTrace();
			// Handle RazorpayException - provide meaningful error messages or log the
			// exception
			orderResponse = "Failed to create order. RazorpayException: " + e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			// Handle other exceptions - provide meaningful error messages or log the
			// exception
			orderResponse = "Failed to create order. Exception: " + e.getMessage();
		}

		return orderResponse;
	}

	@PostMapping("/update_order")
	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
		try {
			String orderId = (String) data.get("order_id");
			String paymentId = (String) data.get("payment_id");
			String status = (String) data.get("status");

			if (orderId == null || paymentId == null || status == null) {
				return ResponseEntity.badRequest().body(Map.of("error", "Required fields are missing."));
			}

			MyOrder myOrder = this.orderRepo.findByOrderId(orderId);
			if (myOrder == null) {
				return ResponseEntity.notFound().build();
			}

			myOrder.setPaymentId(paymentId);
			myOrder.setStatus(status);
			this.orderRepo.save(myOrder);

			return ResponseEntity.ok(Map.of("message", "Order updated successfully."));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "An error occurred during update."));
		}
	}

	@PostMapping("/feedback")
	public ResponseEntity<Object> feedbackSubmit(@RequestBody Feedback feedback) {

		Feedback savedFeedback = repo.save(feedback);
		return new ResponseEntity<>(savedFeedback, HttpStatus.OK);
	}

	@GetMapping("/getAllFeedbackList")
	@ResponseBody
	public List<Feedback> getAllFeedbackByRatingAscending() {
		List<Feedback> feedbackList = repo.findAllByRatingDescending();
		return feedbackList;
	}

	@PostMapping("/track")
	public ResponseEntity<Void> trackVisitor(@RequestParam Long visitedProfileId, @RequestParam Long visitorUserId) {
		try {
			// Validate visitedProfileId and visitorUserId if needed
			if (visitedProfileId <= 0 || visitorUserId <= 0) {
				return ResponseEntity.badRequest().build();
			}

			// Create a new Visitor object and set the current timestamp
			Visitor visitor = new Visitor();
			visitor.setVisitedProfileId(visitedProfileId);
			visitor.setVisitorUserId(visitorUserId);
			visitor.setTimestamp(new Date());
//my comment
			// Save the visitor data to the database
			Visitor savedVisitor = repo.save(visitor);

			if (savedVisitor != null) {
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/sortby")
	public List<User> Sortby(@RequestBody List<Integer> IdList) {
		// Assuming that you have a service or repository to fetch users by their IDs
		List<User> users = repo.getUsersByIds(IdList);

		// Create a composite comparator that considers multiple criteria with
		// priorities

		Comparator<User> userComparator = Comparator.comparingInt(User::getIncomeRange) // Priority 1: Sort by
																						// incomeRange
//	        .thenComparing(User::getCity) // Priority 2: Sort by city
				.thenComparingInt(User::getAge); // Priority 3: Sort by age
		// Sort the user list using the incomeComparator
		List<User> sortedUsers = users.stream().sorted(userComparator).collect(Collectors.toList());

		// Print the sorted user list for debugging purposes
		sortedUsers.forEach(user -> System.out.println(
				user.getName() + " - " + user.getIncomeRange() + " - " + user.getCity() + " - " + user.getAge()));

		return sortedUsers;

	
	}

	
	
	
	
	// FIND THE PHONEBOOK ALL LIST FIND
	@GetMapping("/phonebook")
	public List<Object[]> phonebookList() {
		return repo.PhonebookList();
	}

	// FIND THE USERID AND RESPONSE PHONEBOOK FIND
	@PostMapping("/phonebookid")
	public ResponseEntity<Object> getPhoneNumberByUserid(@RequestParam("userid") String userid) {
		String phoneNumber = repo.findPhoneNumberByUserid(userid);

		if (phoneNumber != null) {
			return ResponseEntity.ok(phoneNumber);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Phone number not found for the given user id");
		}
	}

	// change password after open the profile then value the match
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("email") String userEmail) {

		System.out.println("OLD PASSWORD: " + oldPassword);
		System.out.println("NEW PASSWORD: " + newPassword);

		// Assuming you have a method to get the user by email from the repository
		User currentUser = this.repo.getUserByEmail(userEmail);

		if (currentUser != null && oldPassword.equals(currentUser.getPassword())) {
			// Old password matches the current password for the user

			// Implement logic to update the password
			currentUser.setPassword(newPassword);
			currentUser.setConfirmPassword(newPassword);

			// Save the updated user entity to the repository
			this.repo.save(currentUser);

			return "Password changed successfully";
		} else {
			// Old password doesn't match or user not found
			return "Failed to change password. Please check your old password and try again.";
		}
	}

	
	// forget password Mapping forget verifyOtp and changePassword three api change password 
	@PostMapping("/forget")
	public ResponseEntity<String> sendOtp(@RequestParam("email") String email, HttpSession session) {
		System.out.println("EMAIL: " + email);

		// Generating OTP for 6 digits
		Random random = new SecureRandom(); // Use SecureRandom for better randomness
		int otp = 100000 + random.nextInt(900000);
		System.out.println("OTP: " + otp);

		// Send OTP to email
		String subject = "OTP From Vivah Website";
		String message = "<div style='border:1px solid #e2e2e2; padding:20px'>" + "<h1> OTP is: <b>" + otp + "</b></h1>"
				+ "</div>";

		String to = email;
		boolean flag = this.registrationService.sendEmail(subject, message, to);

		if (flag) {
			// Save OTP and email in session for verification
			session.setAttribute("myOtp", otp);
			session.setAttribute("email", email);
			return ResponseEntity.ok().body("{\"status\": \"otp-sent success\"}");
		} else {
			session.setAttribute("message", "Failed to send OTP. Check your email id!");
			return ResponseEntity.ok().body("{\"status\": \"otp-sent failure\"}");
		}
	}
	
	@PostMapping("/verifyOtp")
	public ResponseEntity<String> verifyOtp(@RequestParam("otp") int enteredOtp, HttpSession session, Model model) {
	    Integer myOtp = (Integer) session.getAttribute("myOtp");
	    String email = (String) session.getAttribute("email");

	    if (myOtp != null && myOtp.equals(enteredOtp)) {
	        User user = repo.getUserByEmail(email);

	        if (user != null) {
	            session.setAttribute("user", user);
	            // Store a flag in the session to indicate that OTP has been successfully verified
	            session.setAttribute("otpVerified", true);
	            return ResponseEntity.ok("{\"status\": \"otp-verification success\"}");
	        } else {
	            model.addAttribute("message", "User not found. Please try again.");
	            return ResponseEntity.ok("{\"status\": \"otp-verification failure\"}");
	        }
	    } else {
	        model.addAttribute("message", "Invalid OTP. Please try again.");
	        return ResponseEntity.ok("{\"status\": \"otp-verification failure\"}");
	    }
	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestParam("newpassword") String newPassword, HttpSession session) {
	    User user = (User) session.getAttribute("user");
	    Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");

	    if (user != null && otpVerified != null && otpVerified) {
	        // Update the user's password in the session
	        user.setPassword(newPassword);
	        user.setConfirmPassword(newPassword);

	        // Optionally, update the password in the database as well
	        repo.save(user);

	        // Clear the OTP verification flag from the session
	        session.removeAttribute("otpVerified");

	        return ResponseEntity.ok("{\"status\": \"password-change success\"}");
	    } else {
	        return ResponseEntity.ok("{\"status\": \"password-change failure\"}");
	    }
	}

	





}
