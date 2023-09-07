package com.quanloop.todo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quanloop.todo.config.filter.JwtFilter;
import com.quanloop.todo.config.model.AuthenticationRequest;
import com.quanloop.todo.config.user.model.Role;
import com.quanloop.todo.config.user.model.User;
import com.quanloop.todo.config.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SqlGroup({
        @Sql(value = "classpath:init/user-data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class JwtFilterTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    JwtUtil jWTUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtoken;
    private User user;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(jwtFilter).build();
    }

    @Test
    public void correct_jwt_header_with_valid_token() throws Exception {
        user = new User();
        user.setUserName("test-user");
        user.setPassword("test@123");
        user.setId(1L);
        user.setRoles(Set.of(new Role("ROLE_USER")));

        jwtoken = jWTUtil.generateToken(user);
        mockMvc.perform(get("/api/todos")
                        .header("Authorization", "Bearer " + jwtoken))
                .andExpect(status().isOk());
    }

    @Test
    public void unsuccessful_flow_jwt_header_with_invalid_token() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJrZXNoYW5pIiwiVXNlcm5hbWUiOiJBZG1pblVzZXIiLCJleHAiOjE2Nzk3NzYzMjIsImlhdCI6MTY3OTc3NjMyMn0.I6b0UPpvypzd-mjdedpB4vlmlYcc0iz5VZ5l90-6Qrs";
        Assertions.assertThrows(SignatureException.class, () -> mockMvc.perform(get("/api/todos")
                .header("Authorization", "Bearer " + invalidToken)));
    }

    @Test
    public void unsuccessful_flow_jwt_header_with_expired_token() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpbmJhbmsiLCJleHAiOjE2OTE5NDcxNDIsImlhdCI6MTY5MTkyOTE0Mn0.RTurw3NgarlV7aCPuPFiltdSGYyj997h_v3E4yODuPGBUNRWeQIz6mMVCEvUvfncjaNwkeHfOtSXZztM68plhg";
        Assertions.assertThrows(ExpiredJwtException.class, () -> mockMvc.perform(get("/api/todos")
                .header("Authorization", "Bearer " + expiredToken)));
    }

    @Test
    public void successful_flow_in_jwt_authentication_bypass_token() throws Exception {
        AuthenticationRequest authReq = new AuthenticationRequest("test-user", "test@123");
        mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authReq)))
                .andExpect(status().isOk());
    }

    @Test
    public void unsuccessful_flow_in_jwt_token_of_invalid_user() {
        user = new User();
        user.setUserName("not-valid-user");
        user.setPassword("test@123");
        user.setId(1L);
        user.setRoles(Set.of(new Role("ROLE_USER")));
        String jwtoken1 = jWTUtil.generateToken(user);
        Assertions.assertThrows(UsernameNotFoundException.class, () -> mockMvc.perform(get("/api/todos")
                .header("Authorization", "Bearer " + jwtoken1)));
    }

}
