package com.haraif.real_time_chat_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthRequestDTO request) {
    return authService.register(request);
  }

}
