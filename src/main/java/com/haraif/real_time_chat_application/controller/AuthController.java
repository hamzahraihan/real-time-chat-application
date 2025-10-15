package com.haraif.real_time_chat_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haraif.real_time_chat_application.dto.AuthRequestDTO;
import com.haraif.real_time_chat_application.dto.AuthResponseDTO;
import com.haraif.real_time_chat_application.service.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthRequestDTO request) {
    return authService.register(request);
  }

  @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO dto) {
    return authService.login(dto);
  }

}
