package com.haraif.real_time_chat_application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haraif.real_time_chat_application.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
  Optional<AppUser> findByUsername(String username);
}