package com.haraif.real_time_chat_application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.haraif.real_time_chat_application.dto.AuthRequestDTO;
import com.haraif.real_time_chat_application.dto.AuthResponseDTO;
import com.haraif.real_time_chat_application.model.AppUser;
import com.haraif.real_time_chat_application.repository.AppUserRepository;

@Service
public class AuthService {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AppUserRepository appUserRepository;

  @Transactional
  public ResponseEntity<AuthResponseDTO> register(AuthRequestDTO dto) {
    // checking if username is already taken
    if (appUserRepository.findByUsername(dto.getUsername()).isPresent()) {
      return ResponseEntity.badRequest().body(AuthResponseDTO.builder().error("username has been taken").build());
    }

    // create a new user if username is available
    AppUser user = AppUser.builder()
        .id(UUID.randomUUID().toString())
        .username(dto.getUsername())
        .password(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()))
        .build();

    appUserRepository.save(user);

    String token = jwtService.generateToken(user.getUsername());
    return ResponseEntity.ok(AuthResponseDTO.builder().token(token).build());
  }

  @Transactional(readOnly = true)
  public ResponseEntity<AuthResponseDTO> login(AuthRequestDTO dto) {
    AppUser user = appUserRepository.findByUsername(dto.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is wrong"));

    if (BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
      String token = jwtService.generateToken(user.getUsername());
      return ResponseEntity.ok(AuthResponseDTO.builder().token(token).build());
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is wrong");
    }
  }
}
