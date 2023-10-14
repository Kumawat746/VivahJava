package com.vivah.vivah.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vivah.vivah.model.Userchat;




public interface UserRepository extends JpaRepository<Userchat, Long> {
    Userchat findByUsername(String username);
}
