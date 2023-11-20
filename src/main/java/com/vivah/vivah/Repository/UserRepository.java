package com.vivah.vivah.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vivah.vivah.model.ChatMessage;
import com.vivah.vivah.model.User;
import com.vivah.vivah.model.Userchat;

import jakarta.transaction.Transactional;




public interface UserRepository extends JpaRepository<Userchat, Long> {
    Userchat findByUsername(String username);

	List<Userchat> findAllByUsername(String sender);

	boolean existsByUserId(String userId);

	Optional<User> findByUserId(String senderUserId);

	
	
}
