package com.haraif.real_time_chat_application.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haraif.real_time_chat_application.dto.AuthRequestDTO;
import com.haraif.real_time_chat_application.dto.AuthResponseDTO;
import com.haraif.real_time_chat_application.repository.AppUserRepository;
import com.haraif.real_time_chat_application.service.AuthService;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AppUserRepository appUserRepository;

  @Autowired
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    appUserRepository.deleteAll();
  }

  @Test
  void testRegisterSuccess() throws Exception {
    AuthRequestDTO dto = new AuthRequestDTO();
    dto.setUsername("john_doe");
    dto.setPassword("password");

    mockMvc.perform(
        post("/api/auth/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpectAll(status().isOk())
        .andDo(result -> {
          String json = result.getResponse().getContentAsString();
          AuthResponseDTO response = objectMapper.readValue(json, new TypeReference<AuthResponseDTO>() {
          });
          assertNotNull(response.getToken());
        });
  }
}
