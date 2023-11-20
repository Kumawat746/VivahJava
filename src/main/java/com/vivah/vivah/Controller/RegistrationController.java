package com.vivah.vivah.Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.razorpay.*;
import com.vivah.vivah.Repository.ChatMessageRepository;
import com.vivah.vivah.Repository.ImageRepository;
import com.vivah.vivah.Repository.OrderRepository;
import com.vivah.vivah.Repository.PartnerPreferenceRepository;
import com.vivah.vivah.Repository.RegistrationRepository;
import com.vivah.vivah.Repository.ReportRepository;
import com.vivah.vivah.Repository.RepositorytwoRepo;
import com.vivah.vivah.Repository.UserRepository;
import com.vivah.vivah.model.ChatMessage;
import com.vivah.vivah.model.Feedback;
import com.vivah.vivah.model.Filter;
import com.vivah.vivah.model.Image;
import com.vivah.vivah.model.MyOrder;
import com.vivah.vivah.model.Otpclass;
import com.vivah.vivah.model.PartnerPreference;
import com.vivah.vivah.model.Registration;
import com.vivah.vivah.model.Report;
import com.vivah.vivah.model.SmsPojo;
import com.vivah.vivah.model.User;
import com.vivah.vivah.model.Userchat;
import com.vivah.vivah.model.Visitor;
import com.vivah.vivah.service.SmsService;
import com.vivah.vivah.servicetwo.RegistrationService;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class RegistrationController {

	@Autowired
	private RegistrationRepository repo;

	@Autowired
	private PartnerPreferenceRepository partnerPreferenceRepository;
	@Autowired
	private RepositorytwoRepo regtwo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private ChatMessageRepository messageRepository;

	@Autowired
	private UserRepository userchatRepository;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private SimpMessagingTemplate websocket;

	@Autowired
	private SmsService service;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private ImageRepository imageRepository;

	String message;

	String getLastUser() {

		String lastUserId = regtwo.getLastUserId();
		return lastUserId;

	}

	// new user data insert the table registration and user table
	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@RequestBody Registration reg) {
		String message;

		String tempEmail = reg.getEmail();
		String tempPhoneNumber = reg.getPhoneNumber();

		String isEmailExists = regtwo.SearchMail(tempEmail);
		String isNumberExists = regtwo.SearchPhoneNumber(tempPhoneNumber);

		if (isEmailExists != null && isNumberExists != null) {
			// Both email and phone number already exist, send a combined message with
			// HttpStatus.CONFLICT (409).
			message = "Email and phone number already exist";
			return new ResponseEntity<>(message, HttpStatus.CONFLICT);
		} else if (isEmailExists != null) {
			// Email already exists, send a message with HttpStatus.CONFLICT (409).
			message = "Email already exists";
			return new ResponseEntity<>(message, HttpStatus.CONFLICT);
		} else if (isNumberExists != null) {
			// Phone number already exists, send a message with HttpStatus.CONFLICT (409).
			message = "Phone number already exists";
			return new ResponseEntity<>(message, HttpStatus.CONFLICT);
		}

		String lastUserId = getLastUser();

		if (lastUserId != null) {
			String numericPart = lastUserId.substring(2);
			int numericValue = Integer.parseInt(numericPart) + 1;
			String formattedNumericValue = String.format("%04d", numericValue);

			String newUserid = "vv" + formattedNumericValue;

			System.out.println(newUserid);

			User newuser = new User();
			newuser.setUserId(newUserid);
			newuser.setUsername(reg.getUsername());

			reg.setUserId(newUserid);
			reg.setJoinDate(LocalDate.now()); // Set the joinDate here

			repo.save(newuser); // Save the user object first
			regtwo.save(reg); // Save the registration data

		} else {
			System.out.println("lastUserId is null. Handle the null case here.");

			String newUserid = "vv" + "1000";

			User newuser = new User();
			newuser.setUserId(newUserid);
			newuser.setUsername(reg.getUsername());

			reg.setUserId(newUserid);
			regtwo.save(reg);
			repo.save(newuser); // Save the user object first
			System.out.println(newUserid);
		}
		// Save the user object to the repository
		return new ResponseEntity<>(reg, HttpStatus.OK);
	}

	// login the data email, phoneNumber and password
	@PostMapping("/login")
	public ResponseEntity<Object> loginUser(@RequestBody Registration login) {

		String email = login.getEmail(); // Assuming emailOrPhoneNumber field contains either email or mobile number

		String phoneNumber = login.getPhoneNumber();
		String password = login.getPassword();

		// First, try to find a user by email
		Registration dataByEmail = regtwo.findByEmailAndPassword(email, password);

		// If no user is found by email, try to find by mobile number
		Registration dataByPhoneNumber = regtwo.findByPhoneNumberAndPassword(phoneNumber, password);

		if (dataByEmail != null || dataByPhoneNumber != null) {

			// User found with either email or mobile number, and password matches
			return new ResponseEntity<>(dataByEmail != null ? dataByEmail : dataByPhoneNumber, HttpStatus.OK);

		} else {

			// No user found or password doesn't match for both email and mobile number
			message = "Invalid email/mobile number or password";

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

	// forget password Mapping forget verifyOtp and changePassword three api change
	// password
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

	@PostMapping("/verifyotp")
	public ResponseEntity<String> verifyOtp(@RequestParam("otp") int enteredOtp, HttpSession session, Model model) {
		Integer myOtp = (Integer) session.getAttribute("myOtp");
		String email = (String) session.getAttribute("email");

		if (myOtp != null && myOtp.equals(enteredOtp)) {
			Registration reg = regtwo.getUserByEmail(email);

			if (reg != null) {
				session.setAttribute("user", reg);
				// Store a flag in the session to indicate that OTP has been successfully
				// verified
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
		Registration reg = (Registration) session.getAttribute("user");
		Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");

		if (reg != null && otpVerified != null && otpVerified) {
			// Update the user's password in the session
			reg.setPassword(newPassword);
			reg.setConfirmPassword(newPassword);

			// Optionally, update the password in the database as well
			regtwo.save(reg);

			// Clear the OTP verification flag from the session
			session.removeAttribute("otpVerified");

			return ResponseEntity.ok("{\"status\": \"password-change success\"}");
		} else {
			return ResponseEntity.ok("{\"status\": \"password-change failure\"}");
		}
	}

	@PostMapping("/sentOtp")
	public ResponseEntity<String> sendOtp(@RequestBody SmsPojo sms) {
		String phone = sms.getPhoneNumber();

		try {
			if (StringUtils.isEmpty(phone)) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Failure: Phone number cannot be empty");
			} else {
				// Check if the phone number exists in the database
				User user = repo.findByPhoneNumber(phone);
				System.out.println(phone);
				if (user != null) {
					String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					service.sentOtp(sms);
					return ResponseEntity.ok("Success: OTP sent");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body("Failure: Phone number not found in the database");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: An error occurred");
		}
	}

	@PostMapping("/verifyOtp")
	public ResponseEntity<String> verifyOtp(@RequestBody Otpclass otp) {
		String userOtp = otp.getOtp();

		try {
			if (StringUtils.isEmpty(userOtp)) {
				return ResponseEntity.badRequest().body("Failure: OTP cannot be empty");
			} else {
				boolean isValidOtp = service.verifyOtp(userOtp);
				return ResponseEntity.ok("{\"success\":" + isValidOtp + "}");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: An error occurred");
		}
	}

	@PostMapping("/changePasswordOtp")
	public ResponseEntity<String> changePassword(@RequestBody SmsPojo chagePassword) {
		try {
			boolean isValidOtp = service.verifyOtp(chagePassword.getOtp());

			if (isValidOtp) {
				// Log the phone number for debugging
				System.out.println("Phone Number: " + chagePassword.getPhoneNumber());

				// Retrieve user from the database using the phone number
				Registration reg = regtwo.findByPhoneNumber(chagePassword.getPhoneNumber());

				if (reg != null) {
					// Update the user's password
					reg.setPassword(chagePassword.getNewPassword());
					reg.setConfirmPassword(chagePassword.getNewPassword());

					// Optionally, update the password in the database
					repo.save(reg);

					return ResponseEntity.ok("{\"status\":\"password-change success\"}");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body("Failure: User not found with the given phone number");
				}
			} else {
				return ResponseEntity.ok("{\"status\":\"password-change failure\", \"message\":\"Invalid OTP\"}");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure: An error occurred");
		}
	}

	// retrieve recently joined users
	@GetMapping("/recently-joined")
	public ResponseEntity<List<Registration>> getRecentlyJoinedUsers() {
		// Calculate the start date based on the current date to 3 days ago
		LocalDate startDate = LocalDate.now().minusDays(3);

		// Call the repository method with the calculated start date
		// The repository method should return a list of Object arrays containing
		// relevant user data
		List<Registration> recentlyJoinedUsers = regtwo.findRecentlyJoinedUsers(startDate);

		// Return a ResponseEntity with the list of recently joined users
		return ResponseEntity.ok(recentlyJoinedUsers);
	}

	// send message sender and receiver
	@PostMapping("/sendMsg")
	public ResponseEntity<String> sendMessage(@RequestBody ChatMessage message) {
		Userchat receiver = message.getReceiver();
		Userchat sender = message.getSender();

		// Save the receiver if it doesn't exist
		if (receiver != null && !userchatRepository.existsByUserId(receiver.getUserId())) {
			userchatRepository.saveAndFlush(receiver);
		}

		// Save the sender if it doesn't exist
		if (sender != null && !userchatRepository.existsByUserId(sender.getUserId())) {
			userchatRepository.saveAndFlush(sender);
		}

		// Set the updated sender and receiver in the message
		message.setSender(sender);
		message.setReceiver(receiver);

		try {
			// Save the message to the database
			messageRepository.saveAndFlush(message);
			return ResponseEntity.ok("Message sent successfully");
		} catch (DataIntegrityViolationException e) {
			// Handle specific data integrity violation (e.g., duplicate entry)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Error sending message: Duplicate entry or invalid data");
		} catch (Exception e) {
			// Log the exception for debugging purposes
			e.printStackTrace();

			// Handle other exceptions and return an appropriate response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error sending message: An unexpected error occurred");
		}
	}

	@PostMapping("/messages")
	public ResponseEntity<List<ChatMessage>> getMessages(@RequestParam("senderId") Long senderId,
			@RequestParam("receiverId") Long receiverId) {

		Userchat sender = userchatRepository.findById(senderId).orElse(null);
		Userchat receiver = userchatRepository.findById(receiverId).orElse(null);

		if (sender == null || receiver == null || senderId.equals(receiverId)) {
			// Handle invalid input or sender and receiver being the same
			return ResponseEntity.badRequest().build();
		}

		List<ChatMessage> messages = messageRepository.findBySenderAndReceiver(sender, receiver);
		return ResponseEntity.ok(messages);
	}

	@PostMapping("/allMatches")
	public ResponseEntity<List<User>> allMatches(@RequestParam String userId, int matchscore) {
		if (userId == null) {
			return ResponseEntity.badRequest().build();
		}

		List<User> allUsers = getAllUser();

		List<PartnerPreference> partnerPreferences = partnerPreferenceRepository.getUserPreference(userId);
		List<User> matchingUsers = new ArrayList<>();
		for (User user : allUsers) {

			int maxMatchScore = 0; // Track the maximum match score for this user
			for (PartnerPreference preference : partnerPreferences) {
				int matchScore = calculateMatchScore(user, preference);
				maxMatchScore = Math.max(maxMatchScore, matchScore);
			}
			user.setMatchScore(maxMatchScore);
			if (maxMatchScore >= matchscore) {
				matchingUsers.add(user);
			}
		}

		// Sort the list in descending order of match scores
		matchingUsers.sort(Comparator.comparing(User::getMatchScore).reversed());

		return ResponseEntity.ok(matchingUsers);
	}

	private int calculateMatchScore(User user, PartnerPreference preference) {
		int matchScore = 0;

		// Age Match
		if (user.getAge() != null && preference.getAgeMin() != null && preference.getAgeMax() != null) {
			if (user.getAge() >= preference.getAgeMin() && user.getAge() <= preference.getAgeMax()) {
				matchScore += 1;
			}
		}

		// Height Match
		if (user.getHeight() != null && preference.getHeightMin() != null && preference.getHeightMax() != null) {
			if (user.getHeight() >= preference.getHeightMin() && user.getHeight() <= preference.getHeightMax()) {
				matchScore += 1;
			}
		}

		// Religion Match
		if (user.getReligion() != null && preference.getReligion() != null) {
			if (user.getReligion().equals(preference.getReligion())) {
				matchScore += 1;
			}
		}

		// City Match
		if (user.getCity() != null && preference.getCity() != null) {
			if (user.getCity().equals(preference.getCity())) {
				matchScore += 1;
			}
		}

		// Add more criteria and weight them as needed

		return matchScore;
	}

//	@PostMapping("/getVisitors")
//	public ResponseEntity<List<User>> trackVisitor(@RequestParam  String userId) {
//		try {
//		
//			if (userId.equals(null)) {
//				return ResponseEntity.badRequest().build();
//			}
//
//			
//			Visitor visitor = new Visitor();
//			
//			//getvisitors 
//			List<String> allids = repo.getvisitor(userId);
//			
//			
//			//get visitor data
//			List<User>	allvisitors= repo.findvisitor(allids);
//			
//			
//			allids.removeIf(id -> id == null);
//			
//			//all visitor list sending
//		
//
//			if (allvisitors != null) {
//				return ResponseEntity.ok(allvisitors);
//			} else {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
//	

	@PostMapping("/PartnerPreference")
	public ResponseEntity<PartnerPreference> PartnerPreference(@RequestBody PartnerPreference PartnerP) {
		String userId = PartnerP.getUserid();
		partnerPreferenceRepository.delete(PartnerP);

		PartnerPreference pp = partnerPreferenceRepository.save(PartnerP);
		return ResponseEntity.ok(pp);

	}

//
//	@PostMapping("/sortby")
//	public List<User> Sortby(@RequestBody List<Integer> IdList) {
//	    // Assuming that you have a service or repository to fetch users by their IDs
//	    List<User> users = repo.getUserById(IdList);
//
//
//	    // Create a composite comparator that considers multiple criteria with priorities
//
//	    Comparator<User> userComparator = Comparator
//	        .comparingInt(User::getIncomeRange) // Priority 1: Sort by incomeRange
////	        .thenComparing(User::getCity) // Priority 2: Sort by city
//      .thenComparingInt(User::getAge); // Priority 3: Sort by age
//	    // Sort the user list using the incomeComparator
//	    List<User> sortedUsers = users.stream()
//	            .sorted(userComparator)
//	            .collect(Collectors.toList());
//
//	    // Print the sorted user list for debugging purposes
//	    sortedUsers.forEach(user -> System.out.println(user.getUsername() + " - " + user.getIncomeRange() + " - " + user.getCity()+ " - " + user.getAge()));
//
//	    return sortedUsers;
//	}
//	
//

	@PostMapping("/apply")
	public ResponseEntity<List<User>> applyFilter(@RequestBody Filter Criteria) {

		List<User> filteredUsers = repo.filterUsers(Criteria);

		return new ResponseEntity<>(filteredUsers, HttpStatus.OK);
	}

	@PostMapping("/updateImage")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			Image image = new Image();
			image.setName(file.getOriginalFilename());
			image.setData(file.getBytes());

			imageRepository.save(image);

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/images/download/")
					.path(image.getId().toString()).toUriString();

			return ResponseEntity.ok("File uploaded successfully. Download link: " + fileDownloadUri);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading the file.");
		}
	}

	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadImage(@RequestParam("id") Long id) {
		Optional<Image> imageOptional = imageRepository.findById(id);
		if (imageOptional.isPresent()) {
			Image image = imageOptional.get();
			return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=\"" + image.getName() + "\"")
					.body(image.getData());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/activity")
	public Map<String, Long> allActivitylogs(@RequestParam String userId) {

		Map<String, Long> allUsersActivity = repo.allActivity(userId);

		return allUsersActivity;

	}

}