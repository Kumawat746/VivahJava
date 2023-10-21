package com.vivah.vivah.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.razorpay.*;
import com.vivah.vivah.Repository.OrderRepository;
import com.vivah.vivah.Repository.RegistrationRepository;
import com.vivah.vivah.Repository.ReportRepository;
import com.vivah.vivah.model.Otpclass;
import com.vivah.vivah.model.SmsPojo;
import com.vivah.vivah.modeltwo.Feedback;
import com.vivah.vivah.modeltwo.MyOrder;
import com.vivah.vivah.modeltwo.Report;
import com.vivah.vivah.modeltwo.User;
import com.vivah.vivah.modeltwo.Visitor;
import com.vivah.vivah.service.SmsService;
import com.vivah.vivah.servicetwo.RegistrationService;

import io.micrometer.common.util.StringUtils;
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
	private ReportRepository reportRepository;
	
	@Autowired
	private SimpMessagingTemplate websocket;
	
	@Autowired
	private SmsService service;

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

			user.setUserId(newUserid);
		} else {
			// Handle the case where lastUserId is null
			System.out.println("lastUserId is null. Handle the null case here.");

			// Generate a unique user ID starting with "vv" and a predefined number (e.g.,
			// "1000")
			String newUserid = "vv" + "1000";

			// Set the generated unique user ID for the user object
			user.setUserId(newUserid);

			System.out.println(newUserid); // Output: vv1000 (if lastUserId was null)
		}


		// Get the current date
		LocalDate currentDate = LocalDate.now();

		// Set the joinDate property for the user
		user.setJoinDate(currentDate);

		
		
		// Save the user object to the repository
		User userObj = repo.save(user);
		return new ResponseEntity<>(userObj, HttpStatus.OK);

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
	public ResponseEntity<Object> getUserById(@RequestParam("userId") String userId) {

		Optional<User> user = repo.getUserByUserId(userId);

		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

	}

	// UPDATE THE DATA
	@PostMapping("/updateUser")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		String userId = user.getUserId();
		repo.updateProfile(user, userId);

		return ResponseEntity.ok(user);
	}

	// FIND THE DATA BY USER INFORMATION
	@PostMapping("/search")
	public List<User> Search(@RequestBody User user) {
		// Call the repository method to perform the search
		List<User> searchResult = repo.searchByCriteria(user);

		// Return the search result
		return searchResult;
	}

	// ACCEPTED MEMBER INSERT THE DATA
	@PostMapping("/accept")
	public void accept(@RequestParam String userId, @RequestParam String accepted_mem) {
		repo.acceptMem(userId, accepted_mem);
	}

	// DECLINED MEMBER INSERT THE DATA
	@PostMapping("/decline")
	public void decline(@RequestParam String userId, @RequestParam String declined_mem) {
		repo.declinedMem(userId, declined_mem);
	}

	// SHORTLIST MEMBER INSERT THE DATA
	@PostMapping("/shortlist")
	public void shortlisted(@RequestParam String userId, @RequestParam String shortlisted_mem) {
		repo.shortlistedmem(userId, shortlisted_mem);
	}

	// SEND MEMBER INSERT THE DATA
	@PostMapping("/send")
	public void send(@RequestParam String userId, @RequestParam String send_mem) {
		repo.sendmem(userId, send_mem);
	}

	// RECEIVED MEMBER INSERT THE DATA
	@PostMapping("/receive")
	public void received(@RequestParam String userId, @RequestParam String received_mem) {
		repo.receivedmem(userId, received_mem);
	}

	// BLOCKED MEMBER INSERT THE DATA
	@PostMapping("/blocked")
	public void blocked(@RequestParam String userId, @RequestParam String blocked_mem) {
		repo.blockedmem(userId, blocked_mem);
	}

	// ACCEPTELIST MEMBER ALL LIST FIND
	@PostMapping("/accepteList")
	public ResponseEntity<List<User>> accepted(@RequestParam String userId) {

		// Fetch accepted user IDs based on the provided userId
		List<String> allids = repo.acceptedIdList(userId);
		if (allids.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			List<User> userList = repo.allAccepted(allids);
			return ResponseEntity.ok(userList);
		}
	}

	// DECLINELIST MEMBER ALL LIST FIND
	@PostMapping("/declineList")
	public ResponseEntity<List<User>> declined(@RequestParam String userId) {
		List<String> allids = repo.declineIdList(userId);

		if (allids.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			List<User> userList = repo.alldeclined(allids);
			return ResponseEntity.ok(userList);
		}
	}

	@PostMapping("/shortList")
	public ResponseEntity<List<User>> shortlist(@RequestParam String userId) {
		List<String> allids = repo.shortIdList(userId);

		if (allids.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			List<User> userList = repo.allshortlisted(allids);
			return ResponseEntity.ok(userList);
		}
	}

	// SENDLISTED MEMBER ALL LIST FIND
	@PostMapping("/sendList")
	public ResponseEntity<List<User>> sendlist(@RequestParam String userId) {
		List<String> allids = repo.sendIdList(userId);
		if (allids.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			List<User> userList = repo.allsendlisted(allids);
			return ResponseEntity.ok(userList);
		}
	}

	// RECEIVE MEMBER ALL LIST FIND
	@PostMapping("/receivdtList")
	public ResponseEntity<List<User>> receivedlist(@RequestParam String userId) {
		List<String> allids = repo.receivedIdList(userId);
		if (allids.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			List<User> userList = repo.allreceivedlisted(allids);
			return ResponseEntity.ok(userList);
		}
	}

	// BLOCKED MEMBER ALL LIST FIND
	@PostMapping("/blockList")
	public ResponseEntity<List<User>> blockedlist(@RequestParam String userId) {
		List<String> allids = repo.blockedIdList(userId);
		if (allids.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			List<User> userList = repo.allblockedlisted(allids);
			return ResponseEntity.ok(userList);

		}
	}

	// Assuming MyOrder entity has fields email and userid

	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(@RequestBody Map<String, Object> data, @RequestParam("userId") String userId) {
		System.out.println(data);

		int amt = Integer.parseInt(data.get("amount").toString());
		String orderResponse = null;

		try {
			// Create a Razorpay client
			var client = new RazorpayClient("rzp_test_oGRI5TqiOtsobA", "Hp1QRAo7ylzuoZ5wjwPPD8je");

			// Create an order request
			JSONObject obj = new JSONObject();
			obj.put("amount", amt * 100); // Amount in paise
			obj.put("currency", "INR");
			obj.put("receipt", "txn_7364767262");

			// Create the new order
			Order order = client.orders.create(obj);
			System.out.println(order);

			// Retrieve user from the database based on userId
			Optional<User> userOptional = this.repo.getUserByUserId(userId);

			// Check if the user is present in the Optional
			if (userOptional.isPresent()) {
				// The user is present, you can get it from the Optional
				User user = userOptional.get();

				// Save the order in the database
				MyOrder myOrder = new MyOrder();
				myOrder.setAmount(order.get("amount") + " ");
				myOrder.setOrderId(order.get("id"));
				myOrder.setPaymentId(null);
				myOrder.setReceipt(order.get("receipt"));
				myOrder.setStatus("created");

				// Set user details
				myOrder.setUserId(user.getUserId());
				myOrder.setEmail(user.getEmail());

				// Subscription details
				int subscriptionDuration = Integer.parseInt(data.get("subscriptionDuration").toString());
				ZonedDateTime startDate = ZonedDateTime.now(ZoneId.systemDefault());
				ZonedDateTime endDate = startDate.plusDays(subscriptionDuration);

				myOrder.setStartDate(startDate);
				myOrder.setEndDate(endDate);

				// Save the order in the database
				this.orderRepo.save(myOrder);

				// Construct a response to show the information
				orderResponse = "Order created successfully. Subscription Details: " + "Start Date: " + startDate
						+ ", End Date: " + endDate;

			} else {
				// The user is not present, handle this case accordingly
				// For example, you can return an error response
				orderResponse = "User not found for the given userId";
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

		// Return the order response
		return orderResponse;
	}

//	@PostMapping("/update_order")
//	public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
//		try {
//			String orderId = (String) data.get("order_id");
//			String paymentId = (String) data.get("payment_id");
//			String status = (String) data.get("status");
//
//			if (orderId == null || paymentId == null || status == null) {
//				return ResponseEntity.badRequest().body(Map.of("error", "Required fields are missing."));
//			}
//
//			MyOrder myOrder = this.orderRepo.findByOrderId(orderId);
//			if (myOrder == null) {
//				return ResponseEntity.notFound().build();
//			}
//
//			myOrder.setPaymentId(paymentId);
//			myOrder.setStatus(status);
//			this.orderRepo.save(myOrder);
//
//			return ResponseEntity.ok(Map.of("message", "Order updated successfully."));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(Map.of("error", "An error occurred during update."));
//		}
//	}
//
	@PostMapping("/feedback")
	public ResponseEntity<Object> feedbackSubmit(@RequestBody Feedback feedback) {

		Feedback savedFeedback = repo.save(feedback);
		return new ResponseEntity<>(savedFeedback, HttpStatus.OK);
	}

	// GET ALL FEEDBACK USER LIST
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
	
	// report add and save the database 
	@PostMapping("/report")
	public ResponseEntity<?> createReport(@RequestBody Report report) {
	    // Handle file content or file path here based on your use case
	    // For simplicity, assuming attachFile is a file path
	    String filePath = "/path/to/save/" + report.getAttachFile().getName();
	    report.setAttachFile(new File(filePath));

	    if (report.getUserId().equals(report.getReportUserId())) {
	        // userId and reportUserId are equal, return a bad request response with a custom message
	        String message = "userId and reportUserId cannot be equal.";
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	    }
	    
	    
	    // Check if the combination of userId and reportUserId already exists in the Report table
	    Optional<Report> existingReport = reportRepository.findByUserIdAndReportUserId(report.getUserId(), report.getReportUserId());

	    if (existingReport.isPresent()) {
	        // Combination of userId and reportUserId already exists, return a conflict response with a custom message
	        String message = "Report with userId=" + report.getUserId() + " and reportUserId=" + report.getReportUserId() + " already exists.";
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	    } else {
	        // Check if the userId exists in the User table
	        Optional<User> existingUser = repo.findByUserId(report.getUserId());

	        if (existingUser.isPresent()) {
	            User user = existingUser.get();
	            report.setEmail(user.getEmail());
	            // userId exists in the User table, save the report in the Report table
	            Report savedReport = reportRepository.save(report);
	            // Return a response with the saved report
	            return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
	        } else {
	            // userId does not exist in the User table, handle the situation accordingly
	            // You may want to throw an exception or handle it based on your requirements
	            // For now, let's return not found to indicate that the user wasn't found
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with userId=" + report.getUserId() + " not found.");
	        }
	    }
	}
	
	
	
//	@PostMapping("/sortby")
//	public List<User> Sortby(@RequestBody List<Integer> IdList) {
//		// Assuming that you have a service or repository to fetch users by their IDs
//		List<User> users = repo.getUsersByIds(IdList);
//
//		// Create a composite comparator that considers multiple criteria with
//		// priorities
//
//		Comparator<User> userComparator = Comparator.comparingInt(User::getIncomeRange) // Priority 1: Sort by
//																						// incomeRange
////	        .thenComparing(User::getCity) // Priority 2: Sort by city
//				.thenComparingInt(User::getAge); // Priority 3: Sort by age
//		// Sort the user list using the incomeComparator
//		List<User> sortedUsers = users.stream().sorted(userComparator).collect(Collectors.toList());
//
//		// Print the sorted user list for debugging purposes
//		sortedUsers.forEach(user -> System.out.println(
//				user.getName() + " - " + user.getIncomeRange() + " - " + user.getCity() + " - " + user.getAge()));
//
//		return sortedUsers;
//
//	
//	}
//
//	
//	
//	
//	
	// FIND THE PHONEBOOK ALL LIST FIND
	@GetMapping("/phonebook")
	public List<Object[]> phonebookList() {
		return repo.PhonebookList();
	}

	// FIND THE USERID AND RESPONSE PHONEBOOK FIND
	@PostMapping("/phonebookid")
	public ResponseEntity<Object> getUserDetailsByUserId(@RequestParam("userId") String userId) {
		Optional<Map<String, Object>> userDetails = repo.getUsersByUserId(userId);

		if (userDetails.isPresent()) {
			return ResponseEntity.ok(userDetails.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not found for the given user id");
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
	
	
	

	@PostMapping("/sentOtp")
	public ResponseEntity<String> SentOtp(@RequestBody SmsPojo sms) {

		// Get the phone number from the request body
		String phone = sms.getPhoneNumber();

		try {
			if (phone.equals(null)) {
				// Return a failure message

				// Send a message to the topic

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failure: Phone number cannot be 0");
			} else {
				String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

				service.sentOtp(sms);
				websocket.convertAndSend("/sms", timeStamp + "sms sent" + sms.getPhoneNumber());
				// Return a success message
				return ResponseEntity.ok("Success");

			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: An error occurred");
		}
	}

	// ...

	@PostMapping("/verify")
	public ResponseEntity<String> verifyOtp(@RequestBody Otpclass otp) {
		// Get the OTP from the request body
		String userOtp = otp.getOtp();

		try {
			if (StringUtils.isEmpty(userOtp)) {
				// Return a bad request response
				return ResponseEntity.badRequest().body("Failure: OTP cannot be empty");
			} else {
				// Verify the OTP using your service
				boolean isValidOtp = service.verifyOtp(userOtp);

				JSONObject jsonresponse = new JSONObject(); // Create a JSON object

				if (isValidOtp) {
					// Return a success response with a JSON object
					jsonresponse.put("success", true);
					return ResponseEntity.ok(jsonresponse.toString());
				} else {
					// Return a failure response with a JSON object
					jsonresponse.put("success", false);
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonresponse.toString());
				}
			}
		} catch (Exception e) {
			// Handle other exceptions if needed
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: An error occurred");
		}
	}
	// retrieve recently joined users
	@GetMapping("/recently-joined")
	public ResponseEntity<List<Object[]>> getRecentlyJoinedUsers() {
	    // Calculate the start date based on the current date to 3 days ago
	    LocalDate startDate = LocalDate.now().minusDays(3);

	    // Call the repository method with the calculated start date
	    // The repository method should return a list of Object arrays containing relevant user data
	    List<Object[]> recentlyJoinedUsers = repo.findRecentlyJoinedUsers(startDate);

	    // Return a ResponseEntity with the list of recently joined users
	    return ResponseEntity.ok(recentlyJoinedUsers);
	}
	
	
	


}
