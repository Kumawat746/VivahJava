package com.vivah.vivah.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vivah.vivah.model.User;
import com.vivah.vivah.model.Visitor;

@RestController
@RequestMapping("/api")
public class VisitorController {


//	@PostMapping("/track")
//	public ResponseEntity<Void> trackVisitor(@RequestParam Long visitedProfileId, @RequestParam Long visitorUserId) {
//	    try {
//	        // Validate visitedProfileId and visitorUserId if needed
//	        if (visitedProfileId <= 0 || visitorUserId <= 0) {
//	            return ResponseEntity.badRequest().build();
//	        }
//
//	        // Create a new Visitor object and set the current timestamp
//	        Visitor visitor = new Visitor();
//	        visitor.setVisitedProfileId(visitedProfileId);
//	        visitor.setVisitorUserId(visitorUserId);
//	        visitor.setTimestamp(new Date());
//
//	        // Save the visitor data to the database
//	        Visitor savedVisitor = visitorRepository.save(visitor);
//
//	        if (savedVisitor != null) {
//	            return ResponseEntity.ok().build();
//	        } else {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	    }
//	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@PostMapping("/track")
//	public ResponseEntity<Visitor> trackVisitor(@RequestBody Visitor visitor) {
//		try {
//			// Validate visitor data here if needed
//			if (visitor.getVisitedProfileId() == null || visitor.getVisitorUserId() == null) {
//				return ResponseEntity.badRequest().build();
//			}
//
//			// Set the current timestamp
//			visitor.setTimestamp(new Date());
//
//			// Save the visitor data to the database
//			Visitor savedVisitor = visitorRepository.save(visitor);
//
//			return ResponseEntity.ok(savedVisitor);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//		
//		
//	}
//	
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//
//    @GetMapping("/profile")
//    public ResponseEntity<List<Visitor>> getProfileVisitors(@RequestParam Long profileId) {
//        try {
//            List<Visitor> profileVisitors = visitorRepository.findByVisitedProfileId(profileId);
//            return ResponseEntity.ok(profileVisitors);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    
//
//
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Visitor>> getUserVisits(@PathVariable Long userId) {
//        try {
//            List<Visitor> userVisits = visitorRepository.findByVisitorUserId(userId);
//            return ResponseEntity.ok(userVisits);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}